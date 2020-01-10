//package cn.net.cobot.mongo.service.impl;
//
//import SnowFlake;
//import cn.net.cobot.mongo.dao.UserRoleMongoDao;
//import RoleMongo;
//import cn.net.cobot.mongo.entity.UserRoleMongo;
//import cn.net.cobot.mongo.service.IUserRoleMongoService;
//import cn.net.cobot.system.entity.UserRole;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
///**
// * @author MrBird
// */
//@Service
//@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
//public class UserRoleMongoServiceImpl implements IUserRoleMongoService {
//
//    @Autowired
//    private UserRoleMongoDao userRoleMongoDao;
//
//    @Override
//    @Transactional
//    public void deleteUserRolesByRoleId(String roleId) {
//        UserRoleMongo roleMongo = new UserRoleMongo();
//        roleMongo.setRoleId(roleId);
//        this.userRoleMongoDao.deleteMany(roleMongo);
//    }
//
//    @Override
//    @Transactional
//    public void deleteUserRolesByUserId(String userId) {
//        UserRoleMongo roleMongo = new UserRoleMongo();
//        roleMongo.setUserId(userId);
//        this.userRoleMongoDao.deleteMany(roleMongo);
//    }
//
//    @Override
//    @Transactional
//    public void inserUserRole(UserRoleMongo userRole) {
//        this.userRoleMongoDao.save(userRole);
//    }
//
//    @Override
//    public int saveBatch(List<UserRoleMongo> userRoleMongos) {
//        return userRoleMongoDao.saveMany(userRoleMongos);
//    }
//
//    @Override
//    public List<UserRoleMongo> findRoleIdListByUserId(Long userId) {
//        UserRoleMongo userRoleMongo =new UserRoleMongo();
//        userRoleMongo.setUserId(String.valueOf(userId));
//        return userRoleMongoDao.queryList(userRoleMongo);
//    }
//}
