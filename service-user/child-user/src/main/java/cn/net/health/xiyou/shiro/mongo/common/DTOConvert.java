package cn.net.health.xiyou.shiro.mongo.common;

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/12/20 11:40
 */
public interface DTOConvert<S,T> {
    T convert(S s);
}
