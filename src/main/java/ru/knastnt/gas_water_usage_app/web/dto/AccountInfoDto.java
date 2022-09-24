package ru.knastnt.gas_water_usage_app.web.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountInfoDto {
    private String userName;
    private String accountNumber;
    private List<MeterDto> meters = new ArrayList<>();
}
