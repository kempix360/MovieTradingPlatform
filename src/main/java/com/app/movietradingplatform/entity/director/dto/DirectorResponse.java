package com.app.movietradingplatform.entity.director.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DirectorResponse {
    private UUID id;
    private String name;
    private String description;
}
