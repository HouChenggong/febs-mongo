package cn.net.health.xiyou.shiro.mongo.service;

import cn.net.health.xiyou.shiro.mongo.entity.MenuMogonTree;
import cn.net.health.xiyou.shiro.mongo.entity.MenuMongo;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;

import java.util.List;

/**
 * @author MrBird
 */
public interface IMenuMongoService {

    /**
     * 查找用户权限集
     *
     * @param username 用户名
     * @return 用户权限集合
     */
    List<MenuMongo> findUserPermissions(String username);


    /**
     * 查找用户权限集不是Button的所有权限
     *
     * @param username 用户名
     * @return 用户权限集合
     */
    List<MenuMongo> findUserPermissionsNotButton(String username);

    /**
     * 查找用户菜单集合
     *
     * @param username 用户名
     * @return 用户菜单集合
     */
    MenuMogonTree<MenuMongo> findUserMenus(String username);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    MenuMogonTree<MenuMongo> findMenus(MenuMongo menu);

    /**
     * 查找所有的菜单/按钮
     *
     * @return MenuTree<Menu>
     */
    List<MenuMongo> findMenuList(MenuMongo menu);

    /**
     * 新增菜单（按钮）
     *
     * @param menu 菜单（按钮）对象
     */
    void createMenu(MenuMongo menu);

    /**
     * 修改菜单（按钮）
     *
     * @param menu 菜单（按钮）对象
     */
    void updateMenu(MenuMongo menu);

    /**
     * 删除菜单（按钮）
     *
     * @param menuId 菜单（按钮）id
     */
    ResponseResult deleteMeuns(String menuId);


    /**
     * 根据路由查询某个权限是否属于当前用户
     *
     * @param userId
     * @param href
     * @return
     */
    Boolean findByHrefAndUser(String userId, String href);
}
