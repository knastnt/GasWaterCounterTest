package ru.knastnt.gas_water_usage_app.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MeasureHistoryDto {
    private LocalDateTime created;
    private BigDecimal value;
}
