package com.medirec.entity;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private Long id;
    private String originalPassword;
    private String newPassword;
    private String confirmNewPassword;

    @Override
    public String toString() {
        return "PasswordResetDTO{" +
                "id=" + id +
                ", originalPassword='" + originalPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmNewPassword='" + confirmNewPassword + '\'' +
                '}';
    }
}
