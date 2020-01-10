package cn.net.health.xiyou.controller.shiro;



import cn.net.health.xiyou.controller.base.BaseController;
import cn.net.health.xiyou.shiro.common.entity.FebsResponse;
import cn.net.health.xiyou.shiro.common.entity.QueryRequest;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.mongo.entity.RoleMongo;
import cn.net.health.xiyou.shiro.mongo.service.IRoleMongoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
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
    public FebsResponse addRole(@Valid RoleMongo role) {
        this.roleMongoService.createRole(role);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{roleIds}")
    @RequiresPermissions("role:delete")
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
    public FebsResponse updateRole(RoleMongo role) {
        this.roleMongoService.updateRole(role);
        return new FebsResponse().success();
    }


}
