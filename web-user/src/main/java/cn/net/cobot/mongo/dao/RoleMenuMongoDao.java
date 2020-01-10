package cn.net.cobot.mongo.dao;

import cn.net.cobot.mongo.common.BaseMongoDbDao;
import cn.net.cobot.mongo.entity.RoleMenuMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class RoleMenuMongoDao extends BaseMongoDbDao<RoleMenuMongo> {
    @Override
    protected Class<RoleMenuMongo> getEntityClass() {
        return RoleMenuMongo.class;
    }
}
