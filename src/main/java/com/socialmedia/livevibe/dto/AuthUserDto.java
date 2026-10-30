package com.socialmedia.livevibe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {
    private String id;
    private String name;
    private String profileImage;
    private String role;
}
