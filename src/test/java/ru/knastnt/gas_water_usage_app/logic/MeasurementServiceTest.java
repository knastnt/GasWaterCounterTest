package ru.knastnt.gas_water_usage_app.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.knastnt.gas_water_usage_app.exceptions.IncorrectMeasurementException;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementServiceTest {
    MeasurementService measurementService = new MeasurementServiceImpl();

    @BeforeEach
    void init() {
        meter = new AbstractMeter() {
            public void AbstractMeter() {
                scoreBoardIntegerNumCount = new BigDecimal("999.99");
                identity = "T1000";
            }
        };
    }

    @Nested
    class submitMeasure {
        @Test
        void submitMeasureIncorrectValueThrowEx() {
            assertThrows(IncorrectMeasurementException.class, () -> meter.submitMeasure(null));
            assertThrows(IncorrectMeasurementException.class, () -> meter.submitMeasure(BigDecimal.valueOf(-100)));
        }
        @Test
        void submitMeasure() {
            meter.submitMeasure(BigDecimal.valueOf(100));
            assertThat(meter.getRealValue()).isEqualByComparingTo(BigDecimal.valueOf(100));
            assertThat(meter.getScoreBoardValue()).isEqualByComparingTo(BigDecimal.valueOf(100));
        }
        @Test
        void submitMeasureAfterBarrier() {
            meter.submitMeasure(BigDecimal.valueOf(1010));
            assertThat(meter.getRealValue()).isEqualByComparingTo(BigDecimal.valueOf(1010));
            assertThat(meter.getScoreBoardValue()).isEqualByComparingTo(BigDecimal.valueOf(10));
        }
        @Test
        void submitMeasureAfterBarrierTwice() {
            meter.submitMeasure(BigDecimal.valueOf(2005));
            assertThat(meter.getRealValue()).isEqualByComparingTo(BigDecimal.valueOf(2010));
            assertThat(meter.getScoreBoardValue()).isEqualByComparingTo(BigDecimal.valueOf(50));
        }
    }
}
