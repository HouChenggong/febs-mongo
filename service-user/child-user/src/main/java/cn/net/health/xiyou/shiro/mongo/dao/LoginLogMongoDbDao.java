package cn.net.health.xiyou.shiro.mongo.dao;

import cn.net.health.xiyou.shiro.mongo.common.BaseMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.LoginLogMongo;
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
