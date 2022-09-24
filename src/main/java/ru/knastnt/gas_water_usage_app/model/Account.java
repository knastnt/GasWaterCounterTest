package ru.knastnt.gas_water_usage_app.model;

import lombok.Getter;
import lombok.Setter;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "account")
    private List<AbstractMeter> meters = new ArrayList<>();
}
