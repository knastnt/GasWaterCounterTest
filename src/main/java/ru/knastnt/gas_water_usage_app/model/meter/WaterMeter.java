package ru.knastnt.gas_water_usage_app.model.meter;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("WM")
public class WaterMeter extends AbstractMeter{
    @Enumerated(EnumType.STRING)
    private TypeOfWater typeOfWater;

    public enum TypeOfWater {
        COLD,
        HOT
    }
}
