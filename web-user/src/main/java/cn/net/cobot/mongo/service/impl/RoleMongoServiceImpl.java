package cn.net.cobot.mongo.service.impl;

import cn.net.cobot.common.authentication.ShiroRealm;
import cn.net.cobot.common.entity.QueryRequest;
import cn.net.cobot.common.utils.FebsUtil;
import cn.net.cobot.common.utils.SnowFlake;
import cn.net.cobot.mongo.dao.RoleMongoDao;
import cn.net.cobot.mongo.entity.ResponseResult;
import cn.net.cobot.mongo.entity.RoleMenuMongo;
import cn.net.cobot.mongo.entity.RoleMongo;

import cn.net.cobot.mongo.entity.UserMongo;
import cn.net.cobot.mongo.service.IRoleMenuMongoService;
import cn.net.cobot.mongo.service.IRoleMongoService;
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
public class RoleMongoServiceImpl implements IRoleMongoService {

    @Autowired
    private IRoleMenuMongoService roleMenuMongoService;

    @Autowired
    private ShiroRealm shiroRealm;

    @Autowired
    private RoleMongoDao roleMongoDao;


    @Override
    public List<RoleMongo> findUserRole(String username) {
        UserMongo userMongo = FebsUtil.getCurrentUserMongo();
        List<String> roleIds = new ArrayList<>(8);
        if (userMongo != null && userMongo.getRoleMap() != null && userMongo.getRoleMap().size() > 0) {
            userMongo.getRoleMap().forEach(one -> {
                roleIds.add(one.getRoleId());
            });
        }
        List<RoleMongo> roleMongoList = roleMongoDao.queryListColumnIn(roleIds, "_id");
        return roleMongoList;

    }

    @Override
    public List<RoleMongo> findRoles(RoleMongo roleMongo) {
        return roleMongoDao.queryAll(roleMongo);
    }

    @Override
    public ResponseResult findRoles(RoleMongo roleMongo, QueryRequest request) {
        String likeValue = "";
        if (StringUtils.isNotBlank(roleMongo.getRoleName())) {
            likeValue = roleMongo.getRoleName();
            roleMongo.setRoleName(null);
        }
        Sort sort = Sort.by(
                Sort.Order.desc("createTime"));
        return this.roleMongoDao.queryPageByEntity(roleMongo
                , (request.getPageNum() - 1) * request.getPageSize()
                , request.getPageSize(),
                "roleName", likeValue, sort);
    }

    @Override
    public RoleMongo findByName(String roleMongoName) {
        return new RoleMongo();
    }

    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Override
    @Transactional
    public void createRole(RoleMongo roleMongo) {
        roleMongo.setRoleId(String.valueOf(snowFlake.makeNextId()));
        roleMongo.setCreateTime(new Date());
        if (roleMongo.getSystem() == null) {
            //默认不是系统属性
            roleMongo.setSystem(false);
        }
        this.roleMongoDao.insertOne(roleMongo);
        this.saveRoleMenus(roleMongo);
    }

    @Override
    @Transactional
    public void updateRole(RoleMongo roleMongo) {
        RoleMongo old = roleMongoDao.queryById(roleMongo.getRoleId());
        roleMongo.setModifyTime(new Date());
        this.roleMongoDao.updateFirst(old, roleMongo);
        this.roleMenuMongoService.deleteRoleMenusByRoleId(roleMongo.getRoleId());
        saveRoleMenus(roleMongo);
        shiroRealm.clearCache();
    }

    @Override
    @Transactional
    public ResponseResult deleteRole(String roleId) {
        RoleMongo roleMongo = roleMongoDao.queryById(roleId);
        if (roleMongo.getSystem() == null || roleMongo.getSystem()) {
            return new ResponseResult(false, "系统属性不能删除");
        }
        //删除角色
        this.roleMongoDao.deleteById(roleId);
        //删除角色权限关联表
        this.roleMenuMongoService.deleteRoleMenusByRoleId(roleId);
        //但是该拥有该角色的用户，我们不立刻删除，而是当用户查询的时候才进行删除，不然操作的工作有点大
        return new ResponseResult(true);
    }

    private void saveRoleMenus(RoleMongo roleMongo) {
        if (StringUtils.isNotBlank(roleMongo.getMenuIds())) {
            String[] menuIds = roleMongo.getMenuIds().split(",");
            List<RoleMenuMongo> roleMenus = new ArrayList<>();
            Arrays.stream(menuIds).forEach(menuId -> {
                RoleMenuMongo roleMenu = new RoleMenuMongo();
                roleMenu.setId(String.valueOf(snowFlake.makeNextId()));
                roleMenu.setMenuId((menuId));
                roleMenu.setRoleId(roleMongo.getRoleId());
                roleMenus.add(roleMenu);
            });
            roleMenuMongoService.saveBatch(roleMenus);
        }
    }
}
