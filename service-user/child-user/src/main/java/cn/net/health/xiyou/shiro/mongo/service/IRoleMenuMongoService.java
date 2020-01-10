package cn.net.health.xiyou.shiro.mongo.service;


import cn.net.health.xiyou.shiro.mongo.entity.RoleMenuMongo;

import java.util.List;

/**
 * @author MrBird
 */
public interface IRoleMenuMongoService {

    /**
     * 通过角色 id 删除
     *
     * @param roleId 角色 id
     */
    void deleteRoleMenusByRoleId(String roleId);

    /**
     * 通过菜单（按钮）id 删除
     *
     * @param menuId 菜单（按钮）id
     */
    void deleteRoleMenusByMenuId(String menuId);

    /**
     * 保存多个
     *
     * @param roleMenuMongos
     * @return
     */
    int saveBatch(List<RoleMenuMongo> roleMenuMongos);


    /**
     * 根据用户的角色ID列表，获取权限的列表
     *
     * @param roleIds
     * @return
     */
    List<RoleMenuMongo> selectMenuIdsByRoleIds(List<String> roleIds);


    /**
     * 根据权限ID查询所有拥有这个权限的角色ID
     *
     * @param menuId
     * @return
     */
    List<RoleMenuMongo> selectMenuIdsByMenuId(String menuId);
}
