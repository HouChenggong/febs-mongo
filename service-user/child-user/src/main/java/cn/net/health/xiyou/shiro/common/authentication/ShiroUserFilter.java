package cn.net.health.xiyou.shiro.common.authentication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author xiyou
 * @version 1.2
 * @date 2020/1/8 17:45
 */
public class ShiroUserFilter extends UserFilter {

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
//        super.redirectToLogin(request, response);
        JSONObject result = new JSONObject();
        result.put("msg", "登录失效或者未登录");
        result.put("code", 1000);
        result.put("success", false);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
