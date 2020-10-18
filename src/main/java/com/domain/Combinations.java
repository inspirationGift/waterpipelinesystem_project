package com.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Combinations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int routeId;
    private int x;
    private int y;
    @Column(nullable = true)
    private Integer mid;
    @Column(nullable = true)
    private Integer len;
    private int version;
}
