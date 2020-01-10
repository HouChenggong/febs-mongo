package cn.net.health.xiyou.shiro.mongo.service;


import cn.net.health.xiyou.shiro.mongo.entity.SystemLogMongo;
import cn.net.health.xiyou.shiro.common.entity.FebsConstant;
import cn.net.health.xiyou.shiro.common.entity.QueryRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author MrBird
 */
public interface ILogMongoService   {

    /**
     * 查询操作日志分页
     *
     * @param systemLog 日志
     * @param request   QueryRequest
     * @return IPage<SystemLog>
     */
    List<SystemLogMongo> findLogs(SystemLogMongo systemLog, QueryRequest request);

    /**
     * 删除操作日志
     *
     * @param logIds 日志 ID集合
     */
    void deleteLogs(String[] logIds);

    /**
     * 异步保存操作日志
     *
     * @param point     切点
     * @param method    Method
     * @param request   HttpServletRequest
     * @param operation 操作内容
     * @param start     开始时间
     */
    @Async(FebsConstant.ASYNC_POOL)
    void saveLog(ProceedingJoinPoint point, Method method, HttpServletRequest request, String operation, long start);
}
