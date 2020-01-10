package cn.net.health.xiyou.shiro.mongo.service;

import cn.net.health.xiyou.shiro.mongo.entity.DeptMongo;
import cn.net.health.xiyou.shiro.mongo.entity.DeptTreeMongo;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.common.entity.QueryRequest;

import java.util.List;

/**
 * @author MrBird
 */
public interface IDeptMongoService {

    /**
     * 获取部门树（下拉选使用）
     *
     * @return 部门树集合
     */
    List<DeptTreeMongo<DeptMongo>> findDepts();

    /**
     * 获取部门列表（树形列表）
     *
     * @param dept 部门对象（传递查询参数）
     * @return 部门树
     */
    List<DeptTreeMongo<DeptMongo>> findDepts(DeptMongo dept);

    /**
     * 获取部门树（供Excel导出）
     *
     * @param dept    部门对象（传递查询参数）
     * @param request QueryRequest
     * @return List<Dept>
     */
    List<DeptMongo> findDepts(DeptMongo dept, QueryRequest request);

    /**
     * 新增部门
     *
     * @param dept 部门对象
     */
    ResponseResult createDept(DeptMongo dept);

    /**
     * 修改部门
     *
     * @param dept 部门对象
     */
    void updateDept(DeptMongo dept);

    /**
     * 删除部门
     *
     * @param deptId 部门 ID集合
     */
    ResponseResult deleteDepts(String deptId);
}
