package ru.knastnt.gas_water_usage_app.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MeasureHistoryDto {
    private LocalDate created;
    private BigDecimal value;
}
