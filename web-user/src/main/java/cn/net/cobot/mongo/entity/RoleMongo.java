package cn.net.cobot.mongo.entity;

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
public class RoleMongo implements Serializable {


    private static final long serialVersionUID = 687935061414448004L;
    /**
     * 角色ID
     */
    @Id
    private String roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    private String roleName;

    /**
     * 角色描述
     */
    @Size(max = 50, message = "{noMoreThan}")
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 角色对应的菜单（按钮） id
     */
    private transient String menuIds;


    /**
     * 是否是系统属性，如果是：不能被删除
     */
    private Boolean system;
}
