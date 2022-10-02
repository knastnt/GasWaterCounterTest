package ru.knastnt.gas_water_usage_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.knastnt.gas_water_usage_app.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
