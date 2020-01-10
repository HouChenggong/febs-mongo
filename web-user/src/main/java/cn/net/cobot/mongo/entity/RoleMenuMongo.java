package cn.net.cobot.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author MrBird
 */
@Data
public class RoleMenuMongo implements Serializable {


    private static final long serialVersionUID = -6453374144977578091L;


    @Id
    private String id;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单/按钮ID
     */
    private String menuId;


}
