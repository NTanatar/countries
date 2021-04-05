package com.tannat.country.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
}
