package com.site.blog.my.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description:
 * @Author: yuanyuqing
 * @Date: 2019/11/5/0005
 * @email 973446095@qq.com
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class User {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * qq登录openid
     */
    private String openId;

    /**
     * qq昵称
     */
    private String nickName;

    /**
     * 创建时间
     */
    private Date ceateTime;

    /**
     * 是否可用 0否，1是
     */
    private Integer enable;

    /**
     * 头像地址
     */
    private String figureUrl;

    /**
     * 用户名
     */
    private String userName;
}
