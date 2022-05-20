package com.medirec.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.medirec.entity.PasswordResetDTO;
import com.medirec.entity.User;
import com.medirec.mapper.UserMapper;
import com.medirec.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zsm
 * @since 2022-05-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(User user) {
        return this.getOne(new QueryWrapper<User>().lambda()
                .eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword()));
    }

    @Override
    public User register(User user) {
        User registerUser = this.getOne(new QueryWrapper<User>().lambda()
                .eq(User::getUsername, user.getUsername()));
        if (registerUser == null) {
            return this.save(user) ? user : null;
        } else {
            return null;
        }
    }

    @Override
    public int resetPassword(PasswordResetDTO dto) {
        System.out.println(dto);
        User user = this.getOne(new QueryWrapper<User>().lambda()
                .eq(User::getId, dto.getId())
                .eq(User::getPassword, dto.getOriginalPassword()));
        if (user == null) {
            return 0;
        } else {
            user.setPassword(dto.getNewPassword());
            return this.updateById(user) ? 1 : -1;
        }

    }

    @Override
    public int changeProfile(User user) {
        System.out.println(user);
        User changedUser = this.getOne(new QueryWrapper<User>().lambda()
                .eq(User::getId, user.getId()));
        if (changedUser == null) {
            return 0;
        } else {
            return this.updateById(user) ? 1 : -1;
        }
    }
}
