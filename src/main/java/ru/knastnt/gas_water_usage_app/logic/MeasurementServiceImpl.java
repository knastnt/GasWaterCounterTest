package ru.knastnt.gas_water_usage_app.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knastnt.gas_water_usage_app.exceptions.IncorrectMeasurementException;
import ru.knastnt.gas_water_usage_app.exceptions.NotFoundException;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.model.MeasureHistory;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;
import ru.knastnt.gas_water_usage_app.repository.AccountRepository;
import ru.knastnt.gas_water_usage_app.repository.MeasureHistoryRepository;
import ru.knastnt.gas_water_usage_app.repository.MeterRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MeasurementServiceImpl implements MeasurementService {
    @Autowired
    private MeterRepository meterRepository;
    @Autowired
    private MeasureHistoryRepository measureHistoryRepository;
    @Autowired
    private AccountRepository accountRepository;


    @Override
    @Transactional
    public void submitMeasure(BigDecimal newValue, Long meterId) {
        log.debug("Submit measure {} for meterId = {}", newValue, meterId);
        if (newValue == null || newValue.signum() <= 0) throw new IncorrectMeasurementException("Measurement must be positive number");
        AbstractMeter meter = meterRepository.findById(meterId).orElseThrow(() -> new NotFoundException("Meter not found"));
        Optional<MeasureHistory> lastMeasure = measureHistoryRepository.findFirstByMeterOrderByIdDesc(meter);
        if (lastMeasure.isPresent() && lastMeasure.get().getValue().compareTo(newValue) > 0)
            throw new IncorrectMeasurementException("Previous measurement is bigger than new");
        measureHistoryRepository.save(new MeasureHistory(meter, newValue));
    }

    @Override
    public List<MeasureHistory> getMeasureHistory(Long meterId) {
        return measureHistoryRepository.findByMeterIdOrderByCreatedDesc(meterId);
    }

    @Override
    public Optional<Account> getAccountInfo(String accountNum) {
        return accountRepository.findByAccountNum(accountNum);
    }
}
