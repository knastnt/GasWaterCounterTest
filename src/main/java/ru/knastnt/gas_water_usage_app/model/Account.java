package ru.knastnt.gas_water_usage_app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String accountNum;
    @ManyToOne(optional = false)
    private Customer customer;
}
