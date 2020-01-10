package cn.net.cobot.system.controller;

import cn.net.cobot.common.annotation.ControllerEndpoint;
import cn.net.cobot.common.controller.BaseController;
import cn.net.cobot.common.entity.FebsResponse;
import cn.net.cobot.common.entity.QueryRequest;
import cn.net.cobot.common.exception.FebsException;
import cn.net.cobot.mongo.entity.ResponseResult;
import cn.net.cobot.mongo.entity.UserMongo;
import cn.net.cobot.mongo.service.IUserMongoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@Validated
@RestController
@RequestMapping("user")
public class UserController extends BaseController {


    @Autowired
    private IUserMongoService userMongoService;

    @GetMapping("{username}")
    public UserMongo getUser(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userMongoService.findUserDetail(username);
    }

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username, String userId) {
        return this.userMongoService.findByName(username) == null || StringUtils.isNotBlank(userId);
    }

    @GetMapping("list")
    @RequiresPermissions("user:view")
    public FebsResponse userList(UserMongo user, QueryRequest request) {
        user.setUserId(super.getCurrentUserMongo().getUserId() );
        ResponseResult userMongos = userMongoService.findUserDetail(user, request);

        Map<String, Object> rows = new HashMap<>(8);
        rows.put("rows", userMongos.getData());
        rows.put("total", Long.valueOf(userMongos.getMsg()));

        return new FebsResponse().success().data(rows);
    }

    @PostMapping("update")
    @RequiresPermissions("user:update")
    @ControllerEndpoint(operation = "修改用户", exceptionMessage = "修改用户失败")
    public FebsResponse updateUser(@Valid UserMongo user) {
        if (user.getUserId() == null) {
            throw new FebsException("用户ID为空");
        }
        this.userMongoService.updateUser(user);
        return new FebsResponse().success();
    }

    @PostMapping
    @RequiresPermissions("user:add")
    @ControllerEndpoint(operation = "新增用户", exceptionMessage = "新增用户失败")
    public FebsResponse addUser(@Valid UserMongo user) {
        this.userMongoService.createUser(user);
        return new FebsResponse().success();
    }
}
