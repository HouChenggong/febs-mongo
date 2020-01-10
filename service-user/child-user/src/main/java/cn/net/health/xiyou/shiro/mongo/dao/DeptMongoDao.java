package cn.net.health.xiyou.shiro.mongo.dao;

import cn.net.health.xiyou.shiro.mongo.common.BaseMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.DeptMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class DeptMongoDao extends BaseMongoDbDao<DeptMongo> {
    @Override
    protected Class<DeptMongo> getEntityClass() {
        return DeptMongo.class;
    }
}
