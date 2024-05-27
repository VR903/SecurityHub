package com.mayank.springsecurity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_CREATE("admin::create"),
    ADMIN_READ("admin::read"),
    ADMIN_UPDATE("admin::update"),
    ADMIN_DELETE("admin::delete"),
    USER_CREATE("user::create"),
    USER_READ("user::read"),
    USER_UPDATE("user::update"),
    USER_DELETE("user::delete");

    private final String permissions;
}
