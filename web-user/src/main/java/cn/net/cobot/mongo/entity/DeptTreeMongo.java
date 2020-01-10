package cn.net.cobot.mongo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Data
public class DeptTreeMongo<T> implements Serializable {


    private static final long serialVersionUID = -4979431595183622634L;
    private String id;
    private String icon;
    private String href;
    private String name;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<DeptTreeMongo<T>> children;
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private DeptMongo data;

    public void initChildren(){
        this.children = new ArrayList<>();
    }

}
