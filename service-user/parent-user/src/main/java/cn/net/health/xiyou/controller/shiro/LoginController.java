package cn.net.health.xiyou.controller.shiro;


import cn.net.health.xiyou.controller.base.BaseController;
import cn.net.health.xiyou.shiro.common.entity.FebsResponse;
import cn.net.health.xiyou.shiro.common.exception.FebsException;
import cn.net.health.xiyou.shiro.common.utils.MD5Util;
import cn.net.health.xiyou.shiro.mongo.common.ValidateCodeMongoService;
import cn.net.health.xiyou.shiro.mongo.dao.UserMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.LoginLogMongo;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.mongo.entity.UserMongo;
import cn.net.health.xiyou.shiro.mongo.service.ILoginLogMongoService;
import cn.net.health.xiyou.shiro.mongo.service.IUserMongoService;
import com.google.common.collect.HashMultiset;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Validated
@RestController
public class LoginController extends BaseController {


    @Autowired
    private ILoginLogMongoService loginLogMongoService;

    @Autowired
    private IUserMongoService userMongoService;

    @Autowired
    private UserMongoDbDao userMongoDbDao;

    @Autowired
    private ValidateCodeMongoService validateCodeMongoService;

    @GetMapping("login")
    public ResponseResult login() {
        return new ResponseResult(false, "请登陆！");
    }

    /**
     * mongo替换了
     *
     * @param username
     * @param password
     * @param verifyCode
     * @param rememberMe
     * @param request
     * @return
     * @throws FebsException
     */
    @PostMapping("login")
//    @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix = "limit")
    public FebsResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeMongoService.check(session.getId(), verifyCode);
        password = MD5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        UserMongo userMongo = userMongoService.findUserDetail(username);
        // 保存登录日志
        LoginLogMongo loginLog = new LoginLogMongo();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        loginLog.setUserId(String.valueOf(userMongo.getUserId()));
        loginLogMongoService.saveLoginLog(loginLog);
        session.setAttribute("userId", userMongo.getUserId());
        return new FebsResponse().success();
    }


    /**
     * mongo替换了
     *
     * @param username
     * @return
     */
    @GetMapping("index/{username}")
    public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String username
    ) {
        long parmUserId = 8;
        Session session = super.getSession();
        Long sessionUserId = (Long) session.getAttribute("userId");
//        UserSessionCommonService.sessionIsTrue(sessionUserId, parmUserId);
        // 更新登录时间
        this.userMongoService.updateLoginTime(String.valueOf(sessionUserId));
        Map<String, Object> data = new HashMap<>(8);
        // 获取系统访问记录
        Long totalVisitCount = this.loginLogMongoService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        List<LoginLogMongo> todayVisitCount = this.loginLogMongoService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount.size());
        HashMultiset<String> ipMultiSet = HashMultiset.create();
        HashMultiset<String> userSet = HashMultiset.create();
        todayVisitCount.forEach(one -> {
            ipMultiSet.add(one.getIp());
            userSet.add(one.getUserId());
        });
        data.put("todayIp", ipMultiSet.elementSet().size());
        data.put("todayUser", userSet.elementSet().size());
        // 获取近期系统访问记录
//        List<LoginLogMongo> lastSevenVisitCount = this.loginLogMongoService.findLastSevenDaysVisitCount(null);
//        data.put("lastSevenVisitCount", lastSevenVisitCount);

//        List<Map<String, Object>> lastSevenUserVisitCount = this.loginLogService.findLastSevenDaysVisitCount(param);
//        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new FebsResponse().success().data(data);
    }

    /**
     * mongo替换了
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws FebsException
     */
    @GetMapping("images/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, FebsException {
        validateCodeMongoService.create(request, response);
    }


}
