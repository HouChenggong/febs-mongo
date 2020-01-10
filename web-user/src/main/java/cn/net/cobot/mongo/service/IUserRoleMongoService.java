//package cn.net.cobot.mongo.service;
//
//
//import cn.net.cobot.mongo.entity.RoleMenuMongo;
//import cn.net.cobot.mongo.entity.UserRoleMongo;
//
//import java.util.List;
//
///**
// * @author MrBird
// */
//public interface IUserRoleMongoService {
//
//    /**
//     * 通过角色 id 删除
//     *
//     * @param roleId 角色 id
//     */
//    void deleteUserRolesByRoleId(String roleId);
//
//    /**
//     * 通过用户 id 删除
//     *
//     * @param userId 用户 id
//     */
//    void deleteUserRolesByUserId(String userId);
//
//
//    /**
//     * 插入数据
//     *
//     * @param userRoleMongo
//     * @return
//     */
//    void inserUserRole(UserRoleMongo userRoleMongo);
//
//
//    /**
//     * 保存多个
//     *
//     * @param userRoleMongos
//     * @return
//     */
//    int saveBatch(List<UserRoleMongo> userRoleMongos);
//
//
//    /**
//     * 根据用户id获取角色的id列表
//     *
//     * @param userId
//     * @return
//     */
//    List<UserRoleMongo> findRoleIdListByUserId(Long userId);
//}
