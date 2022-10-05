package ru.knastnt.gas_water_usage_app.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.knastnt.gas_water_usage_app.exceptions.NotFoundException;
import ru.knastnt.gas_water_usage_app.logic.MeasurementService;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.web.dto.AccountInfoDto;
import ru.knastnt.gas_water_usage_app.web.dto.MeasureHistoryDto;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
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

    @GetMapping("/account/{accountNum}")
    public ResponseEntity<AccountInfoDto> getInfo(@PathVariable String accountNum) {
        Account account = measurementService.getAccountInfo(accountNum).orElseThrow(() -> new NotFoundException("Account not found"));
        return ResponseEntity.ok(mapper.mapAccount(account));
    }

    @GetMapping("/meter/{meterId}/history")
    public ResponseEntity<List<MeasureHistoryDto>> getHistory(@PathVariable Long meterId) {
        return ResponseEntity.ok(mapper.mapHistList(measurementService.getMeasureHistory(meterId)));
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleEx(NotFoundException e) {
        log.warn("NotFoundException in rest calling", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
