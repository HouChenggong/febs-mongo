package cn.net.cobot.mongo.common;

import cn.net.cobot.mongo.entity.UserMongo;
import cn.net.cobot.mongo.entity.dto.UserMongoDTO;
import org.springframework.beans.BeanUtils;

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/12/20 11:41
 */
public class UserInputDTOConvert implements DTOConvert<UserMongoDTO, UserMongo> {
    @Override
    public UserMongo convert(UserMongoDTO userInputDTO) {
        UserMongo user = new UserMongo();
        BeanUtils.copyProperties(userInputDTO, user);
        return user;
    }
}
