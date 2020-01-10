package cn.net.health.xiyou.controller.base;


import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * @author H1871
 */
@Controller
public class PageException implements ErrorController {


    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public ResponseResult handleError(HttpServletRequest request, Model model) {
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        model.addAttribute("statusCode", statusCode);
        System.out.println(statusCode);
        ResponseResult result = new ResponseResult(false, "错误：" + statusCode);
        result.setCode(statusCode);
        return result;


    }
}

