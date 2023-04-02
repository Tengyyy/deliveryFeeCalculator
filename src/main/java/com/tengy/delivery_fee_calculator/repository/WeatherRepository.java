package com.tengy.delivery_fee_calculator.repository;

import com.tengy.delivery_fee_calculator.model.Weather;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<Weather, Integer> {
}
