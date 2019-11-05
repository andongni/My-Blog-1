package com.site.blog.my.core.controller.qqLogin;

import com.alibaba.fastjson.JSONObject;
import com.site.blog.my.core.entity.User;
import com.site.blog.my.core.service.UserService;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @Description:
 * @Author: yuanyuqing
 * @CreateDate: 2019/11/5 15:32
 * @Version: 1.0
 */
@RestController
@RequestMapping("/qq/oauth")
public class QqController {

    @Value("${qq.oauth.http}")
    private String qqUrl;
    @Value("${qq.oauth.response_type}")
    private String response_type;
    @Value("${qq.oauth.client_id}")
    private String client_id;
    @Value("${qq.oauth.redirect_uri}")
    private String redirect_uris;
    @Value("${qq.oauth.appid}")
    private String appid;
    @Value("${qq.oauth.appkey}")
    private String appkey;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String qqLogin(HttpSession httpSession){
        String redirect_uris_callback = qqUrl + redirect_uris ;

        String state_uuid = UUID.randomUUID().toString().replace("-","");
        httpSession.setAttribute("state",state_uuid);

        String url = "https://graph.qq.com/oauth2.0/authorize?response_type=code"+
                "&client_id=" + client_id +
                "&redirect_uri=" + redirect_uris_callback +
                "&state=" + state_uuid;
        return "redirect:" + qqUrl;
    }

    @GetMapping("/callback")
    public String qqCallback(HttpServletRequest request)throws IOException {
        HttpSession session = request.getSession();
        //qq返回的信息：http://graph.qq.com/demo/index.jsp?code=9A5F************************06AF&state=test
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String uuid = (String) session.getAttribute("state");

        if(uuid != null){
            if(!uuid.equals(state)){
                throw new RuntimeException("QQ,state错误");
            }
        }


        //Step2：通过Authorization Code获取Access Token
        String backUrl = qqUrl + redirect_uris;
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code"+
                "&client_id=" + client_id +
                "&client_secret=" + appkey +
                "&code=" + code +
                "&redirect_uri=" + backUrl;

        String access_token = QQHttpClient.getAccessToken(url);

        //Step3: 获取回调后的 openid 值
        url = "https://graph.qq.com/oauth2.0/me?access_token=" + access_token;
        String openid = QQHttpClient.getOpenID(url);

        //Step4：获取QQ用户信息
        url = "https://graph.qq.com/user/get_user_info?access_token=" + access_token +
                "&oauth_consumer_key="+ appid +
                "&openid=" + openid;

        JSONObject jsonObject = QQHttpClient.getUserInfo(url);

        //也可以放到Redis和mysql中
        String nickname = (String)jsonObject.get("nickname");
        String figureurl = (String)jsonObject.get("figureurl_qq_2");
        session.setAttribute("openid",openid);  //openid,用来唯一标识qq用户
        session.setAttribute("nickname",nickname); //QQ名
        session.setAttribute("figureurl_qq_2",figureurl); //大小为100*100像素的QQ头像URL
        User user = new User();
        user.setCeateTime(new Date());
        user.setEnable(1);
        user.setNickName(nickname);
        user.setOpenId(openid);
        user.setFigureUrl(figureurl);
        userService.saveUser(user);
        return "redirect:"+qqUrl;
    }
}
