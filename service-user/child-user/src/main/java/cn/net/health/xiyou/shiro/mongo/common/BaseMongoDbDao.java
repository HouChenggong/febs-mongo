package cn.net.health.xiyou.shiro.mongo.common;

import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashMultimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: huangyibo
 * @Date: 2019/1/31 0:08
 * @Description: mongoDB基础方法封装
 * https://docs.spring.io/spring-data/mongodb/docs/2.2.3.RELEASE/reference/html/#mongo-template.save-insert
 * 1. 查询相关
 * find()是根据条件查询，可以是原生的JSON条件，也可以是符合条件组成的对象
 * findOne() 返回单个对象
 * findById()根据ID查询
 * findAll()直接返回数据库中所有的数据
 * findAndRemove()返回匹配到的第一个，并且删除
 * findAllAndRemove() 查询所有复合的记录，并且一个一个的删除，不是批量删除
 * <p>
 * 2.  update相关
 * save()方法，有的话，覆盖可能具有相同对象的任何对象id，没有新建并保存
 * updateFirst() 修改匹配到的第一个数据
 * updateMulti（）修改匹配到的所有数据
 * <p>
 * upsert()  修改时如果不存在则进行添加操作,针对一条记录
 * <p>
 * <p>
 * <p>
 * <p>
 * 5.大小写相关，10.7.5上面
 * https://docs.spring.io/spring-data/mongodb/docs/2.2.3.RELEASE/reference/html/#mongo.query.additional-query-options
 */
@Slf4j
public abstract class BaseMongoDbDao<T> {


    /**
     * 反射获取泛型类型
     *
     * @return
     */
    protected abstract Class<T> getEntityClass();

    @Autowired
    private MongoTemplate mongoTemplate;


