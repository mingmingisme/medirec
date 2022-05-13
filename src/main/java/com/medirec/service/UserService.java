package com.medirec.service;

import com.medirec.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsm
 * @since 2022-05-13
 */
public interface UserService extends IService<User> {

    User login(User user);

    User register(User user);
}
