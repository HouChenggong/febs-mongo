//package cn.net.health.cobot.shiro.common.authentication;
//
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.PrintWriter;
//
///**
// * @author xiyou
// * @version 1.2
// * @date 2019/11/6 11:45
// */
//@Slf4j
//public class CorsAuthenticationFilter extends FormAuthenticationFilter {
//
//
//
//    public CorsAuthenticationFilter() {
//        super();
//    }
//
//    @Override
//    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        //Always return true if the request's method is OPTIONSif (request instanceof HttpServletRequest) {
//        if ("OPTIONS".equals(((HttpServletRequest) request).getMethod().toUpperCase())) {
//            return true;
//        }
//
//        return super.isAccessAllowed(request, response, mappedValue);
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//
//
//        if (isLoginRequest(request, response)) {
//            if (isLoginSubmission(request, response)) {
//                if (log.isTraceEnabled()) {
//                    log.trace("Login submission detected.  Attempting to execute login.");
//                }
//                return executeLogin(request, response);
//            } else {
//                if (log.isTraceEnabled()) {
//                    log.trace("Login page view.");
//                }
//                //allow them to see the login page ;)
//                return true;
//            }
//        } else {
//            HttpServletRequest req = (HttpServletRequest) request;
//            HttpSession session = req.getSession();
//
//            HttpServletResponse resp = (HttpServletResponse) response;
//
//            if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
//                resp.setStatus(HttpStatus.OK.value());
//                return true;
//            }
//
//            if (log.isTraceEnabled()) {
//                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
//                        "Authentication url [" + getLoginUrl() + "]");
//            }
//
//            resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
//            resp.setHeader("Access-Control-Allow-Credentials", "true");
//            resp.setContentType("application/json; charset=utf-8");
//            resp.setCharacterEncoding("UTF-8");
//            PrintWriter out = resp.getWriter();
//            JSONObject result = new JSONObject();
//            result.put("msg", "登录失效或者未登录");
//            result.put("code", 1000);
//            result.put("success", false);
//            out.println(result);
//            out.flush();
//            out.close();
//
//            return false;
//        }
//    }
//}
