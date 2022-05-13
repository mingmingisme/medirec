package com.medirec.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * User类
 *
 * @author zsm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String password;

    private String email;

    private String occupation;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
