package ru.knastnt.gas_water_usage_app.web.dto;

import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;
import ru.knastnt.gas_water_usage_app.model.meter.GasMeter;
import ru.knastnt.gas_water_usage_app.model.meter.WaterMeter;

import static ru.knastnt.gas_water_usage_app.model.meter.WaterMeter.TypeOfWater.COLD;
import static ru.knastnt.gas_water_usage_app.model.meter.WaterMeter.TypeOfWater.HOT;

public enum MeterType {
    GAS,
    WATER_HOT,
    WATER_COLD;

    public static MeterType getByMeter(AbstractMeter meter) {
        if (meter == null) return null;
        if (meter instanceof GasMeter) return GAS;
        if (meter instanceof WaterMeter) {
            if (HOT.equals(((WaterMeter) meter).getTypeOfWater())) return WATER_HOT;
            if (COLD.equals(((WaterMeter) meter).getTypeOfWater())) return WATER_COLD;
        }
        throw new  IllegalArgumentException();
    }
}
