package ru.knastnt.gas_water_usage_app.logic;

import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.model.MeasureHistory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MeasurementService {
    void submitMeasure(BigDecimal value, Long meterId);
    List<MeasureHistory> getMeasureHistory(Long meterId);

    Optional<Account> getAccountInfo(String accountNum);
}
