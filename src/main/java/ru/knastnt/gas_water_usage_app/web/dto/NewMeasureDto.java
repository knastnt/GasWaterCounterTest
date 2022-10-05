package ru.knastnt.gas_water_usage_app.web.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewMeasureDto {
    private BigDecimal value;
}
