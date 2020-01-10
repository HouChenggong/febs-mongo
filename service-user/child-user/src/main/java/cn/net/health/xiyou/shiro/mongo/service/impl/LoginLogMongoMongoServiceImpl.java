package cn.net.health.xiyou.shiro.mongo.service.impl;

import cn.net.health.xiyou.shiro.common.entity.QueryRequest;
import cn.net.health.xiyou.shiro.common.utils.AddressUtil;
import cn.net.health.xiyou.shiro.common.utils.HttpContextUtil;
import cn.net.health.xiyou.shiro.common.utils.IPUtil;
import cn.net.health.xiyou.shiro.common.utils.SnowFlake;
import cn.net.health.xiyou.shiro.mongo.dao.LoginLogMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.LoginLogMongo;
import cn.net.health.xiyou.shiro.mongo.entity.UserMongo;
import cn.net.health.xiyou.shiro.mongo.service.ILoginLogMongoService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author MrBird
 */
@Service
public class LoginLogMongoMongoServiceImpl implements ILoginLogMongoService {
    @Autowired
    private LoginLogMongoDbDao loginLogMongoDbDao;
    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Override
    public List<LoginLogMongo> findLoginLogs(LoginLogMongo loginLogMongo, QueryRequest request) {
        return null;
    }

    @Override
    public void saveLoginLog(LoginLogMongo loginLogMongo) {
        loginLogMongo.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLogMongo.setIp(ip);
        loginLogMongo.setId(String.valueOf(snowFlake.makeNextId()));
        loginLogMongo.setLocation(AddressUtil.getCityInfo(ip));
        loginLogMongoDbDao.insertOne(loginLogMongo);
    }

    @Override
    public void deleteLoginLogs(String[] ids) {
//        List<String> list = Arrays.asList(ids);
//        baseMapper.deleteBatchIds(list);
    }

    @Override
    public Long findTotalVisitCount() {
        LoginLogMongo loginLogMongo = new LoginLogMongo();
        return loginLogMongoDbDao.getCount(loginLogMongo);
    }

    @Override
    public List<LoginLogMongo> findTodayVisitCount() {
        LoginLogMongo loginLogMongo = new LoginLogMongo();
        List<LoginLogMongo> todayList = loginLogMongoDbDao.getTodayData("loginTime", loginLogMongo);
        return todayList;
    }

    @Override
    public Long findTodayIp() {
        return Long.valueOf(1);
    }

    @Override
    public List<LoginLogMongo> findLastSevenDaysVisitCount(UserMongo user) {

        return null;
    }
}
