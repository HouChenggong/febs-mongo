package cn.net.cobot.mongo.dao;

import cn.net.cobot.mongo.common.BaseMongoDbDao;
import cn.net.cobot.mongo.entity.ExpireData;
import cn.net.cobot.mongo.entity.UserMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class ExpireMongoDao extends BaseMongoDbDao<ExpireData> {
    @Override
    protected Class<ExpireData> getEntityClass() {
        return ExpireData.class;
    }
}
