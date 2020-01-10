package cn.net.health.xiyou.shiro.mongo.dao;

import cn.net.health.xiyou.shiro.mongo.common.BaseMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.SystemLogMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class SystemLogMongoDao extends BaseMongoDbDao<SystemLogMongo> {
    @Override
    protected Class<SystemLogMongo> getEntityClass() {
        return SystemLogMongo.class;
    }
}
