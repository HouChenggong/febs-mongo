package cn.net.health.xiyou.shiro.mongo.common;

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/12/17 14:16
 */
public class UserSessionCommonService {

    public static Boolean sessionIsTrue(long sessionUserId, long userId) {
        if (sessionUserId == userId) {
            return true;
        }
        throw new RuntimeException(Constants.USER_SESSION);
    }
}
