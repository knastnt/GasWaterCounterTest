package ru.knastnt.gas_water_usage_app.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MeterDto {
    private Long id;
    private String identity;
    private MeterType type;
    private BigDecimal currentValue;
    private LocalDate startWorking;
    private LocalDate endWorking;
}
