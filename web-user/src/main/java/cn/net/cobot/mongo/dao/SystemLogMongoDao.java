package cn.net.cobot.mongo.dao;

import cn.net.cobot.mongo.common.BaseMongoDbDao;
import cn.net.cobot.mongo.entity.SystemLogMongo;
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
