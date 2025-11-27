package com.app.movietradingplatform.entity.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserRequest {
    private String username;
    private String password;
}
