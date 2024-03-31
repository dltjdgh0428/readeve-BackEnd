package com.book_everywhere.auth.dto;

import com.book_everywhere.auth.entity.Role;
import lombok.Data;

@Data
public class UserDto {
    private String nickname;
    private Role role;
}
