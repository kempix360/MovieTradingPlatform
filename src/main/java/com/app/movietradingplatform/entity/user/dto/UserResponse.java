package com.app.movietradingplatform.entity.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserResponse {
    private UUID id;
    private String username;
    private LocalDate registrationDate;
}
