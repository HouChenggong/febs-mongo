package cn.net.cobot.mongo.service.impl;


import cn.net.cobot.common.entity.QueryRequest;
import cn.net.cobot.common.utils.SnowFlake;
import cn.net.cobot.mongo.common.TreeMongoUtil;
import cn.net.cobot.mongo.dao.DeptMongoDao;
import cn.net.cobot.mongo.entity.DeptMongo;
import cn.net.cobot.mongo.entity.DeptTreeMongo;
import cn.net.cobot.mongo.entity.ResponseResult;
import cn.net.cobot.mongo.service.IDeptMongoService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 * @Transactional(propagation=Propagation.SUPPORTS)
 * 如果其他bean调用这个方法, 在其他bean中声明事务, 那就用事务.如果其他bean没有声明事务, 那就不用事务.
 * 该属性用于设置当前事务是否为只读事务，设置为true表示只读，false则表示可读写，默认值为false。例如：@Transactional(readOnly=true)
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptMongoServiceImpl implements IDeptMongoService {
    private SnowFlake snowFlake = new SnowFlake(2, 3);
    @Autowired
    private DeptMongoDao deptMongoDao;


    @Override
    public List<DeptTreeMongo<DeptMongo>> findDepts() {
        List<DeptMongo> depts = deptMongoDao.queryAll(new DeptMongo());
        List<DeptTreeMongo<DeptMongo>> trees = this.convertDepts(depts);
        return TreeMongoUtil.buildDeptTree(trees);
    }

    @Override
    public List<DeptTreeMongo<DeptMongo>> findDepts(DeptMongo dept) {


        List<DeptMongo> depts = deptMongoDao.queryAll(new DeptMongo());
        List<DeptTreeMongo<DeptMongo>> trees = this.convertDepts(depts);
        return TreeMongoUtil.buildDeptTree(trees);
    }

    @Override
    public List<DeptMongo> findDepts(DeptMongo dept, QueryRequest request) {
        return null;
    }

    @Override
    @Transactional
    public ResponseResult createDept(DeptMongo dept) {
        dept.setDeptId(String.valueOf(snowFlake.makeNextId()));
        String parentId = dept.getParentId();
        if (StringUtils.isBlank(parentId)) {
            dept.setParentId("0");
            dept.setLevel(0);
        } else {
//            if (dept.getLevel() >= 2) {
//                return new ResponseResult(false, "部门的层级最好控制在3级！");
//            }
//            dept.setLevel(dept.getLevel() + 1);
        }

        dept.setCreateTime(new Date());
        this.deptMongoDao.insertOne(dept);
        return new ResponseResult(true);
    }

    @Override
    @Transactional
    public void updateDept(DeptMongo dept) {
        dept.setModifyTime(new Date());
        this.deptMongoDao.updateFirstById(dept.getDeptId(), dept);
    }

    @Override
    @Transactional
    public ResponseResult deleteDepts(String deptId) {
        DeptMongo deptMongo = new DeptMongo();
        deptMongo.setParentId(deptId);
        Long size = this.deptMongoDao.getCount(deptMongo);
        if (size > 0) {
            //如果当前部门下面还有子部门，不能进行删除当前部门
            return new ResponseResult(false, "当前部门下面还有子部门，不能进行删除当前部门");
        }
        this.delete(deptId);
        return new ResponseResult(true);
    }

    private List<DeptTreeMongo<DeptMongo>> convertDepts(List<DeptMongo> depts) {
        List<DeptTreeMongo<DeptMongo>> trees = new ArrayList<>();
        depts.forEach(dept -> {
            DeptTreeMongo<DeptMongo> tree = new DeptTreeMongo<>();
            tree.setId(String.valueOf(dept.getDeptId()));
            tree.setParentId(String.valueOf(dept.getParentId()));
            tree.setName(dept.getDeptName());
            tree.setData(dept);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(String deptId) {
        this.deptMongoDao.deleteById(deptId);
    }
}
