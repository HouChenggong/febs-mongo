package cn.net.cobot.mongo.dao;

import cn.net.cobot.mongo.common.BaseMongoDbDao;
import cn.net.cobot.mongo.entity.LoginLogMongo;
import cn.net.cobot.mongo.entity.UserMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class UserMongoDbDao extends BaseMongoDbDao<UserMongo> {
    @Override
    protected Class<UserMongo> getEntityClass() {
        return UserMongo.class;
    }
}
