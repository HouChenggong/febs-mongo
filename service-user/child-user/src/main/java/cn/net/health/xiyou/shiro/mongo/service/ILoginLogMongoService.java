package cn.net.health.xiyou.shiro.mongo.service;

import cn.net.health.xiyou.shiro.mongo.entity.LoginLogMongo;
import cn.net.health.xiyou.shiro.mongo.entity.UserMongo;
import cn.net.health.xiyou.shiro.common.entity.QueryRequest;

import java.util.List;

/**
 * @author MrBird
 */
public interface ILoginLogMongoService {

    /**
     * 获取登录日志分页信息
     *
     * @param loginLog 传参
     * @param request  request
     * @return IPage<LoginLog>
     */
    List<LoginLogMongo> findLoginLogs(LoginLogMongo loginLog, QueryRequest request);

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志
     */
    void saveLoginLog(LoginLogMongo loginLog);

    /**
     * 删除登录日志
     *
     * @param ids 日志 id集合
     */
    void deleteLoginLogs(String[] ids);

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    List<LoginLogMongo>  findTodayVisitCount();

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp();

    /**
     * 获取系统近七天来的访问记录
     *
     * @param user 用户
     * @return 系统近七天来的访问记录
     */
    List<LoginLogMongo> findLastSevenDaysVisitCount(UserMongo user);
}
