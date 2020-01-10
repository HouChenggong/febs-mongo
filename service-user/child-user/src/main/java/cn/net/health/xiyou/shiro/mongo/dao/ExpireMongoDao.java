package cn.net.health.xiyou.shiro.mongo.dao;

import cn.net.health.xiyou.shiro.mongo.common.BaseMongoDbDao;
import cn.net.health.xiyou.shiro.mongo.entity.ExpireData;
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
