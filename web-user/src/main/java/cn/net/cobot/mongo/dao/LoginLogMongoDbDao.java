package cn.net.cobot.mongo.dao;

import cn.net.cobot.mongo.common.BaseMongoDbDao;
import cn.net.cobot.mongo.entity.LoginLogMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class LoginLogMongoDbDao extends BaseMongoDbDao<LoginLogMongo> {
    @Override
    protected Class<LoginLogMongo> getEntityClass() {
        return LoginLogMongo.class;
    }
}
