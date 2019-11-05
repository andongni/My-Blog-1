package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.UserMapper;
import com.site.blog.my.core.entity.User;
import com.site.blog.my.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: yuanyuqing
 * @Date: 2019/11/5/0005
 * @email 973446095@qq.com
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 保存user信息
     * @param user
     */
    public void saveUser(User user){
        if(user == null){
            return;
        }
        userMapper.insert(user);
    }
}
