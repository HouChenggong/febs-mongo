package cn.net.cobot.mongo.dao;

import cn.net.cobot.mongo.common.BaseMongoDbDao;
import cn.net.cobot.mongo.entity.MenuMongo;
import org.springframework.stereotype.Repository;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:12
 * @Description:
 */
@Repository
public class MenuMongoDao extends BaseMongoDbDao<MenuMongo> {
    @Override
    protected Class<MenuMongo> getEntityClass() {
        return MenuMongo.class;
    }
}
