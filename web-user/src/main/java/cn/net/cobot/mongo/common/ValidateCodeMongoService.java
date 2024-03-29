package cn.net.cobot.mongo.common;

import cn.net.cobot.common.entity.FebsConstant;
import cn.net.cobot.common.entity.ImageType;
import cn.net.cobot.common.exception.FebsException;
import cn.net.cobot.common.properties.FebsProperties;
import cn.net.cobot.common.properties.ValidateCodeProperties;
import cn.net.cobot.mongo.dao.ExpireMongoDao;
import cn.net.cobot.mongo.entity.ExpireData;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 验证码服务
 *
 * @author MrBird
 */
@Service
public class ValidateCodeMongoService {

    @Autowired
    private ExpireMongoDao expireMongoDao;
    @Autowired
    private FebsProperties properties;


    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String key = session.getId();
        ValidateCodeProperties code = properties.getCode();
        setHeader(response, code.getType());

        Captcha captcha = createCaptcha(code);
//        expireMongoDao.set(FebsConstant.CODE_PREFIX + key, StringUtils.lowerCase(captcha.text()), code.getTime());
        ExpireData expireData = new ExpireData();
        expireData.setId(FebsConstant.CODE_PREFIX + key);
        expireData.setCreateTime(new Date());
        expireData.setValue(StringUtils.lowerCase(captcha.text()));
        expireMongoDao.insertOrCover(expireData);
        captcha.out(response.getOutputStream());
    }


    public void check(String key, String value) throws FebsException {
        ExpireData expireData = expireMongoDao.queryById(FebsConstant.CODE_PREFIX + key);
        if (StringUtils.isBlank(value)) {
            throw new FebsException("请输入验证码");
        }
        if (expireData == null) {
            throw new FebsException("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(value, (expireData.getValue()))) {
            throw new FebsException("验证码不正确");
        }
    }

    private Captcha createCaptcha(ValidateCodeProperties code) {
        Captcha captcha = null;
        if (StringUtils.equalsIgnoreCase(code.getType(), ImageType.GIF)) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());
        return captcha;
    }

    private void setHeader(HttpServletResponse response, String type) {
        if (StringUtils.equalsIgnoreCase(type, ImageType.GIF)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
