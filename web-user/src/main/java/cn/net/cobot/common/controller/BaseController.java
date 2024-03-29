package cn.net.cobot.common.controller;

import cn.net.cobot.mongo.entity.UserMongo;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
public class BaseController {

    private Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected UserMongo getCurrentUser() {
        return (UserMongo) getSubject().getPrincipal();
    }

    protected UserMongo getCurrentUserMongo() {
        return (UserMongo) getSubject().getPrincipal();
    }

    protected Session getSession() {
        return getSubject().getSession();
    }

    protected Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }




    protected Map<String, Object> getDataMap(List<T> list, int dataMapInitialCapacity) {
        Map<String, Object> data = new HashMap<>(dataMapInitialCapacity);
        data.put("rows", list);
        data.put("total", list.size());
        return data;
    }

}
