package cn.net.health.xiyou.shiro.mongo.service.impl;

import cn.net.health.xiyou.shiro.common.utils.SnowFlake;
import cn.net.health.xiyou.shiro.mongo.dao.RoleMenuMongoDao;
import cn.net.health.xiyou.shiro.mongo.entity.RoleMenuMongo;
import cn.net.health.xiyou.shiro.mongo.service.IRoleMenuMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author MrBird
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleMenuMongoServiceImpl implements IRoleMenuMongoService {

    @Autowired
    private RoleMenuMongoDao roleMenuMongoDao;
    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Override
    @Transactional
    public void deleteRoleMenusByRoleId(String roleId) {
        RoleMenuMongo roleMenuMongo = new RoleMenuMongo();
        roleMenuMongo.setRoleId(roleId);
        roleMenuMongoDao.deleteMany(roleMenuMongo);
    }

    @Override
    @Transactional
    public void deleteRoleMenusByMenuId(String menuId) {
        RoleMenuMongo roleMenuMongo = new RoleMenuMongo();
        roleMenuMongo.setMenuId(menuId);
        roleMenuMongoDao.deleteMany(roleMenuMongo);
    }

    @Override
    @Transactional
    public int saveBatch(List<RoleMenuMongo> roleMenuMongos) {
        return roleMenuMongoDao.insertMany(roleMenuMongos);
    }

    @Override
    public List<RoleMenuMongo> selectMenuIdsByRoleIds(List<String> roleIds) {
        return roleMenuMongoDao.queryListColumnIn(roleIds, "roleId");
    }

    @Override
    public List<RoleMenuMongo> selectMenuIdsByMenuId(String menuId) {
        RoleMenuMongo roleMenuMongo = new RoleMenuMongo();
        roleMenuMongo.setMenuId(menuId);
        return roleMenuMongoDao.queryListByEntity(roleMenuMongo);
    }

}
