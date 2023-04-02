package com.tengy.delivery_fee_calculator.controller;

import com.tengy.delivery_fee_calculator.repository.WeatherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherController {

    private final WeatherRepository repository;

    public WeatherController(WeatherRepository weatherRepository){
        this.repository = weatherRepository;
    }


    // run every 60 minutes, 15 mins after full hour
    @Scheduled(cron = "${weatherJob.cron}")
    public void weatherJob() throws Exception {
        WeatherParser.parseWeather(repository, "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
    }

    @Bean
    CommandLineRunner commandLineRunner(){
        return args -> WeatherParser.parseWeather(repository, "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
    }


}
