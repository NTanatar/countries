package com.tannat.country.domain;

import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "countries")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Country extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String worldRegion;
    private String governmentType;
    private Integer regionsCount;

    @Column(name = "landlocked")
    private Boolean isLandlocked;
    private LocalDate foundingDate;

    @OneToMany(mappedBy = "country")
    private List<City> cities;

    public boolean hasCity(Long cityId) {
        return cityId != null && !CollectionUtils.isEmpty(cities) &&
                cities.stream().anyMatch(c -> cityId.equals(c.getId()));
    }

    public Optional<City> getCity(Long id) {
        if (id == null || CollectionUtils.isEmpty(cities)) {
            return Optional.empty();
        }
        return cities.stream().filter(c -> id.equals(c.getId())).findFirst();
    }
}
