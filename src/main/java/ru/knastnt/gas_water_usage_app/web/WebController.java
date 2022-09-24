package ru.knastnt.gas_water_usage_app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.knastnt.gas_water_usage_app.exceptions.NotFoundException;
import ru.knastnt.gas_water_usage_app.logic.MeasurementService;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.web.dto.AccountInfoDto;
import ru.knastnt.gas_water_usage_app.web.dto.MeasureHistoryDto;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WebController {
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private WebMapper mapper;

    @PutMapping("/{meterId}")
    public ResponseEntity<String> submitMeasure(BigDecimal value, @PathVariable Long meterId) {
        measurementService.submitMeasure(value, meterId);
        return ResponseEntity.ok("Measure accepted");
    }

    @GetMapping("/account")
    public ResponseEntity<AccountInfoDto> getInfo(String accountNum) {
        Account account = measurementService.getAccountInfo(accountNum).orElseThrow(() -> new NotFoundException("Account not found"));
        return ResponseEntity.ok(mapper.mapAccount(account));
    }

    @GetMapping("/meter/{meterId}/history")
    public ResponseEntity<List<MeasureHistoryDto>> getInfo(@PathVariable Long meterId) {
        return ResponseEntity.ok(mapper.mapHistList(measurementService.getMeasureHistory(meterId)));
    }
}
