package cn.net.health.xiyou.shiro.monitor.service;

import cn.net.health.xiyou.shiro.monitor.entity.ActiveUser;

import java.util.List;

/**
 * @author MrBird
 */
public interface ISessionService {

    /**
     * 获取在线用户列表
     *
     * @param username 用户名
     * @param userId   当前用户的ID
     * @return List<ActiveUser>
     */
    List<ActiveUser> list(String username, String userId);

    /**
     * 踢出用户
     *
     * @param sessionId sessionId
     */
    void forceLogout(String sessionId);
}
