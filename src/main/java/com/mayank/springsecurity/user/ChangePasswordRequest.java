package com.mayank.springsecurity.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequest {

    @NotBlank(message = "Old password cannot be blank!")
    private String currentPassword;
    @NotBlank(message = "New password cannot be blank!")
    private String newPassword;
    @NotBlank(message = "Confirm New password cannot be blank!")
    private String confirmNewPassword;
}
