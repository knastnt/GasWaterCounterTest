package ru.knastnt.gas_water_usage_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.knastnt.gas_water_usage_app.model.meter.AbstractMeter;

@Repository
public interface MeterRepository extends CrudRepository<AbstractMeter, Long> {
}
