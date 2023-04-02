package com.tengy.delivery_fee_calculator;

import com.tengy.delivery_fee_calculator.controller.DeliveryFeeController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebMvcTest(DeliveryFeeController.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String tallinnCarTestExpectedResult = "{\"totalFee\":4.0,\"regionalBaseFee\":4.0,\"airTemperatureExtraFee\":0.0,\"windSpeedExtraFee\":0.0,\"weatherPhenomenonExtraFee\":0.0,\"currency\":\"euro\"}";

    @Test
    public void tallinnCarTest() throws Exception {
        this.mockMvc.perform(get("/api/delivery_fee?city=tallinn&vehicle_type=car"))
                .andExpect(status().isOk())
                .andExpect(content().json(tallinnCarTestExpectedResult));
    }
}
