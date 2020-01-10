package cn.net.cobot.system.controller;


import cn.net.cobot.common.annotation.ControllerEndpoint;
import cn.net.cobot.common.controller.BaseController;
import cn.net.cobot.common.entity.FebsResponse;
import cn.net.cobot.common.entity.QueryRequest;
import cn.net.cobot.mongo.entity.ResponseResult;
import cn.net.cobot.mongo.entity.RoleMongo;
import cn.net.cobot.mongo.service.IRoleMongoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {


    @Autowired
    private IRoleMongoService roleMongoService;

    @GetMapping
    public FebsResponse getAllRoles(RoleMongo role) {
        return new FebsResponse().success().data(roleMongoService.findRoles(role));
    }

    @GetMapping("list")
    @RequiresPermissions("role:view")
    public Map<String, Object> roleList(RoleMongo role, QueryRequest request) {
        ResponseResult result = this.roleMongoService.findRoles(role, request);

        Map<String, Object> map = new HashMap<>(8);
        map.put("rows", result.getData());
        map.put("total", Long.valueOf(result.getMsg()));
        Map<String, Object> res = new HashMap<>(8);
        res.put("data", map);
        res.put("code", 200);
        return res;
    }

    @PostMapping
    @RequiresPermissions("role:add")
    @ControllerEndpoint(operation = "新增角色", exceptionMessage = "新增角色失败")
    public FebsResponse addRole(@Valid RoleMongo role) {
        this.roleMongoService.createRole(role);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{roleIds}")
    @RequiresPermissions("role:delete")
    @ControllerEndpoint(operation = "删除单个角色", exceptionMessage = "删除角色失败")
    public FebsResponse deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) {
        ResponseResult result = this.roleMongoService.deleteRole(roleIds);
        if (result.isSuccess()) {
            return new FebsResponse().success();
        } else {
            return new FebsResponse().fail();
        }
    }

    @PostMapping("update")
    @RequiresPermissions("role:update")
    @ControllerEndpoint(operation = "修改角色", exceptionMessage = "修改角色失败")
    public FebsResponse updateRole(RoleMongo role) {
        this.roleMongoService.updateRole(role);
        return new FebsResponse().success();
    }


}
