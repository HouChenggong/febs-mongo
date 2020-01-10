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
public class DeptMongo implements Serializable {


    private static final long serialVersionUID = 7878200062551789133L;
    /**
     * 部门 ID
     */
    @Id
    private String deptId;

    /**
     * 上级部门 ID
     */
    private String parentId;

    /**
     * 部门名称
     */
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    private String deptName;

    /**
     * 排序
     */
    private Long orderNum;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date modifyTime;


    private Integer level;
}
