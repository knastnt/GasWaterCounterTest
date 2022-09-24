package ru.knastnt.gas_water_usage_app.model.meter;

import lombok.Getter;
import lombok.Setter;
import ru.knastnt.gas_water_usage_app.model.Account;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class AbstractMeter {
    @Id
    @GeneratedValue
    protected Long id;
    @ManyToOne(optional = false)
    private Account account;
    @Column(nullable = false)
    protected String identity;
    @Column(nullable = false)
    protected LocalDate startWorking;
    protected LocalDate endWorking;
}
