package com.tannat.country.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long countryId;
    private String name;
    private LocalDate foundingDate;
    private LocalDate cityDay;
    private Boolean hasRiver;
    private Integer population;
}
