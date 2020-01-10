package cn.net.cobot.system.controller;


import cn.net.cobot.common.annotation.ControllerEndpoint;
import cn.net.cobot.common.controller.BaseController;
import cn.net.cobot.common.entity.FebsResponse;
import cn.net.cobot.common.exception.FebsException;
import cn.net.cobot.mongo.entity.MenuMogonTree;
import cn.net.cobot.mongo.entity.MenuMongo;
import cn.net.cobot.mongo.entity.ResponseResult;
import cn.net.cobot.mongo.entity.UserMongo;
import cn.net.cobot.mongo.service.IMenuMongoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {



    @Autowired
    private IMenuMongoService menuMongoService;

    @GetMapping("{username}")
    public FebsResponse getUserMenus(@NotBlank(message = "{required}") @PathVariable String username) throws FebsException {
        UserMongo currentUser = super.getCurrentUserMongo();
        if (!StringUtils.equalsIgnoreCase(username, currentUser.getUsername())) {
            throw new FebsException("您无权获取别人的菜单");
        }
        MenuMogonTree<MenuMongo> userMenus = this.menuMongoService.findUserMenus(username);
        return new FebsResponse().data(userMenus);
    }

    @GetMapping("tree")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse getMenuTree(MenuMongo menu) {
        MenuMogonTree<MenuMongo> menus = this.menuMongoService.findMenus(menu);
        return new FebsResponse().success().data(menus.getChilds());
    }

    @PostMapping
    @RequiresPermissions("menu:add")
    @ControllerEndpoint(operation = "新增菜单/按钮", exceptionMessage = "新增菜单/按钮失败")
    public FebsResponse addMenu(@Valid MenuMongo menu) {
        this.menuMongoService.createMenu(menu);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{menuIds}")
    @RequiresPermissions("menu:delete")
    @ControllerEndpoint(operation = "删除菜单/按钮", exceptionMessage = "删除菜单/按钮失败")
    public FebsResponse deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) {
        ResponseResult result = this.menuMongoService.deleteMeuns(menuIds);
        if (result.isSuccess()) {
            return new FebsResponse().success();
        } else {
            FebsResponse response = new FebsResponse();
            return response;
        }

    }

    @PostMapping("update")
    @RequiresPermissions("menu:update")
    @ControllerEndpoint(operation = "修改菜单/按钮", exceptionMessage = "修改菜单/按钮失败")
    public FebsResponse updateMenu(@Valid MenuMongo menu) {
        this.menuMongoService.updateMenu(menu);
        return new FebsResponse().success();
    }


}
