package ru.knastnt.gas_water_usage_app.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.knastnt.gas_water_usage_app.exceptions.InactiveMeterException;
import ru.knastnt.gas_water_usage_app.exceptions.IncorrectMeasurementException;
import ru.knastnt.gas_water_usage_app.exceptions.NotFoundException;
import ru.knastnt.gas_water_usage_app.logic.MeasurementService;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.web.dto.AccountInfoDto;
import ru.knastnt.gas_water_usage_app.web.dto.MeasureHistoryDto;
import ru.knastnt.gas_water_usage_app.web.dto.NewMeasureDto;

import java.util.List;

@Slf4j
@RestController
public class WebController {
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private WebMapper mapper;

    @ApiOperation(
            value = "Returns information about customer account",
            response = AccountInfoDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Account not found")
    })
    @GetMapping("/account/{accountNum}")
    public ResponseEntity<AccountInfoDto> getInfo(@PathVariable String accountNum) {
        Account account = measurementService.getAccountInfo(accountNum).orElseThrow(() -> new NotFoundException("Account not found"));
        return ResponseEntity.ok(mapper.mapAccount(account));
    }

    @ApiOperation(
            value = "Returns a list of all submitted measurements for meter",
            notes = "Fresh measurements in top of the list",
            response = MeasureHistoryDto.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Meter not found")
    })
    @GetMapping("/meter/{meterId}/history")
    public ResponseEntity<List<MeasureHistoryDto>> getHistory(@PathVariable Long meterId) {
        return ResponseEntity.ok(mapper.mapHistList(measurementService.getMeasureHistory(meterId)));
    }

    @ApiOperation(
            value = "Submit measureme for meter",
            notes = "New measure must be bigger than previous. Meter must be active with it work dates."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Measure accepted"),
            @ApiResponse(code = 403, message = "Measurement must be positive number | Previous measurement is bigger than new | Measurement work time isn't correspond with now"),
            @ApiResponse(code = 404, message = "Meter not found"),
    })
    @PostMapping("/meter/{meterId}/history")
    public ResponseEntity<String> submitMeasure(@RequestBody NewMeasureDto newMeasureDto, @PathVariable Long meterId) {
        measurementService.submitMeasure(newMeasureDto.getValue(), meterId);
        return ResponseEntity.ok("Measure accepted");
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleEx(NotFoundException e) {
        log.warn("NotFoundException in rest calling", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(IncorrectMeasurementException.class)
    public ResponseEntity<String> handleEx(IncorrectMeasurementException e) {
        log.warn("IncorrectMeasurementException in rest calling", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(InactiveMeterException.class)
    public ResponseEntity<String> handleEx(InactiveMeterException e) {
        log.warn("InactiveMeterException in rest calling", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
