package cn.net.health.xiyou.controller.shiro;



import cn.net.health.xiyou.shiro.common.entity.FebsResponse;
import cn.net.health.xiyou.shiro.common.exception.FebsException;
import cn.net.health.xiyou.shiro.mongo.entity.DeptMongo;
import cn.net.health.xiyou.shiro.mongo.entity.DeptTreeMongo;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.mongo.service.IDeptMongoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("dept")
public class DeptController {

    @Autowired
    private IDeptMongoService deptService;

    @GetMapping("select/tree")

    public List<DeptTreeMongo<DeptMongo>> getDeptTree() throws FebsException {
        return this.deptService.findDepts();
    }

    @GetMapping("tree")

    public FebsResponse getDeptTree(DeptMongo dept) throws FebsException {
        List<DeptTreeMongo<DeptMongo>> depts = this.deptService.findDepts(dept);
        return new FebsResponse().success().data(depts);
    }

    @PostMapping
    @RequiresPermissions("dept:add")

    public FebsResponse addDept(@Valid DeptMongo dept) {
        ResponseResult result = this.deptService.createDept(dept);
        if (!result.isSuccess()) {
            return new FebsResponse().fail();
        }
        return new FebsResponse().success();
    }

    @GetMapping("delete/{deptIds}")
    @RequiresPermissions("dept:delete")

    public FebsResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws FebsException {
        ResponseResult result = this.deptService.deleteDepts(deptIds);
        if (result.isSuccess()) {
            return new FebsResponse().success();
        }
        return new FebsResponse().fail();
    }

    @PostMapping("update")
    @RequiresPermissions("dept:update")

    public FebsResponse updateDept(@Valid DeptMongo dept) throws FebsException {
        this.deptService.updateDept(dept);
        return new FebsResponse().success();
    }


}
