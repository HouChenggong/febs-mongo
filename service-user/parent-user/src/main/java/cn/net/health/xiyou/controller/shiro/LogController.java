package cn.net.health.xiyou.controller.shiro;


import cn.net.health.xiyou.controller.base.BaseController;
import cn.net.health.xiyou.shiro.common.entity.FebsResponse;
import cn.net.health.xiyou.shiro.common.entity.QueryRequest;
import cn.net.health.xiyou.shiro.mongo.entity.ResponseResult;
import cn.net.health.xiyou.shiro.mongo.entity.SystemLogMongo;
import cn.net.health.xiyou.shiro.monitor.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("log")
public class LogController extends BaseController {

    @Autowired
    private ILogService logService;

    @GetMapping("list")
    @RequiresPermissions("log:view")
    public FebsResponse logList(SystemLogMongo log, QueryRequest request) {
        ResponseResult result = (this.logService.findLogs(log, request));
        Map<String, Object> rows = new HashMap<>(8);
        rows.put("rows", result.getData());
        rows.put("total", Long.valueOf(result.getMsg()));

        return new FebsResponse().success().data(rows);
    }

    @GetMapping("delete/{ids}")
    @RequiresPermissions("log:delete")
    public FebsResponse deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] logIds = ids.split(",");
        this.logService.deleteLogs(logIds);
        return new FebsResponse().success();
    }


}
