package cn.net.health.xiyou.shiro.mongo.dao;

import cn.net.health.xiyou.shiro.mongo.common.BaseMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.RoleMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class RoleMongoDao extends BaseMongoDbDao<RoleMongo> {
    @Override
    protected Class<RoleMongo> getEntityClass() {
        return RoleMongo.class;
    }
}
