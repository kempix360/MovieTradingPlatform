package com.app.movietradingplatform.entity.director.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DirectorRequest {
    private String name;
    private String description;
}
