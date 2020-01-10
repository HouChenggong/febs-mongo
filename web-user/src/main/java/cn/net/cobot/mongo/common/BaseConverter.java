package cn.net.cobot.mongo.common;

import java.util.function.Function;

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/12/20 11:47
 */
public abstract class BaseConverter<A, B> implements Function<A, B> {
    protected abstract B doForward(A a);
    protected abstract A doBackward(B b);
    //其他略
}