    /***
     * A:findOne
     * 根据id从几何中查询对象
     * @param id
     * @return
     */
    public T queryById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        log.info("-------------->MongoDB queryByStringId ...[{}] ", id);
        return this.mongoTemplate.findOne(query, this.getEntityClass());
    }

    /***
     * 根据id从几何中查询对象
     * @param id
     * @return
     */
    public T queryByLongId(Long id) {
        Query query = new Query(Criteria.where("_id").is(id));
        log.info("-------------->MongoDB queryByLongId ...[{}] ", id);
        return this.mongoTemplate.findOne(query, this.getEntityClass());
    }


    /**
     * 根据条件查询只返回一个文档
     * 只要对象里面存的是你的条件即可，比如 where name ="XXX" 那你的对象只能有一个name不能再有其它的了
     *
     * @param t
     * @return
     */
    public T queryOneByEntity(T t) {
        Query query = getQueryByObject(t);
        log.info("-------------->MongoDB queryOneByEntity ...[{}]", getConditions(t));
        return mongoTemplate.findOne(query, this.getEntityClass());
    }


    /**
     * 根据条件查询集合
     *
     * @param t
     * @return
     */
    public List<T> queryListByEntity(T t) {
        Query query = getQueryByObject(t);
        log.info("-------------->MongoDB queryListByEntity ...[{}]", getConditions(t));
        return mongoTemplate.find(query, this.getEntityClass());
    }

    /**
     * 查询全部
     *
     * @param t
     * @return
     */
    public List<T> queryAll(T t) {
        Query query = getQueryByObject(t);
        log.info("-------------->MongoDB queryAll ...[{}]", getConditions(t));
        return mongoTemplate.findAll(this.getEntityClass());
    }


    /**
     * 根据条件查询集合
     *
     * @param lists
     * @param column 默认是ID
     * @return
     */
    public List<T> queryListByIdIn(List<Long> lists, String column) {
        if (StringUtils.isBlank(column)) {
            column = "_id";
        }
        Query query = Query.query(Criteria.where(column).in(lists));
        log.info("-------------->MongoDB queryListByIdIn ...[{}]", column);
        return mongoTemplate.find(query, this.getEntityClass());
    }

    /**
     * 根据条件查询集合
     *
     * @param lists
     * @param column 默认是ID
     * @return
     */
    public List<T> queryListColumnIn(List<String> lists, String column) {
        if (StringUtils.isBlank(column)) {
            column = "_id";
        }
        Query query = Query.query(Criteria.where(column).in(lists));
        log.info("-------------->MongoDB queryListColumnIn ...column is ：[{}] and list.size is: [{}]", column, lists.size());
        return mongoTemplate.find(query, this.getEntityClass());
    }

    /**
     * 原生的JSON查询
     *
     * @param jsonStr
     * @return
     */
    public List<T> selectListByJson(String jsonStr) {
        BasicQuery query = new BasicQuery(jsonStr);
        log.info("-------------->MongoDB selectListByJson ...[{}]", jsonStr);
        return mongoTemplate.find(query, this.getEntityClass());
    }


    /***
     * 根据条件分页查询
     * @param t
     * @param start 查询起始值
     * @param size  查询大小
     * @param likeColumn  模糊查询的字段
     * @param likeValue  模糊查询的值
     * @return
     */
    public ResponseResult queryPageByEntity(T t, int start, int size, String likeColumn, String likeValue, Sort sort) {
        ResponseResult result = new ResponseResult();
        Query query = getQueryByObject(t);
        if (StringUtils.isNotBlank(likeValue)) {
            Pattern pattern = Pattern.compile("^.*" + likeValue + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where(likeColumn).regex(pattern));
        }
        log.info("-------------->MongoDB queryPageByEntity ...detail is :[{}]   page is: [{}] size is [{}]", getConditions(t), start, size);
        long total = this.mongoTemplate.count(query, this.getEntityClass());
        query.skip(start);
        query.limit(size);
        result.setMsg(String.valueOf(total));
        query.with(sort);
        result.setData(this.mongoTemplate.find(query, this.getEntityClass()));
        return result;
    }

    /***
     * 根据条件分页查询
     * @param t
     * @param start 查询起始值
     * @param size  查询大小
     * @param columnList  查询大小
     * @param column  哪一列
     * @return
     */
    public ResponseResult queryPageByEntityAndColumnOrIn(T t, int start, int size, List<String> columnList, String column, Sort sort) {
        ResponseResult result = new ResponseResult();
        Query query = new Query();
        Criteria[] list = new Criteria[columnList.size()];
        for (int i = 0, len = columnList.size(); i < len; i++) {
            Criteria one = Criteria.where(column).is(columnList.get(i));
            list[i] = one;
        }
        Criteria criteria = new Criteria().orOperator(list);

        Criteria obj = getCriteriaByObject(t);
        if (obj != null) {
            query.addCriteria(new Criteria().andOperator(obj, criteria));
        } else {
            query.addCriteria(criteria);
        }
        log.info("-------------->MongoDB queryPageByEntity ...detail is :[{}]   page is: [{}] size is [{}]", getConditions(t), start, size);
        long total = this.mongoTemplate.count(query, this.getEntityClass());
        result.setMsg(String.valueOf(total));
        query.skip(start);
        query.limit(size);
        result.setData(this.mongoTemplate.find(query, this.getEntityClass()));
        return result;
    }

    /***
     * 根据条件分页查询
     * @param t
     * @param start 查询起始值
     * @param size  查询大小
     * @param columnList  查询大小
     * @param column  哪一列
     * @return
     */
    public ResponseResult queryPageByEntityAndColumnIn(T t, int start, int size, List<String> columnList,
                                                       String column, String likeColumn, String likeValue, Sort sort) {
        ResponseResult result = new ResponseResult();
        Query query = new Query();
        Criteria criteria = (Criteria.where(column).in(columnList));
        if (StringUtils.isNotBlank(likeValue)) {
            Pattern pattern = Pattern.compile("^.*" + likeValue + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where(likeColumn).regex(pattern));
        }
        Criteria obj = getCriteriaByObject(t);
        if (obj != null) {
            query.addCriteria(new Criteria().andOperator(obj, criteria));
        } else {
            query.addCriteria(criteria);
        }
        log.info("-------------->MongoDB queryPageByEntity ...detail is :[{}]   page is: [{}] size is [{}]", getConditions(t), start, size);
        long total = this.mongoTemplate.count(query, this.getEntityClass());
        result.setMsg(String.valueOf(total));
        query.skip(start);
        query.limit(size);
        result.setData(this.mongoTemplate.find(query, this.getEntityClass()));
        return result;
    }

    /***
     * 根据条件查询库中符合条件的记录数量
     * @param t
     * @return
     */
    public Long getCount(T t) {
        Query query = getQueryByObject(t);
        log.info("-------------->MongoDB getCount ...[{}]", getConditions(t));
        return this.mongoTemplate.count(query, this.getEntityClass());
    }

    /***
     * 今天的数据
     * @param t
     * @return
     */
    public List<T> getTodayData(String dateColumn, T t) {
        /**
         * > 大于 $gt
         * < 小于 $lt
         * >= 大于等于 $gte
         * <= 小于等于 $lte
         */
        Query query = new Query(Criteria.where(dateColumn)
                .lte(DateTimeStampUtils.nextDayZero())
                .gte(DateTimeStampUtils.todayZero()));
        log.info("-------------->MongoDB getTodayDate ...[{}]", getConditions(t));
        return this.mongoTemplate.find(query, this.getEntityClass());
    }


    /***
     * 最近10天的数据
     * @param t
     * @return
     */
    public List<T> getNearlyTenData(String dateColumn, T t) {
        Query query = new Query(Criteria.where(dateColumn)
                .lte(DateTimeStampUtils.nextTenDaysZero())
                .gte(DateTimeStampUtils.todayZero()));

        log.info("-------------->MongoDB getNearlyTenData ...[{}]", getConditions(t));
        return this.mongoTemplate.find(query, this.getEntityClass());
    }

    /***
     * 保存，覆盖可能具有相同对象的任何对象id。
     * @param t
     */
    public void insertOrCover(T t) {
        log.info("-------------->MongoDB insertOrCover ...[{}]", getConditions(t));
        this.mongoTemplate.save(t);
    }


    /***
     * 保存一个对象, 如果存在具有相同文档的现有文档，id则会生成错误。
     * @param t
     */
    public void insertOne(T t) {
        log.info("-------------->MongoDB insertOne ...[{}]", getConditions(t));
        this.mongoTemplate.insert(t);
    }

    /***
     * 保存一个对象
     * @param list
     */
    public int insertMany(List<T> list) {
        log.info("-------------->MongoDB insertMany ...[{}]", list.size());
        return this.mongoTemplate.insertAll(list).size();
    }


    /**
     * 根据条件查询集合删除，默认是ID
     *
     * @param lists
     * @param column 默认是ID
     * @return
     */
    public long deleteManyByColumnIn(List<String> lists, String column) {
        if (StringUtils.isBlank(column)) {
            column = "_id";
        }
        Query query = Query.query(Criteria.where(column).in(lists));
        long num = mongoTemplate.remove(query, this.getEntityClass()).getDeletedCount();
        log.info("-------------->MongoDB deleteManyByColumnIn ...条件是：[{}] 删除了：[{}]个", column, num);
        return num;
    }


    /***
     * 删除对象,当前对象必须要有ID
     * @param t
     * @return
     */
    public int deleteOneMustHaveId(T t) {
        log.info("-------------->MongoDB deleteOneMustHaveId ...[{}]", getConditions(t));
        return (int) this.mongoTemplate.remove(t).getDeletedCount();
    }

    /***
     * 删除对象,当前对象里面有什么，就可以根据where 条件删除
     * 但是是一个一个的删除，不是批量删除
     * https://docs.spring.io/spring-data/mongodb/docs/2.2.3.RELEASE/reference/html/#mongo-template.save-update-remove
     *
     * @param t
     * @return
     */
    public int deleteMany(T t) {
        Query query = getQueryByObject(t);
        int count = this.mongoTemplate.findAllAndRemove(query, this.getEntityClass()).size();
        log.info("-------------->MongoDB deleteMany but it is one by one ...记录数:[{}]条,条件是:[{}]", getConditions(t));
        return count;
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void deleteById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        if (null != criteria) {
            Query query = new Query(criteria);
            T obj = this.mongoTemplate.findOne(query, this.getEntityClass());
            log.info("-------------->MongoDB deleteById ...[{}]", id);
            if (obj != null) {
                this.deleteOneMustHaveId(obj);
            }
        }
    }

    /**
     * 根据字段删除
     *
     * @param column
     * @param columnValue
     */
    public void deleteByColumn(String column, String columnValue) {
        Criteria criteria = Criteria.where(column).is(columnValue);
        if (null != criteria) {
            Query query = new Query(criteria);
            T obj = this.mongoTemplate.findOne(query, this.getEntityClass());
            log.info("-------------->MongoDB deleteByColumn ...column is [{}] and columnValue is [{}]", column, columnValue);
            if (obj != null) {
                this.deleteOneMustHaveId(obj);
            }
        }
    }

    /*MongoDB中更新操作分为三种
     * 1：updateFirst     修改第一条
     * 2：updateMulti     修改所有匹配的记录
     * 3：upsert  修改时如果不存在则进行添加操作
     * */

    /**
     * 修改匹配到的第一条记录
     *
     * @param srcObj
     * @param targetObj
     */
    public void updateFirst(T srcObj, T targetObj) {
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        long num = this.mongoTemplate.updateFirst(query, update, this.getEntityClass()).getModifiedCount();
        log.info("-------------->MongoDB updateFirst ...更新数量是：[{}] ，条件是:[{}]", num, getUpdateConditions(srcObj, targetObj));

    }


    /**
     * 修改匹配到的第一条记录
     *
     * @param t         id
     * @param targetObj
     */
    public void updateFirstById(String t, T targetObj) {
        Query query = Query.query(Criteria.where("_id").is(t));
        Update update = getUpdateByObject(targetObj);
        long num = this.mongoTemplate.updateFirst(query, update, this.getEntityClass()).getModifiedCount();
        log.info("-------------->MongoDB updateFirstById ...更新数量是：[{}] ，更新后的结果是:[{}]", num, getConditions(targetObj));
    }

    /***
     * 修改匹配到的所有记录
     * @param srcObj
     * @param targetObj
     */
    public void updateMulti(T srcObj, T targetObj) {
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        log.info("-------------->MongoDB updateMulti ...更新的条件是：[{}]", getUpdateConditions(srcObj, targetObj));
        this.mongoTemplate.updateMulti(query, update, this.getEntityClass());
    }

    /***
     * 修改匹配到的记录，若不存在该记录则进行添加
     * @param srcObj
     * @param targetObj
     */
    public void updateInsert(T srcObj, T targetObj) {
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        log.info("-------------->MongoDB updateInsert ...[{}]", getUpdateConditions(srcObj, targetObj));
        this.mongoTemplate.upsert(query, update, this.getEntityClass());
    }

    /**
     * 将查询条件对象转换为query
     *
     * @param object
     * @return
     * @author Jason
     */
    private Query getQueryByObject(T object) {
        Query query = new Query();
        String[] fileds = getFiledName(object);
        Criteria criteria = new Criteria();
        for (int i = 0; i < fileds.length; i++) {
            String filedName = (String) fileds[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if (filedValue != null && StringUtils.isNotBlank(filedValue.toString())) {
                criteria.and(filedName).is(filedValue);
            }
        }
        query.addCriteria(criteria);
        return query;
    }

    /**
     * 将查询条件对象转换为query
     *
     * @param object
     * @return
     * @author Jason
     */
    private Criteria getCriteriaByObject(T object) {
        String[] fileds = getFiledName(object);
        Criteria criteria = new Criteria();
        int totol = 0;
        for (int i = 0; i < fileds.length; i++) {
            String filedName = (String) fileds[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if (filedValue != null && StringUtils.isNotBlank(filedValue.toString())) {
                criteria.and(filedName).is(filedValue);
                totol++;
            }
        }
        if (totol == 0) {
            return null;
        }
        return criteria;
    }

    /**
     * 根据传入的对象获取不为空的信息
     *
     * @param object
     * @return
     */
    private String getConditions(T object) {
        String[] fileds = getFiledName(object);
        JSONObject jsonObject = new JSONObject();
        int num = 0;
        for (int i = 0; i < fileds.length; i++) {
            String filedName = (String) fileds[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if (filedValue != null && StringUtils.isNotBlank(filedValue.toString())) {
                jsonObject.put(filedName, filedValue);
                num++;
            }
        }
        return "操作条件有" + num + "它们是：" + jsonObject.toString() + "..." + object.getClass();
    }

    /**
     * 将查询条件对象转换为update
     *
     * @param object
     * @return
     * @author Jason
     */
    private Update getUpdateByObject(T object) {
        Update update = new Update();
        String[] fileds = getFiledName(object);
        for (int i = 0; i < fileds.length; i++) {
            String filedName = (String) fileds[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if (filedValue != null && StringUtils.isNotBlank(filedValue.toString())) {
                update.set(filedName, filedValue);
            }
        }
        return update;
    }

    /***
     * 获取对象属性返回字符串数组
     * @param o
     * @return
     */
    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];

        for (int i = 0; i < fields.length; ++i) {
            fieldNames[i] = fields[i].getName();
        }

        return fieldNames;
    }


    /***
     * 根据属性获取对象属性值
     * @param fieldName
     * @param o
     * @return
     */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String e = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + e + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[0]);
            return method.invoke(o, new Object[0]);
        } catch (Exception var6) {
            return null;
        }
    }


    /**
     * 根据传入的对象获取不为空的信息
     *
     * @param object
     * @param target
     * @return
     */
    private String getUpdateConditions(T object, T target) {
        String[] fileds = getFiledName(object);
        String[] filedTarget = getFiledName(target);

        HashMap<String, Object> targetMap = new HashMap<>(16);
        HashMultimap<String, Object> result = HashMultimap.create();


        for (int i = 0; i < fileds.length; i++) {
            String filedName = (String) fileds[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if (filedValue != null && StringUtils.isNotBlank(filedValue.toString())) {
                targetMap.put(filedName, filedValue);
            }
        }
        for (int i = 0; i < filedTarget.length; i++) {
            String filedName = (String) filedTarget[i];
            Object filedValue = getFieldValueByName(filedName, target);
            if (filedValue != null && StringUtils.isNotBlank(filedValue.toString())) {
                if (targetMap.containsKey(filedName)) {

                    if (targetMap.get(filedName).equals(filedValue)) {
                    } else {
                        result.put(filedName, targetMap.get(filedName));
                        result.put(filedName, "修改前______修改后");
                        result.put(filedName, filedValue);
                    }

                } else {
                    result.put(filedName, filedValue);
                }

            }
        }

        return "操作条件有" + "它们分别是：" + result.toString() + "..." + object.getClass();
    }
}
