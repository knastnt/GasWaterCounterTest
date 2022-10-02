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
import ru.knastnt.gas_water_usage_app.repository.AccountRepository;
import ru.knastnt.gas_water_usage_app.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.*;

class WebControllerTest extends GasWaterUsageAppApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private final String ACCOUNT_NUM = "00001";

    @BeforeEach
    void init() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Иван")
                .middleName("Иванович")
                .lastName("Иванов")
                .build());
        accountRepository.save(Account.builder()
                .accountNum(ACCOUNT_NUM)
                .customer(customer)
                .build());
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
        void tst() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/account/" + ACCOUNT_NUM))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(""));
        }
    }
}
