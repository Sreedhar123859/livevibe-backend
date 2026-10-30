package com.socialmedia.livevibe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenInfo {
    private String iss;
    private String aud;
    private String sub;
    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    private String name;
    private String picture;
    private String exp;
}

