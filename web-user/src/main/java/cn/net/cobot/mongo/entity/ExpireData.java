package cn.net.cobot.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/12/17 17:52
 * 不加document注解不生效
 * TTL索引是单字段索引。复合索引不支持TTL，并且忽略该 expireAfterSeconds选项。
 * 该_id字段不支持TTL索引。
 * 如果某个字段已经存在非TTL单字段索引，则无法在同一字段上创建TTL索引，因为您无法创建具有相同密钥规格且仅选项不同的索引。
 * 要将非TTL单字段索引更改为TTL索引，必须首先删除该索引，然后使用该expireAfterSeconds选项重新创建 。
 */
@Data
@Document(collection = "expireData")
public class ExpireData implements Serializable {


    private static final long serialVersionUID = -1386610110433692207L;


    @Id
    private String id;


    private String value;


    //过期时间为100秒
    @Indexed(expireAfterSeconds = 100)
    private Date createTime;


}
