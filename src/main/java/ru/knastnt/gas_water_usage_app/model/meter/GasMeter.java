package ru.knastnt.gas_water_usage_app.model.meter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("GM")
public class GasMeter extends AbstractMeter{
}
