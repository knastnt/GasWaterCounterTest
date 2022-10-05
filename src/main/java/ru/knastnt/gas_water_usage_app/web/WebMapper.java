package ru.knastnt.gas_water_usage_app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.model.MeasureHistory;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;
import ru.knastnt.gas_water_usage_app.repository.MeasureHistoryRepository;
import ru.knastnt.gas_water_usage_app.web.dto.AccountInfoDto;
import ru.knastnt.gas_water_usage_app.web.dto.MeasureHistoryDto;
import ru.knastnt.gas_water_usage_app.web.dto.MeterDto;
import ru.knastnt.gas_water_usage_app.web.dto.MeterType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebMapper {
    @Autowired
    private MeasureHistoryRepository measureHistoryRepository;

    @Transactional
    public AccountInfoDto mapAccount(Account account) {
        if (account == null) return null;
        AccountInfoDto result = new AccountInfoDto();

        result.setUserName(account.getCustomer().getDemoFio());
        result.setAccountNumber(account.getAccountNum());
        result.setMeters(account.getMeters().stream().map(this::mapMeter).collect(Collectors.toList()));

        return result;
    }

    public MeterDto mapMeter(AbstractMeter meter) {
        if (meter == null) return null;
        MeterDto result = new MeterDto();

        result.setId(meter.getId());
        result.setIdentity(meter.getIdentity());
        result.setType(MeterType.getByMeter(meter));
        result.setCurrentValue(measureHistoryRepository
                .findFirstByMeterOrderByIdDesc(meter).map(MeasureHistory::getValue).orElse(null));
        result.setStartWorking(meter.getStartWorking());
        result.setEndWorking(meter.getEndWorking());

        return result;
    }

    public List<MeasureHistoryDto> mapHistList(List<MeasureHistory> measureHistory) {
        if (measureHistory == null) return Collections.emptyList();
        return measureHistory.stream()
                .map(this::mapHist)
                .collect(Collectors.toList());
    }

    private MeasureHistoryDto mapHist(MeasureHistory measureHistory) {
        if (measureHistory == null) return null;
        MeasureHistoryDto result = new MeasureHistoryDto();

        result.setCreated(measureHistory.getCreated());
        result.setValue(measureHistory.getValue());

        return result;
    }
}
