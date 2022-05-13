package com.medirec.service.impl;

import com.medirec.entity.User;
import com.medirec.mapper.UserMapper;
import com.medirec.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zsm
 * @since 2022-05-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
