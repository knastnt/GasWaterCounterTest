package ru.knastnt.gas_water_usage_app.model;

import lombok.*;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String accountNum;
    @ManyToOne(optional = false)
    private Customer customer;
    @OneToMany(mappedBy = "account", targetEntity = AbstractMeter.class, fetch = FetchType.EAGER)
    @Builder.Default
    private List<AbstractMeter> meters = new ArrayList<>();
}
