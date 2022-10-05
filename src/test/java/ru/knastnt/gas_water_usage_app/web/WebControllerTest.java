package ru.knastnt.gas_water_usage_app.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.knastnt.gas_water_usage_app.GasWaterUsageAppApplicationTests;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.model.Customer;
import ru.knastnt.gas_water_usage_app.model.MeasureHistory;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;
import ru.knastnt.gas_water_usage_app.model.meter.GasMeter;
import ru.knastnt.gas_water_usage_app.model.meter.WaterMeter;
import ru.knastnt.gas_water_usage_app.repository.AccountRepository;
import ru.knastnt.gas_water_usage_app.repository.CustomerRepository;
import ru.knastnt.gas_water_usage_app.repository.MeasureHistoryRepository;
import ru.knastnt.gas_water_usage_app.repository.MeterRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class WebControllerTest extends GasWaterUsageAppApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MeterRepository meterRepository;
    @Autowired
    private MeasureHistoryRepository measureHistoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final String ACCOUNT_NUM = "00001";
    private AbstractMeter meter1;
    private AbstractMeter meter2;
    private AbstractMeter meter3;

    @BeforeEach
    void init() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Иван")
                .middleName("Иванович")
                .lastName("Иванов")
                .build());
        Account account = accountRepository.save(Account.builder()
                .accountNum(ACCOUNT_NUM)
                .customer(customer)
                .build());
        meter1 = meterRepository.save(WaterMeter.builder()
                .typeOfWater(WaterMeter.TypeOfWater.HOT)
                .account(account)
                .identity("X000123")
                .startWorking(LocalDate.of(2022, 10, 5))
                .endWorking(null)
                .build());
        meter2 = meterRepository.save(WaterMeter.builder()
                .typeOfWater(WaterMeter.TypeOfWater.HOT)
                .account(account)
                .identity("X000100")
                .startWorking(LocalDate.of(2021, 1, 1))
                .endWorking(LocalDate.of(2022, 10, 4))
                .build());
        meter3 = meterRepository.save(GasMeter.builder()
                .account(account)
                .identity("G000200")
                .startWorking(LocalDate.of(2022, 9, 1))
                .endWorking(null)
                .build());
        measureHistoryRepository.save(MeasureHistory.builder()
                .meter(meter1)
                .value(new BigDecimal("00001.15"))
                .build());
        measureHistoryRepository.save(MeasureHistory.builder()
                .meter(meter1)
                .value(new BigDecimal("00009.93"))
                .build());
        entityManager.flush();
        entityManager.clear();
    }

    @Nested
    class GetAccInfo {
        @Test
        void notExist_return404() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/account/unexistedAccountNum"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().string("Account not found"));
        }

        @Test
        void accountJsonContent() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/account/" + ACCOUNT_NUM))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(
                            "{\n" +
                                    "  \"userName\": \"Иван Иванович И.\",\n" +
                                    "  \"accountNumber\": \"00001\",\n" +
                                    "  \"meters\": [\n" +
                                    "    {\n" +
                                    "      \"identity\": \"X000123\",\n" +
                                    "      \"type\": \"WATER_HOT\",\n" +
                                    "      \"currentValue\": 9.93,\n" +
                                    "      \"startWorking\": \"2022-10-05\",\n" +
                                    "      \"endWorking\": null\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"identity\": \"X000100\",\n" +
                                    "      \"type\": \"WATER_HOT\",\n" +
                                    "      \"currentValue\": null,\n" +
                                    "      \"startWorking\": \"2021-01-01\",\n" +
                                    "      \"endWorking\": \"2022-10-04\"\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"identity\": \"G000200\",\n" +
                                    "      \"type\": \"GAS\",\n" +
                                    "      \"currentValue\": null,\n" +
                                    "      \"startWorking\": \"2022-09-01\",\n" +
                                    "      \"endWorking\": null\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}"
                            , false));
        }
    }

    @Nested
    class GetMeterHistory {
        @Test
        void meterHistory() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/meter/" + meter1.getId() + "/history"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[*]").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[*].created").value(Matchers.notNullValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[*].value").value(Matchers.containsInAnyOrder(1.15, 9.93)));
        }
    }

    @Nested
    class SubmitMeasure {
        @Test
        void notExistMeter_return404() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/meter/100500/history")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"value\": \"128.45\"}"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().string("Meter not found"));
        }

        @Test
        void incorrectMeasure_return400() throws Exception {
            for (String s : new String[]{
                    "{}",
                    "{\"value\": null}",
                    "{\"value\": \"null\"}",
                    "{\"value\": -125}",
                    "{\"value\": \"-125\"}",
                    "{\"value\": 0.00}",
                    "{\"value\": \"0.00\"}"
            }) {
                mockMvc.perform(MockMvcRequestBuilders.post("/meter/" + meter3.getId() + "/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content().string("Measurement must be positive number"));
            }
        }

        @Test
        void correctMeasure_return200() throws Exception {
            for (String s : new String[]{
                    "{\"value\": 125}",
                    "{\"value\": \"126.00\"}",
                    "{\"value\": 127.99}"
            }) {
                mockMvc.perform(MockMvcRequestBuilders.post("/meter/" + meter3.getId() + "/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk());
            }

            assertThat(measureHistoryRepository.findAll())
                    .anyMatch(measureHistory -> new BigDecimal("125").compareTo(measureHistory.getValue()) == 0 && meter3.getId().equals(measureHistory.getMeter().getId()))
                    .anyMatch(measureHistory -> new BigDecimal("126").compareTo(measureHistory.getValue()) == 0 && meter3.getId().equals(measureHistory.getMeter().getId()))
                    .anyMatch(measureHistory -> new BigDecimal("127.99").compareTo(measureHistory.getValue()) == 0 && meter3.getId().equals(measureHistory.getMeter().getId()));
        }

        @Test
        void lessMeasure_return400() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/meter/" + meter1.getId() + "/history")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"value\": \"9.00\"}"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string("Previous measurement is bigger than new"));
        }

        @Test
        void inactiveMeter_return400() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/meter/" + meter2.getId() + "/history")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"value\": \"9.00\"}"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string("Measurement work time isn't correspond with now"));
        }

        @Test
        void meterHistory() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/meter/" + meter1.getId() + "/history"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[*]").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[*].created").value(Matchers.notNullValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[*].value").value(Matchers.containsInAnyOrder(1.15, 9.93)));
        }
    }
}
