package com.tengy.delivery_fee_calculator.model;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

public record Weather(@Id Integer id, String stationName, Integer wmoCode, Float airTemp, Float windSpeed, String weatherPhenomenon, Timestamp observationTime) {
}
