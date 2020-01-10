package cn.net.health.xiyou.shiro.mongo.service.impl;

import cn.net.health.xiyou.shiro.common.authentication.ShiroRealm;
import cn.net.health.xiyou.shiro.common.utils.FebsUtil;
import cn.net.health.xiyou.shiro.common.utils.SnowFlake;
import cn.net.health.xiyou.shiro.mongo.common.TreeMongoUtil;
import cn.net.health.xiyou.shiro.mongo.dao.MenuMongoDao;
import cn.net.health.xiyou.shiro.mongo.dao.UserMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.service.IMenuMongoService;
import cn.net.health.xiyou.shiro.mongo.service.IRoleMenuMongoService;


import cn.net.health.xiyou.shiro.mongo.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author MrBird
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuMongoServiceImpl implements IMenuMongoService {

    @Autowired
    private IRoleMenuMongoService roleMenuMongoService;
    @Autowired
    private ShiroRealm shiroRealm;

    @Autowired
    private MenuMongoDao menuMongoDao;
    @Autowired
    private UserMongoDbDao userMongoDbDao;


    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Override
    public List<MenuMongo> findUserPermissions(String username) {
        UserMongo userMongo = FebsUtil.getCurrentUserMongo();
        List<String> roleIds = new ArrayList<>(8);
        if (userMongo != null && userMongo.getRoleMap() != null && userMongo.getRoleMap().size() > 0) {
            for (RoleMongo one : userMongo.getRoleMap()) {
                roleIds.add(one.getRoleId());
            }
        }
        List<RoleMenuMongo> roleMenuMongos = roleMenuMongoService.selectMenuIdsByRoleIds(roleIds);
        List<String> menuIds = new ArrayList<>(8);
        roleMenuMongos.forEach(one -> {
            menuIds.add(one.getMenuId());
        });
        List<MenuMongo> menuMongos = menuMongoDao.queryListColumnIn(menuIds, "_id");
        return menuMongos;
    }

    @Override
    public List<MenuMongo> findUserPermissionsNotButton(String username) {
        UserMongo userMongo = FebsUtil.getCurrentUserMongo();
        List<String> roleIds = new ArrayList<>(8);
        if (userMongo != null && userMongo.getRoleMap() != null && userMongo.getRoleMap().size() > 0) {
            for (RoleMongo one : userMongo.getRoleMap()) {
                roleIds.add(one.getRoleId());
            }
        }
        List<RoleMenuMongo> roleMenuMongos = roleMenuMongoService.selectMenuIdsByRoleIds(roleIds);
        List<String> menuIds = new ArrayList<>(8);
        roleMenuMongos.forEach(one -> {
            menuIds.add(one.getMenuId());
        });
        MenuMongo menuMongo = new MenuMongo();
        menuMongo.setType(MenuMongo.TOP_NODE);
        Sort sort = Sort.by(
                Sort.Order.desc("createTime"));
        ResponseResult responseResult = menuMongoDao.queryPageByEntityAndColumnIn
                (menuMongo, 0, 10000, menuIds, "_id", "", "", sort);
        return (List<MenuMongo>) responseResult.getData();
    }

    @Override
    public MenuMogonTree<MenuMongo> findUserMenus(String username) {
        List<MenuMongo> menuMongos = this.findUserPermissionsNotButton(username);
        List<MenuMogonTree<MenuMongo>> trees = this.convertMenus(menuMongos);
        return TreeMongoUtil.buildMenuTree(trees);
    }

    @Override
    public MenuMogonTree<MenuMongo> findMenus(MenuMongo menu) {

        List<MenuMongo> menus = this.menuMongoDao.queryAll(menu);
        List<MenuMogonTree<MenuMongo>> trees = this.convertMenus(menus);

        return TreeMongoUtil.buildMenuTree(trees);
    }

    @Override
    public List<MenuMongo> findMenuList(MenuMongo menu) {
        return null;
    }

    @Override
    @Transactional
    public void createMenu(MenuMongo menu) {
        menu.setMenuId(String.valueOf(snowFlake.makeNextId()));
        menu.setCreateTime(new Date());
        if (menu.getSystem() == null) {
            menu.setSystem(false);
        }

        this.setMenu(menu);
        this.menuMongoDao.insertOne(menu);
    }


    @Override
    @Transactional
    public void updateMenu(MenuMongo menu) {
        menu.setModifyTime(new Date());
        this.setMenu(menu);
        this.menuMongoDao.updateFirstById(menu.getMenuId(), menu);

        shiroRealm.clearCache();
    }

    @Override
    @Transactional
    public ResponseResult deleteMeuns(String menuId) {
        MenuMongo menuMongo = menuMongoDao.queryById(menuId);
        if (menuMongo.getSystem() == null || menuMongo.getSystem() == true) {
            return new ResponseResult(false, "系统属性不能删除");
        }
        this.delete(menuId);
        shiroRealm.clearCache();
        return new ResponseResult(true);
    }

    @Override
    public Boolean findByHrefAndUser(String userId, String href) {
        MenuMongo menuMongo = new MenuMongo();
        menuMongo.setPerms(href);
        menuMongo = menuMongoDao.queryOneByEntity(menuMongo);
        if (menuMongo != null && menuMongo.getMenuId() != null) {
            String menuId = menuMongo.getMenuId();
            List<RoleMenuMongo> roleMenuMongos = roleMenuMongoService.selectMenuIdsByMenuId(menuId);
            if (roleMenuMongos != null && roleMenuMongos.size() > 0) {
                Set<String> roleIds = new HashSet<>(4);
                roleMenuMongos.forEach(one -> {
                    roleIds.add(one.getRoleId());
                });
                UserMongo userMongo = userMongoDbDao.queryByLongId(Long.valueOf(userId));
                Set<String> userRoleIds = new HashSet<>(4);
                if (userMongo != null && userMongo.getRoleMap() != null && userMongo.getRoleMap().size() > 0) {
                    for (RoleMongo one : userMongo.getRoleMap()) {
                        userRoleIds.add(one.getRoleId());
                    }
                }

                roleIds.retainAll(userRoleIds);
                if (roleIds.size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<MenuMogonTree<MenuMongo>> convertMenus(List<MenuMongo> menus) {
        List<MenuMogonTree<MenuMongo>> trees = new ArrayList<>();
        menus.forEach(menu -> {
            MenuMogonTree<MenuMongo> tree = new MenuMogonTree<>();
            tree.setId(String.valueOf(menu.getMenuId()));
            tree.setParentId(String.valueOf(menu.getParentId()));
            tree.setTitle(menu.getMenuName());
            tree.setIcon(menu.getIcon());
            tree.setHref(menu.getUrl());
            tree.setData(menu);
            trees.add(tree);
        });
        return trees;
    }

    private void setMenu(MenuMongo menu) {
        if (StringUtils.isBlank(menu.getParentId())) {
            menu.setParentId(MenuMongo.TOP_NODE);
        }
        if (MenuMongo.TYPE_BUTTON.equals(menu.getType())) {
            menu.setUrl(null);
            menu.setIcon(null);
        }
    }

    /**
     * 删除权限的时候删除角色和权限关联表
     * 如果是弗雷权限，把子权限也要删除
     *
     * @param menuId
     */
    private void delete(String menuId) {

        MenuMongo menuMongo = new MenuMongo();
        menuMongo.setParentId(menuId);
        //删除子关联关系
        this.menuMongoDao.deleteMany(menuMongo);
        //删除父关联关系
        this.menuMongoDao.deleteById(menuId);
        //删除权限相关的角色表
        this.roleMenuMongoService.deleteRoleMenusByMenuId(menuId);

    }
}
