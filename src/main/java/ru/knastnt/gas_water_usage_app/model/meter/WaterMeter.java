package ru.knastnt.gas_water_usage_app.model.meter;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity
public class WaterMeter extends AbstractMeter{
    @Enumerated(EnumType.STRING)
    private TypeOfWater typeOfWater;

    public enum TypeOfWater {
        COLD,
        HOT
    }
}
