package cn.net.health.xiyou.shiro.monitor.service.impl;


import cn.net.health.xiyou.shiro.common.entity.QueryRequest;
import cn.net.health.xiyou.shiro.common.utils.AddressUtil;
import cn.net.health.xiyou.shiro.common.utils.IPUtil;
import cn.net.health.xiyou.shiro.mongo.dao.SystemLogMongoDao;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.mongo.entity.SystemLogMongo;
import cn.net.health.xiyou.shiro.mongo.entity.UserMongo;
import cn.net.health.xiyou.shiro.monitor.service.ILogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author MrBird
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl implements ILogService {

    @Autowired
    private SystemLogMongoDao loginLogMongoDbDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseResult findLogs(SystemLogMongo systemLog, QueryRequest request) {
        String likeValue = "";
        Sort sort = Sort.by(
                Sort.Order.desc("createTime"));
        if (StringUtils.isNotBlank(systemLog.getUsername())) {
            likeValue = systemLog.getUsername();
            systemLog.setUsername(null);
        }
        ResponseResult result = this.loginLogMongoDbDao.queryPageByEntity(systemLog
                , (request.getPageNum() - 1) * request.getPageSize()
                , request.getPageSize(),
                "username", likeValue, sort);
        return result;
    }

    @Override
    @Transactional
    public void deleteLogs(String[] logIds) {
        List<String> list = Arrays.asList(logIds);
        this.loginLogMongoDbDao.deleteManyByColumnIn(list, "_id");
    }

    @Override
    public void saveLog(ProceedingJoinPoint point, Method method, HttpServletRequest request, String operation, long start) {
        SystemLogMongo systemLog = new SystemLogMongo();
        // 设置 IP地址
        String ip = IPUtil.getIpAddr(request);
        systemLog.setIp(ip);
        // 设置操作用户
        UserMongo user = (UserMongo) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            systemLog.setUsername(user.getUsername());
        }
        // 设置耗时
        systemLog.setTime(System.currentTimeMillis() - start);
        // 设置操作描述
        systemLog.setOperation(operation);
        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = method.getName();
        systemLog.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = point.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            systemLog.setParams(params.toString());
        }
        systemLog.setCreateTime(new Date());
        systemLog.setLocation(AddressUtil.getCityInfo(ip));
        // 保存系统日志
        this.loginLogMongoDbDao.insertOne(systemLog);
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Set set = ((Map) args[i]).keySet();
                    List<Object> list = new ArrayList<>();
                    List<Object> paramList = new ArrayList<>();
                    for (Object key : set) {
                        list.add(((Map) args[i]).get(key));
                        paramList.add(key);
                    }
                    return handleParams(params, list.toArray(), paramList);
                } else {
                    if (args[i] instanceof Serializable) {
                        Class<?> aClass = args[i].getClass();
                        try {
                            aClass.getDeclaredMethod("toString", new Class[]{null});
                            // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                        } catch (NoSuchMethodException e) {
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                        }
                    } else if (args[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) args[i];
                        params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                    } else {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                    }
                }
            }
        } catch (Exception ignore) {
            params.append("参数解析失败");
        }
        return params;
    }
}
