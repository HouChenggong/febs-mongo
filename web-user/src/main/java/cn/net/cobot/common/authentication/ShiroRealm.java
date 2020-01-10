package cn.net.cobot.common.authentication;

import cn.net.cobot.mongo.entity.MenuMongo;
import cn.net.cobot.mongo.entity.RoleMongo;
import cn.net.cobot.mongo.entity.UserMongo;
import cn.net.cobot.mongo.service.IMenuMongoService;
import cn.net.cobot.mongo.service.IRoleMongoService;
import cn.net.cobot.mongo.service.IUserMongoService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author MrBird
 */
@Component
public class ShiroRealm extends AuthorizingRealm {


    @Autowired
    private IUserMongoService userMongoService;
    @Autowired
    private IRoleMongoService roleService;
    @Autowired
    private IMenuMongoService menuService;

    /**
     * 授权模块，获取用户角色和权限
     *
     * @param principal principal
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        UserMongo user = (UserMongo) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getUsername();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        List<RoleMongo> roleList = this.roleService.findUserRole(userName);
        if(roleList!=null && roleList.size()>0){
            Set<String> roleSet = roleList.stream().map(RoleMongo::getRoleName).collect(Collectors.toSet());
            simpleAuthorizationInfo.setRoles(roleSet);
        }


        // 获取用户权限集
        List<MenuMongo> permissionList = this.menuService.findUserPermissions(userName);
        if(permissionList!=null && permissionList.size()>0){
            Set<String> permissionSet = permissionList.stream().map(MenuMongo::getPerms).collect(Collectors.toSet());
            permissionSet.remove("");
            simpleAuthorizationInfo.setStringPermissions(permissionSet);
        }

        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param token AuthenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        // 通过用户名到数据库查询用户信息
        UserMongo user = this.userMongoService.findByName(userName);

        if (user == null) {
            throw new UnknownAccountException("账号未注册！");
        }
        if (!StringUtils.equals(password, user.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        if (UserMongo.STATUS_LOCK.equals(user.getStatus())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
