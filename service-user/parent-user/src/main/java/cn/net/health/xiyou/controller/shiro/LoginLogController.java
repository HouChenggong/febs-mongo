package cn.net.health.xiyou.controller.shiro;


import cn.net.health.xiyou.controller.base.BaseController;
import cn.net.health.xiyou.shiro.common.entity.FebsResponse;
import cn.net.health.xiyou.shiro.common.entity.QueryRequest;
import cn.net.health.xiyou.shiro.mongo.entity.LoginLogMongo;
import cn.net.health.xiyou.shiro.monitor.service.ILoginLogService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("loginLog")
public class LoginLogController extends BaseController {

    @Autowired
    private ILoginLogService loginLogService;

    @GetMapping("list")
    @RequiresPermissions("loginlog:view")
    public FebsResponse loginLogList(LoginLogMongo loginLog, QueryRequest request) {
        List<LoginLogMongo> lists = (this.loginLogService.findLoginLogs(loginLog, request));
        Map<String, Object> rows = new HashMap<>(8);
        rows.put("rows", lists);
        rows.put("total", Long.valueOf("100"));

        return new FebsResponse().success().data(rows);
    }

    @GetMapping("delete/{ids}")
    @RequiresPermissions("loginlog:delete")
    public FebsResponse deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] loginLogIds = ids.split(",");
        this.loginLogService.deleteLoginLogs(loginLogIds);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("loginlog:export")
    public void export(QueryRequest request, LoginLogMongo loginLog, HttpServletResponse response) {
        List<LoginLogMongo> loginLogs = this.loginLogService.findLoginLogs(loginLog, request);
        ExcelKit.$Export(LoginLogMongo.class, response).downXlsx(loginLogs, false);
    }
}
