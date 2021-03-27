package com.tannat.country.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@Table(name = "cities")
@ToString(exclude = "country")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate foundingDate;
    private LocalDate cityDay;
    private Boolean hasRiver;
    private Integer population;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}
