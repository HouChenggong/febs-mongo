package cn.net.cobot.monitor.service.impl;

import cn.net.cobot.common.entity.QueryRequest;
import cn.net.cobot.common.utils.AddressUtil;
import cn.net.cobot.common.utils.HttpContextUtil;
import cn.net.cobot.common.utils.IPUtil;
import cn.net.cobot.mongo.dao.LoginLogMongoDbDao;
import cn.net.cobot.mongo.entity.LoginLogMongo;
import cn.net.cobot.mongo.entity.UserMongo;
import cn.net.cobot.monitor.service.ILoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl implements ILoginLogService {

    @Autowired
    private LoginLogMongoDbDao loginLogMongoDbDao;

    @Override
    public List<LoginLogMongo> findLoginLogs(LoginLogMongo loginLog, QueryRequest request) {
        Sort sort = Sort.by(
                Sort.Order.desc("loginTime"));
        return (List<LoginLogMongo>) this.loginLogMongoDbDao.queryPageByEntity(loginLog
                , (request.getPageNum() - 1) * request.getPageSize()
                , request.getPageSize(),"","",sort).getData();
    }

    @Override
    @Transactional
    public void saveLoginLog(LoginLogMongo loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        this.loginLogMongoDbDao.insertOne(loginLog);
    }

    @Override
    @Transactional
    public void deleteLoginLogs(String[] ids) {
        List<String> list = Arrays.asList(ids);
        this.loginLogMongoDbDao.deleteManyByColumnIn(list, "_id");
    }

    @Override
    public Long findTotalVisitCount() {
      return 0L;
    }

    @Override
    public Long findTodayVisitCount() {
        return 0L;
    }

    @Override
    public Long findTodayIp() {
        return 0L;
    }

    @Override
    public List<Map<String, Object>> findLastSevenDaysVisitCount(UserMongo user) {
        return null;
    }
}
