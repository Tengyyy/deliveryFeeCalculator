package com.tengy.delivery_fee_calculator.controller;


import com.tengy.delivery_fee_calculator.model.Weather;
import com.tengy.delivery_fee_calculator.repository.WeatherRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.sql.Timestamp;

public class WeatherParser {

    public static void parseWeather(WeatherRepository weatherRepository, String url) throws Exception {
        Document document = loadDocument(url);
        loadWeatherData(weatherRepository, document);
    }

    private static Document loadDocument(String url) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new URL(url).openStream());
    }

    private static void loadWeatherData(WeatherRepository weatherRepository, Document document){


        Node observations = document.getElementsByTagName("observations").item(0);
        long time = Long.parseLong(observations.getAttributes().item(0).getNodeValue());
        Timestamp timestamp = new Timestamp(time * 1000);

        NodeList stations = document.getElementsByTagName("station");
        for(int i = 0; i < stations.getLength(); i++){
            Node node = stations.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;

                String name = element.getElementsByTagName("name").item(0).getTextContent();

                if(name.equals("Tallinn-Harku") || name.equals("Tartu-Tõravere") || name.equals("Pärnu")){
                    int wmoCode = Integer.parseInt(element.getElementsByTagName("wmocode").item(0).getTextContent());
                    float airTemp = Float.parseFloat(element.getElementsByTagName("airtemperature").item(0).getTextContent());
                    float windSpeed = Float.parseFloat(element.getElementsByTagName("windspeed").item(0).getTextContent());
                    String weatherPhenomenon = element.getElementsByTagName("phenomenon").item(0).getTextContent();

                    weatherRepository.save(new Weather(null, name, wmoCode, airTemp, windSpeed, weatherPhenomenon, timestamp));
                }
            }
        }
    }
}
