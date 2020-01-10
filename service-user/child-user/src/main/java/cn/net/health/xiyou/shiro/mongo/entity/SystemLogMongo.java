package cn.net.health.xiyou.shiro.mongo.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
public class SystemLogMongo implements Serializable {


    private static final long serialVersionUID = 2265727535883679383L;
    /**
     * 日志ID
     */
    @Id
    private String id;

    /**
     * 操作用户
     */
    //@Field(name = "USERNAME")
    ////@ExcelField(value = "操作用户")
    private String username;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 操作内容
     */
    //@Field(name = "OPERATION")
    //@ExcelField(value = "操作内容")
    private String operation;

    /**
     * 耗时
     */
    //@Field(name = "TIME")
    //@ExcelField(value = "耗时（毫秒）")
    private Long time;

    /**
     * 操作方法
     */
    //@Field(name = "METHOD")
    //@ExcelField(value = "操作方法")
    private String method;

    /**
     * 方法参数
     */
    //@Field(name = "PARAMS")
    //@ExcelField(value = "方法参数")
    private String params;

    /**
     * 操作者IP
     */
    //@Field(name = "IP")
    //@ExcelField(value = "操作者IP")
    private String ip;

    /**
     * 创建时间
     */
    //@Field(name = "CREATE_TIME")
    //@ExcelField(value = "操作时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 操作地点
     */
    //@Field(name = "LOCATION")
    //@ExcelField(value = "操作地点")
    private String location;

    private transient String createTimeFrom;
    private transient String createTimeTo;
}
