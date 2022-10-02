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

import static org.junit.jupiter.api.Assertions.*;

class WebControllerTest extends GasWaterUsageAppApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {

    }

    @Nested
    class GetAccInfo {
        @Test
        void tst() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/account"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(""));
        }
    }
}
