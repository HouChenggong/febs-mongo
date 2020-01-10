package cn.net.health.xiyou.shiro.mongo.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
public class MenuMongo implements Serializable {


    private static final long serialVersionUID = 7186686912676916513L;

    // 菜单
    public static final String TYPE_MENU = "0";
    // 按钮
    public static final String TYPE_BUTTON = "1";

    public static final String TOP_NODE = "0";


    /**
     * 菜单/按钮ID
     */
    @Id
    private String menuId;

    /**
     * 上级菜单ID
     */
    private String parentId;

    /**
     * 菜单/按钮名称
     */
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    private String menuName;

    /**
     * 菜单URL
     */
    @Size(max = 50, message = "{noMoreThan}")
    private String url;

    /**
     * 权限标识
     */
    @Size(max = 50, message = "{noMoreThan}")
    private String perms;

    /**
     * 图标
     */
    @Size(max = 50, message = "{noMoreThan}")
    private String icon;

    /**
     * 类型 0菜单 1按钮
     */
    @NotBlank(message = "{required}")
    private String type;

    /**
     * 排序
     */
    private Long orderNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否是系统属性，如果是：不能被删除
     */
    private Boolean system;


}
