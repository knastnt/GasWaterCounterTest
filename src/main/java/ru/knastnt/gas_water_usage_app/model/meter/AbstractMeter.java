package ru.knastnt.gas_water_usage_app.model.meter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.knastnt.gas_water_usage_app.model.Account;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractMeter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(nullable = false)
    protected String identity;
    @Column(nullable = false)
    protected LocalDate startWorking;
    protected LocalDate endWorking;
    @ManyToOne(optional = false)
    protected Account account;
}
