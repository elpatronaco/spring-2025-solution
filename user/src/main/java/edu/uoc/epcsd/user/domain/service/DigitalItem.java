package edu.uoc.epcsd.user.domain.service;

import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.NotNull;

import edu.uoc.epcsd.user.domain.DigitalItemStatus;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalItem {

    @NotNull
    private Long id;
    
    @NotNull
    private Long digitalSessionId;

    @NotNull
    private String description;
    
	@NotNull
    private Long lat;
    
    @NotNull
    private Long lon;
    
    @NotNull
    private String link;

    @NotNull
    @Builder.Default
    private DigitalItemStatus status = DigitalItemStatus.AVAILABLE;
}
