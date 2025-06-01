package edu.uoc.epcsd.user.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalSession {

    @NotNull
    private Long id;

    @NotNull
    private String description;
    @NotNull

    private String location;

    @NotNull
    private String link;

    @NotNull
    private Long userId;

}
