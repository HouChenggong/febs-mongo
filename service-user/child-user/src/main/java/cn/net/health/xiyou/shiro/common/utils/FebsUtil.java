package cn.net.health.xiyou.shiro.common.utils;


import cn.net.health.xiyou.shiro.mongo.entity.UserMongo;
import cn.net.health.xiyou.shiro.common.entity.FebsConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * FEBS工具类
 *
 * @author MrBird
 */
@Slf4j
public class FebsUtil {


    private static final Pattern PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 驼峰转下划线
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String camelToUnderscore(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
        if (arr.length == 0) {
            return value;
        }
        StringBuilder result = new StringBuilder();
        IntStream.range(0, arr.length).forEach(i -> {
            if (i != arr.length - 1) {
                result.append(arr[i]).append("_");
            } else {
                result.append(arr[i]);
            }
        });
        return StringUtils.lowerCase(result.toString());
    }

    /**
     * 下划线转驼峰
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String underscoreToCamel(String value) {
        StringBuilder result = new StringBuilder();
        String[] arr = value.split("_");
        for (String s : arr) {
            result.append((String.valueOf(s.charAt(0))).toUpperCase()).append(s.substring(1));
        }
        return result.toString();
    }

    /**
     * 判断是否为 ajax请求
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

    /**
     * 正则校验
     *
     * @param regex 正则表达式字符串
     * @param value 要匹配的字符串
     * @return 正则校验结果
     */
    public static boolean match(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * 获取当前登录用户
     *
     * @return User
     */
    public static UserMongo getCurrentUser() {
        return (UserMongo) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取当前登录用户
     *
     * @return User
     */
    public static UserMongo getCurrentUserMongo() {
        return (UserMongo) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 判断是否包含中文
     *
     * @param value 内容
     * @return 结果
     */
    public static boolean containChinese(String value) {
        Matcher m = PATTERN.matcher(value);
        return m.find();
    }

    public static String view(String viewName) {
        return FebsConstant.VIEW_PREFIX + viewName;
    }
}
