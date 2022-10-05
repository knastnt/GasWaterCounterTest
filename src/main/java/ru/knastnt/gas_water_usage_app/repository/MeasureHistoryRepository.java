package ru.knastnt.gas_water_usage_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.knastnt.gas_water_usage_app.model.MeasureHistory;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasureHistoryRepository extends CrudRepository<MeasureHistory, Long> {
    Optional<MeasureHistory> findFirstByMeterOrderByIdDesc(AbstractMeter meter);
    List<MeasureHistory> findByMeterIdOrderByCreatedDesc(Long meterId);
}
