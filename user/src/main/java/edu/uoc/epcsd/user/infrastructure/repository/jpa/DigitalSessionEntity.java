package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.DigitalSession;
import lombok.*;

import javax.persistence.*;

@Entity(name = "DigitalSession")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalSessionEntity implements DomainTranslatable<DigitalSession> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "link", nullable = false)
    private String link;

    @ManyToOne(optional = false)
    private UserEntity user;

    public static DigitalSessionEntity fromDomain(DigitalSession digitalSession) {
        if (digitalSession == null) {
            return null;
        }

        return DigitalSessionEntity.builder()
                .id(digitalSession.getId())
                .description(digitalSession.getDescription())
                .location(digitalSession.getLocation())
                .link(digitalSession.getLink())
                .build();
    }

    @Override
    public DigitalSession toDomain() {
        return DigitalSession.builder()
                .id(this.getId())
                .description(this.getDescription())
                .location(this.getLocation())
                .link(this.getLink())
                .userId(this.getUser().getId())
                .build();
    }
}
