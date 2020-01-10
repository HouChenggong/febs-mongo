package cn.net.cobot.monitor.controller;

import cn.net.cobot.common.annotation.ControllerEndpoint;
import cn.net.cobot.common.controller.BaseController;
import cn.net.cobot.common.entity.FebsResponse;
import cn.net.cobot.common.entity.QueryRequest;
import cn.net.cobot.mongo.entity.LoginLogMongo;
import cn.net.cobot.monitor.service.ILoginLogService;
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
    @ControllerEndpoint(exceptionMessage = "删除日志失败")
    public FebsResponse deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] loginLogIds = ids.split(",");
        this.loginLogService.deleteLoginLogs(loginLogIds);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("loginlog:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void export(QueryRequest request, LoginLogMongo loginLog, HttpServletResponse response) {
        List<LoginLogMongo> loginLogs = this.loginLogService.findLoginLogs(loginLog, request);
        ExcelKit.$Export(LoginLogMongo.class, response).downXlsx(loginLogs, false);
    }
}
