package cn.net.health.xiyou.controller.shiro;


import cn.net.health.xiyou.controller.base.BaseController;
import cn.net.health.xiyou.shiro.common.entity.FebsResponse;
import cn.net.health.xiyou.shiro.monitor.entity.ActiveUser;
import cn.net.health.xiyou.shiro.monitor.service.ISessionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("session")
public class SessionController extends BaseController {

    @Autowired
    private ISessionService sessionService;

    @GetMapping("list")
    @RequiresPermissions("online:view")
    public FebsResponse list(String username) {
        String userId = String.valueOf(super.getCurrentUserMongo().getUserId());
        List<ActiveUser> list = sessionService.list(username,userId);
        Map<String, Object> data = new HashMap<>(4);
        data.put("rows", list);
        data.put("total", CollectionUtils.size(list));
        return new FebsResponse().success().data(data);
    }

    @GetMapping("delete/{id}")
    @RequiresPermissions("user:kickout")
    public FebsResponse forceLogout(@PathVariable String id) {
        sessionService.forceLogout(id);
        return new FebsResponse().success();
    }
}
