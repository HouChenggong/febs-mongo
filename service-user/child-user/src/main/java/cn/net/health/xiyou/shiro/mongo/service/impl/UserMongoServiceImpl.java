package cn.net.health.xiyou.shiro.mongo.service.impl;

import cn.net.health.xiyou.shiro.common.authentication.ShiroRealm;
import cn.net.health.xiyou.shiro.common.entity.QueryRequest;
import cn.net.health.xiyou.shiro.common.utils.FebsUtil;
import cn.net.health.xiyou.shiro.common.utils.MD5Util;
import cn.net.health.xiyou.shiro.common.utils.SnowFlake;
import cn.net.health.xiyou.shiro.mongo.dao.DeptMongoDao;
import cn.net.health.xiyou.shiro.mongo.dao.UserMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.DeptMongo;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.mongo.entity.RoleMongo;
import cn.net.health.xiyou.shiro.mongo.entity.UserMongo;
import cn.net.health.xiyou.shiro.mongo.service.IMenuMongoService;
import cn.net.health.xiyou.shiro.mongo.service.IUserMongoService;

import org.apache.commons.lang3.SerializationUtils;

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
public class UserMongoServiceImpl implements IUserMongoService {

    @Autowired
    private ShiroRealm shiroRealm;

    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Autowired
    private UserMongoDbDao userMongoDbDao;

    @Autowired
    private IMenuMongoService menuMongoService;

    @Autowired
    private DeptMongoDao deptMongoDao;

    @Override
//    @Cacheable(value = "userName",key = "#username")
    public UserMongo findByName(String username) {
        UserMongo userMongo = new UserMongo();
        userMongo.setUsername(username);
        UserMongo result = this.userMongoDbDao.queryOneByEntity(userMongo);
        return result;
    }

    @Override
    public ResponseResult findUserDetail(UserMongo user, QueryRequest request) {
        String userId = String.valueOf(user.getUserId());
        Boolean selectAll = menuMongoService.findByHrefAndUser(userId, "user:selectAll");

        String likeValue = "";
        if (StringUtils.isNotBlank(user.getUsername())) {
            likeValue = user.getUsername();
            user.setUsername(null);
        }
        Sort sort = Sort.by(
                Sort.Order.desc("createTime"));
        if (selectAll) {
            //查询所有用户
            user.setUserId(null);
            return this.userMongoDbDao.queryPageByEntity(user
                    , (request.getPageNum() - 1) * request.getPageSize()
                    , request.getPageSize(), "username", likeValue, sort);
        } else {

            //查询当前部门的人
            Boolean selectDeptAll = menuMongoService.findByHrefAndUser(userId, "user:selectDeptAll");
            if (selectDeptAll) {
                //第一层级
                List<String> listOne = new ArrayList<>(8);

                UserMongo nowSelect = FebsUtil.getCurrentUserMongo();
                nowSelect.getDeptMap().forEach(one -> {
                    listOne.add(one.getDeptId());


                });
                //第二层级
                List<DeptMongo> deptMongoList = deptMongoDao.queryListColumnIn(listOne, "parentId");
                List<String> twoLevle = new ArrayList<>(8);
                for (int i = 0, len = deptMongoList.size(); i < len; i++) {
                    twoLevle.add(deptMongoList.get(i).getDeptId());
                }
                //第3层级
                List<DeptMongo> threeDeptList = deptMongoDao.queryListColumnIn(twoLevle, "parentId");
                List<String> threeLevle = new ArrayList<>(8);
                for (int i = 0, len = threeDeptList.size(); i < len; i++) {
                    threeLevle.add(threeDeptList.get(i).getDeptId());
                }
                List<String> allLevel = new ArrayList<>(8);
                allLevel.addAll(listOne);
                allLevel.addAll(twoLevle);
                allLevel.addAll(threeLevle);
                user.setUserId(null);
                return this.userMongoDbDao.queryPageByEntityAndColumnIn(user
                        , (request.getPageNum() - 1) * request.getPageSize()
                        , request.getPageSize(), allLevel, "deptMap._id",
                        "username", likeValue,sort);
            } else {
                //只看自己
                UserMongo userMongo = userMongoDbDao.queryByLongId(Long.valueOf(userId));
                ResponseResult result = new ResponseResult();
                List<UserMongo> userMongos = new ArrayList<>(4);
                userMongos.add(userMongo);
                result.setData(userMongos);
                result.setMsg(String.valueOf(1));
                return result;
            }
        }


    }

    @Override
    public UserMongo findUserDetail(String username) {
        UserMongo userMongo = new UserMongo();
        userMongo.setUsername(username);
        return userMongoDbDao.queryOneByEntity(userMongo);
    }

