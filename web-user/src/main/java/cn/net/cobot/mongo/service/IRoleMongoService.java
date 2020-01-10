package cn.net.cobot.mongo.service;


import cn.net.cobot.common.entity.QueryRequest;
import cn.net.cobot.mongo.entity.ResponseResult;
import cn.net.cobot.mongo.entity.RoleMongo;

import java.util.List;

/**
 * @author MrBird
 */
public interface IRoleMongoService {


    /**
     * 通过用户名查找用户角色
     *
     * @param username 用户名
     * @return 用户角色集合
     */
    List<RoleMongo> findUserRole(String username);

    /**
     * 查找所有角色
     *
     * @param role 角色对象（用于传递查询条件）
     * @return 角色集合
     */
    List<RoleMongo> findRoles(RoleMongo role);

    /**
     * 查找所有角色（分页）
     *
     * @param role    角色对象（用于传递查询条件）
     * @param request request
     * @return IPage
     */
    ResponseResult findRoles(RoleMongo role, QueryRequest request);

    /**
     * 通过角色名称查找相应角色
     *
     * @param roleName 角色名称
     * @return 角色
     */
    RoleMongo findByName(String roleName);

    /**
     * 新增角色
     *
     * @param role 待新增的角色
     */
    void createRole(RoleMongo role);

    /**
     * 修改角色
     *
     * @param role 待修改的角色
     */
    void updateRole(RoleMongo role);


    /**
     * 删除角色
     *
     * @param roleId 待删除角色的 id
     */
    ResponseResult deleteRole(String roleId);
}
