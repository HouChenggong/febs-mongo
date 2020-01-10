package cn.net.cobot.monitor.service.impl;

import cn.net.cobot.common.utils.AddressUtil;
import cn.net.cobot.common.utils.DateUtil;
import cn.net.cobot.common.utils.FebsUtil;
import cn.net.cobot.mongo.entity.UserMongo;
import cn.net.cobot.mongo.service.IMenuMongoService;
import cn.net.cobot.monitor.entity.ActiveUser;
import cn.net.cobot.monitor.service.ISessionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class SessionServiceImpl implements ISessionService {

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private IMenuMongoService menuMongoService;

    @Override
    public List<ActiveUser> list(String username, String userId) {
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
        Boolean selectAll = menuMongoService.findByHrefAndUser(userId, "online:viewAll");
        List<ActiveUser> list = new ArrayList<>();
        UserMongo userMongo = FebsUtil.getCurrentUserMongo();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            ActiveUser activeUser = new ActiveUser();
            UserMongo user;
            SimplePrincipalCollection principalCollection;
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (UserMongo) principalCollection.getPrimaryPrincipal();
                activeUser.setUsername(user.getUsername());
                activeUser.setUserId(user.getUserId().toString());
            }
            Boolean isSessionUser = user.getUsername().equals(userMongo.getUsername());

            if (selectAll || isSessionUser) {
                activeUser.setId((String) session.getId());
                activeUser.setHost(session.getHost());
                activeUser.setStartTimestamp(DateUtil.getDateFormat(session.getStartTimestamp(), DateUtil.FULL_TIME_SPLIT_PATTERN));
                activeUser.setLastAccessTime(DateUtil.getDateFormat(session.getLastAccessTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
                long timeout = session.getTimeout();
                activeUser.setStatus(timeout == 0L ? "0" : "1");
                String address = AddressUtil.getCityInfo(activeUser.getHost());
                activeUser.setLocation(address);
                activeUser.setTimeout(timeout);
                if (StringUtils.equals(currentSessionId, activeUser.getId())) {
                    activeUser.setCurrent(true);
                }
                if (StringUtils.isBlank(username)
                        || activeUser.getUsername().contains(username)) {
                    list.add(activeUser);
                }
            }
        }
        return list;
    }

    @Override
    public void forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        session.setTimeout(0);
        session.stop();
        sessionDAO.delete(session);
    }
}
