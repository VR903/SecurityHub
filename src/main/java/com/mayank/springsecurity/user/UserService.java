package com.mayank.springsecurity.user;

import java.security.Principal;

public interface UserService {
    void changePassword(ChangePasswordRequest request, Principal principal);
}
