package com.financeTracker.model.entity;

import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * Represents a user in the system.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /** User's login (username). */
    private String login;

    /** User's password (hashed for security). */
    private String password;

    /** User's full name. */
    private String name;

    /** Role of the user in the system. Default is USER. */
    @Builder.Default
    private UserRole userRole = UserRole.USER;

    /** Status of the user (active, inactive, etc.). Default is ACTIVE. */
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

}
