package com.tengy.delivery_fee_calculator.repository;

import com.tengy.delivery_fee_calculator.model.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Integer> {
}
