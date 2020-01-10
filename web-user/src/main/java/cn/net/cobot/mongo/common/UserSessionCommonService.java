package cn.net.cobot.mongo.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
