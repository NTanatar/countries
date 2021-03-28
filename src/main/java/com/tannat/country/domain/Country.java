package com.tannat.country.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
public class Country {
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
