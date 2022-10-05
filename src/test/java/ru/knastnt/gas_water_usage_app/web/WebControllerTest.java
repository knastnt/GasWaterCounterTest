package ru.knastnt.gas_water_usage_app.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.knastnt.gas_water_usage_app.GasWaterUsageAppApplicationTests;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.model.Customer;
import ru.knastnt.gas_water_usage_app.model.meter.GasMeter;
import ru.knastnt.gas_water_usage_app.model.meter.WaterMeter;
import ru.knastnt.gas_water_usage_app.repository.AccountRepository;
import ru.knastnt.gas_water_usage_app.repository.CustomerRepository;
import ru.knastnt.gas_water_usage_app.repository.MeterRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;

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
    @PersistenceContext
    private EntityManager entityManager;

    private final String ACCOUNT_NUM = "00001";

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
        meterRepository.save(WaterMeter.builder()
                .typeOfWater(WaterMeter.TypeOfWater.HOT)
                .account(account)
                .identity("X000123")
                .startWorking(LocalDate.of(2022, 10, 5))
                .endWorking(null)
                .build());
        meterRepository.save(WaterMeter.builder()
                .typeOfWater(WaterMeter.TypeOfWater.HOT)
                .account(account)
                .identity("X000100")
                .startWorking(LocalDate.of(2021, 1, 1))
                .endWorking(LocalDate.of(2022, 10, 4))
                .build());
        meterRepository.save(GasMeter.builder()
                .account(account)
                .identity("G000200")
                .startWorking(LocalDate.of(2022, 9, 1))
                .endWorking(null)
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
                                    "      \"currentValue\": null,\n" +
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
}
