package ru.knastnt.gas_water_usage_app.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeasureHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AbstractMeter meter;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime created;

    public MeasureHistory(AbstractMeter meter, BigDecimal value) {
        this.meter = meter;
        this.value = value;
    }
}
