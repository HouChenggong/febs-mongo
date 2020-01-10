package cn.net.cobot.mongo.entity;

import cn.net.cobot.common.annotation.IsMobile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author H1871
 */
@Data
public class UserMongo implements Serializable {


    // 用户状态：有效
    public static final String STATUS_VALID = "1";
    // 用户状态：锁定
    public static final String STATUS_LOCK = "0";
    // 默认头像
    public static final String DEFAULT_AVATAR = "default.jpg";
    // 默认密码
    public static final String DEFAULT_PASSWORD = "_123456";
    // 性别男
    public static final String SEX_MALE = "0";
    // 性别女
    public static final String SEX_FEMALE = "1";
    // 性别保密
    public static final String SEX_UNKNOW = "2";
    // 黑色主题
    public static final String THEME_BLACK = "black";
    // 白色主题
    public static final String THEME_WHITE = "white";
    // TAB开启
    public static final String TAB_OPEN = "1";
    // TAB关闭
    public static final String TAB_CLOSE = "0";


    private static final long serialVersionUID = -6515298226435152565L;


    /**
     * 用户 ID
     */
    @Id
    private Long userId;

    /**
     * 用户名
     */
    //@Field(name = "USERNAME")
    @Size(min = 4, max = 10, message = "{range}")
    //@ExcelField(value = "用户名")
    private String username;

    /**
     * 密码
     */
    //@Field(name = "PASSWORD")
    private String password;


    /**
     * 邮箱
     */
    //@Field(name = "EMAIL")
    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    //@ExcelField(value = "邮箱")
    private String email;

    /**
     * 联系电话
     */
    //@Field(name = "MOBILE")
    @IsMobile(message = "{mobile}")
    //@ExcelField(value = "联系电话")
    private String mobile;

    /**
     * 状态 0锁定 1有效
     */
    //@Field(name = "STATUS")
    @NotBlank(message = "{required}")
    //@ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    private String status;

    /**
     * 创建时间
     */
    //@Field(name = "CREATE_TIME")
    //@ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 修改时间
     */
    //@Field(name = "MODIFY_TIME")
    //@ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

    /**
     * 最近访问时间
     */
    //@Field(name = "LAST_LOGIN_TIME")
    //@ExcelField(value = "最近访问时间", writeConverter = TimeConverter.class)
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 性别 0男 1女 2 保密
     */
    //@Field(name = "SSEX")
    @NotBlank(message = "{required}")
    //@ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    private String sex;

    /**
     * 头像
     */
    //@Field(name = "AVATAR")
    private String avatar;

    /**
     * 主题
     */
    //@Field(name = "THEME")
    private String theme;

    /**
     * 是否开启 tab 0开启，1关闭
     */
    //@Field(name = "IS_TAB")
    private String isTab;

    /**
     * 描述
     */
    //@Field(name = "DESCRIPTION")
    @Size(max = 100, message = "{noMoreThan}")
    //@ExcelField(value = "个人描述")
    private String description;


    //@Field()
    private String createTimeFrom;
    //@Field()
    private String createTimeTo;
    /**
     * 角色 ID和名称组合的列表
     */
    //@Field()
    private Set<RoleMongo> roleMap;

    /**
     * 暂时保留这个接口
     */
    private String roleId;

    /**
     * 角色名称，保留
     */
    private String roleName;
    /**
     * 保留
     */
    private String deptName;


    /**
     * 暂时保留这个接口
     */
    private String deptId;

    /**
     * 部门 ID和名称组成的列表
     */
    //@Field(name = "DEPT_ID")
    private Set<DeptMongo> deptMap;

    public Long getId() {
        return userId;
    }
}
