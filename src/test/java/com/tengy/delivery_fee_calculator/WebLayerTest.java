package com.tengy.delivery_fee_calculator;

import com.tengy.delivery_fee_calculator.model.Weather;
import com.tengy.delivery_fee_calculator.repository.WeatherRepository;
import com.tengy.delivery_fee_calculator.service.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(WeatherService.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherService weatherService;

    @Test
    public void tallinnCarTest() throws Exception {

        initializeRepository();
        String expectedResult = "{\"totalFee\":4.0,\"regionalBaseFee\":4.0,\"airTemperatureExtraFee\":0.0,\"windSpeedExtraFee\":0.0,\"weatherPhenomenonExtraFee\":0.0,\"currency\":\"euro\"}";

        this.mockMvc.perform(get("/api/delivery_fee?city=tallinn&vehicle_type=car"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void tartuScooterTest() throws Exception {

        initializeRepository();

        String expectedResult = "{\"totalFee\":3.0,\"regionalBaseFee\":3.0,\"airTemperatureExtraFee\":0.0,\"windSpeedExtraFee\":0.0,\"weatherPhenomenonExtraFee\":0.0,\"currency\":\"euro\"}";

        this.mockMvc.perform(get("/api/delivery_fee?city=tartu&vehicle_type=scooter"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void parnuBikeTest() throws Exception {
        initializeRepository();


        String expectedResult = "{\"totalFee\":2.0,\"regionalBaseFee\":2.0,\"airTemperatureExtraFee\":0.0,\"windSpeedExtraFee\":0.0,\"weatherPhenomenonExtraFee\":0.0,\"currency\":\"euro\"}";

        this.mockMvc.perform(get("/api/delivery_fee?city=parnu&vehicle_type=bike"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void tartuLowAirTempBikeTest() throws Exception {

        initializeRepository();

        String expectedResult = "{\"totalFee\":3.5,\"regionalBaseFee\":2.5,\"airTemperatureExtraFee\":1.0,\"windSpeedExtraFee\":0.0,\"weatherPhenomenonExtraFee\":0.0,\"currency\":\"euro\"}";

        this.mockMvc.perform(get("/api/delivery_fee?city=tartu&vehicle_type=bike&time=2020-01-01 00:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void thunderstormBikeTest() throws Exception {
        initializeRepository();


        String expectedResult = "Usage of selected vehicle type is forbidden";

        this.mockMvc.perform(get("/api/delivery_fee?city=tallinn&vehicle_type=bike&time=2019-01-01 00:00:00"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void tooHighWindSpeedTest() throws Exception {

        initializeRepository();


        String expectedResult = "Usage of selected vehicle type is forbidden";

        this.mockMvc.perform(get("/api/delivery_fee?city=tallinn&vehicle_type=scooter&time=2018-01-01 00:00:00"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void lowAirTempHighWindSpeedSnowTest() throws Exception {
        initializeRepository();

        String expectedResult = "{\"totalFee\":5.5,\"regionalBaseFee\":3.0,\"airTemperatureExtraFee\":1.0,\"windSpeedExtraFee\":0.5,\"weatherPhenomenonExtraFee\":1.0,\"currency\":\"euro\"}";

        this.mockMvc.perform(get("/api/delivery_fee?city=tartu&vehicle_type=scooter&time=2017-01-01 00:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    private void initializeRepository(){
        weatherService.clearWeather();
        weatherService.addWeather(new Weather(null, "Tartu-Tõravere", 26242, -20F, 15F, "Light snow shower", Timestamp.valueOf("2017-01-01 00:00:00")));
        weatherService.addWeather(new Weather(null, "Tallinn-Harku", 26038, 0F, 30F, "Clear", Timestamp.valueOf("2018-01-01 00:00:00")));
        weatherService.addWeather(new Weather(null, "Tallinn-Harku", 26038, 0F, 0F, "Thunderstorm", Timestamp.valueOf("2019-01-01 00:00:00")));
        weatherService.addWeather(new Weather(null, "Tartu-Tõravere", 26242, -20F, 0F, "Clear", Timestamp.valueOf("2020-01-01 00:00:00")));
        weatherService.addWeather(new Weather(null, "Tallinn-Harku", 26038, 10F, 0F, "Clear", Timestamp.valueOf("2023-04-02 00:00:00")));
        weatherService.addWeather(new Weather(null, "Tartu-Tõravere", 26242, 10F, 0F, "Clear", Timestamp.valueOf("2023-04-02 00:00:00")));
        weatherService.addWeather(new Weather(null, "Pärnu", 41803, 10F, 0F, "", Timestamp.valueOf("2023-04-02 00:00:00")));
    }
}
