package cn.net.health.xiyou.controller.shiro;


import cn.net.health.xiyou.shiro.common.utils.SnowFlake;
import cn.net.health.xiyou.shiro.mongo.service.IRoleMongoService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/11/6 11:45
 */
@RestController
@Api("test接口")
@RequestMapping("/test2/*")
public class DemoTestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IRoleMongoService roleMongoService;



    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户标识", required = true, paramType = "query", dataType = "String")
    })

    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public String deleteUser(@RequestParam("userId") String userId) {
        System.out.println("deleteUser:::" + userId);
        LookupOperation lookupOperation = LookupOperation.newLookup().
                from("userRoleMongo").  //关联从表名
                localField("role_id").     //主表关联字段
                foreignField("role_id").//从表关联的字段
                as("role_userRole");   //查询结果名
//带条件查询可以选择添加下面的条件
//       Criteria criteria=Criteria.where("studenAndgrade").not().size(0);   //只查询有结果的学生
//        Criteria qqq=Criteria.where("name").regex("文");//只查询名字中带有文的
        //       AggregationOperation match1= Aggregation.match(qqq);
//        AggregationOperation match = Aggregation.match(criteria);
//        Aggregation counts = Aggregation.newAggregation(match1,lookupOperation,match).;
        Aggregation aggregation = Aggregation.newAggregation(lookupOperation);
        List<Map> results = mongoTemplate.aggregate(aggregation, "roleMongo", Map.class).getMappedResults();
        //上面的student必须是查询的主表名
        System.out.println(JSON.toJSONString(results));
        return "OK";
    }

    @ApiOperation(value = "查询用户", notes = "查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户标识", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/queryUser", method = RequestMethod.GET)

    public String queryUser(@RequestParam("userId") String userId) {
        System.out.println("queryUser:::" + userId);
        return "OK";

    }


}
