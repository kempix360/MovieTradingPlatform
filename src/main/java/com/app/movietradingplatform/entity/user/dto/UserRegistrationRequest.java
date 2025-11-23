package com.app.movietradingplatform.entity.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserRegistrationRequest {
    private String username;
    private String password;
}
