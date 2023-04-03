package com.tengy.delivery_fee_calculator.service;


import com.tengy.delivery_fee_calculator.model.Weather;
import com.tengy.delivery_fee_calculator.model.constants.City;
import com.tengy.delivery_fee_calculator.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Autowired
    WeatherRepository weatherRepository;

    private static final Map<City, Integer> wmoCodes = Map.ofEntries(
            Map.entry(City.TALLINN, 26038),
            Map.entry(City.TARTU, 26242),
            Map.entry(City.PARNU, 41803)
    );

    public List<Weather> getAllWeather(City city) {
        Integer wmoCode = wmoCodes.get(city);
        List<Weather> weatherList = new ArrayList<>();
        weatherRepository.findAll().forEach(item -> {
            if(item.wmoCode().equals(wmoCode)) weatherList.add(item);
        });

        return weatherList;
    }

    public Weather getMostRecentWeather(City city){
        List<Weather> weatherList = getAllWeather(city);
        if(weatherList.isEmpty()) return null;

        return weatherList.get(weatherList.size() - 1);
    }

    public Weather getClosestWeather(City city, Timestamp timestamp){
        List<Weather> weatherList = getAllWeather(city);
        if(weatherList.isEmpty()) return null;

        long time = timestamp.getTime();

        Weather closestWeather = null;
        long closestTimeDiff = Long.MAX_VALUE;

        // start searching from most recent as users are more likely to search for recent weather data
        for(int i = weatherList.size() - 1; i >= 0; i--){
            Weather weather = weatherList.get(i);
            long timeDiff = Math.abs(weather.observationTime().getTime() - time);
            if(timeDiff < closestTimeDiff){
                closestWeather = weather;
                closestTimeDiff = timeDiff;
            }
            else break;
        }

        return closestWeather;
    }

    public void clearWeather(){
        weatherRepository.deleteAll();
    }

    public void addWeather(Weather weather){
        weatherRepository.save(weather);
    }
}
