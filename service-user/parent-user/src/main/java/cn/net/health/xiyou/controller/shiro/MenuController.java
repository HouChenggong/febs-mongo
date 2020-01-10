package cn.net.health.xiyou.controller.shiro;


import cn.net.health.xiyou.controller.base.BaseController;
import cn.net.health.xiyou.shiro.common.entity.FebsResponse;
import cn.net.health.xiyou.shiro.common.exception.FebsException;
import cn.net.health.xiyou.shiro.mongo.entity.MenuMogonTree;
import cn.net.health.xiyou.shiro.mongo.entity.MenuMongo;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.mongo.entity.UserMongo;
import cn.net.health.xiyou.shiro.mongo.service.IMenuMongoService;
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

    public FebsResponse getMenuTree(MenuMongo menu) {
        MenuMogonTree<MenuMongo> menus = this.menuMongoService.findMenus(menu);
        return new FebsResponse().success().data(menus.getChilds());
    }

    @PostMapping
    @RequiresPermissions("menu:add")

    public FebsResponse addMenu(@Valid MenuMongo menu) {
        this.menuMongoService.createMenu(menu);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{menuIds}")
    @RequiresPermissions("menu:delete")

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

    public FebsResponse updateMenu(@Valid MenuMongo menu) {
        this.menuMongoService.updateMenu(menu);
        return new FebsResponse().success();
    }


}