    @Override
    @Transactional
    public void updateLoginTime(String userId) {
        UserMongo user = userMongoDbDao.queryByLongId(Long.valueOf(userId));
        UserMongo userUpdate = SerializationUtils.clone(user);
        userUpdate.setLastLoginTime(new Date());
        this.userMongoDbDao.updateFirst(user, userUpdate);
    }

    @Override
    @Transactional
    public void createUser(UserMongo user) {
        user.setUserId((snowFlake.makeNextId()));
        user.setCreateTime(new Date());
        user.setStatus(UserMongo.STATUS_VALID);
        user.setAvatar(UserMongo.DEFAULT_AVATAR);
        user.setTheme(UserMongo.THEME_BLACK);
        user.setIsTab(UserMongo.TAB_OPEN);
        //默认密码是用户名_123456
        user.setPassword(MD5Util.encrypt(user.getUsername(), user.getUsername() + UserMongo.DEFAULT_PASSWORD));

        // 保存用户角色
        String[] roles = user.getRoleId().split(",");

        Set<RoleMongo> roleMap = new HashSet<>(4);
        for (int i = 0; i < roles.length; i++) {
            RoleMongo roleMongo = new RoleMongo();
            roleMongo.setRoleId(roles[i]);
            roleMongo.setRoleName(roles[i]);
            roleMap.add(roleMongo);
        }
        user.setRoleMap(roleMap);
        this.userMongoDbDao.insertOne(user);
//        String[] roles = (String[]) user.getRoleMap().keySet().toArray();
//        setUserRoles(user, roles);
    }

    @Override
    @Transactional
    public void deleteUsers(String[] userIds) {

    }

    @Override
    @Transactional
    public void updateUser(UserMongo user) {
        UserMongo old = userMongoDbDao.queryByLongId(user.getUserId());
        String username = user.getUsername();
        // 更新用户
        user.setPassword(old.getPassword());
        user.setUsername(old.getUsername());
        user.setModifyTime(new Date());
        String[] roles = user.getRoleId().split(",");
        String[] depts = user.getDeptId().split(",");
        Set<RoleMongo> roleMap = new HashSet<>(4);
        for (int i = 0; i < roles.length; i++) {
            RoleMongo roleMongo = new RoleMongo();
            roleMongo.setRoleId(roles[i]);
            roleMongo.setRoleName(roles[i]);
            roleMap.add(roleMongo);
        }
        user.setRoleMap(roleMap);


        Set<DeptMongo> deptMongoSet = new HashSet<>(4);
        for (int i = 0; i < depts.length; i++) {
            DeptMongo deptMongo = new DeptMongo();
            deptMongo.setDeptId(depts[i]);
            deptMongo.setDeptName(depts[i]);
            deptMongoSet.add(deptMongo);
        }
        user.setDeptMap(deptMongoSet);
        userMongoDbDao.updateFirst(old, user);
//        setUserRoles(user, roles);

        UserMongo currentUser = FebsUtil.getCurrentUserMongo();
        if (StringUtils.equalsIgnoreCase(currentUser.getUsername(), username)) {
            shiroRealm.clearCache();
        }
    }

    @Override
    @Transactional
    public void resetPassword(String[] usernames) {
        Arrays.stream(usernames).forEach(username -> {
            UserMongo old = new UserMongo();
            old.setUsername(username);
            old = userMongoDbDao.queryOneByEntity(old);
            UserMongo user = new UserMongo();
            user.setPassword(MD5Util.encrypt(username, UserMongo.DEFAULT_PASSWORD));
            userMongoDbDao.updateFirst(old, user);
        });
    }


    @Override
    @Transactional
    public void updatePassword(String username, String password) {
        UserMongo old = new UserMongo();
        old.setUsername(username);
        UserMongo user = SerializationUtils.clone(old);
        user.setPassword(MD5Util.encrypt(username, password));
        user.setModifyTime(new Date());
        this.userMongoDbDao.updateFirst(old, user);
    }

    @Override
    public void updateAvatar(String username, String avatar) {

    }

    @Override
    public void updateTheme(String username, String theme, String isTab) {

    }

    @Override
    public void updateProfile(UserMongo user) {

    }

//    private void setUserRoles(UserMongo user, String[] roles) {
//        List<UserRoleMongo> userRoles = new ArrayList<>();
//        Arrays.stream(roles).forEach(roleId -> {
//            UserRoleMongo userRole = new UserRoleMongo();
//            userRole.setUserId(String.valueOf(user.getUserId()));
//            userRole.setRoleId((roleId));
//            userRoles.add(userRole);
//        });
//        userRoleMongoService.saveBatch(userRoles);
//    }
}
