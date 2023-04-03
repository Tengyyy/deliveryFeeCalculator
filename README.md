# Delivery Fee Calculator

API usage: 
 * Provide city and vehicle type and get delivery fee based on most recent weather data in that city:\
```/api/delivery_fee?city={city}&vehicle_type={vehicle_type}```

 * Additionally provide a timestamp and get delivery fee based on closest weather data to that time:\
```/api/delivery_fee?city={city}&vehicle_type={vehicle_type}&time={yyyy-mm-dd hh:mm:ss}```\
\
Accepted city values: Tallinn, Tartu, Pärnu/Parnu\
Accepted vehicle_type values: car, scooter, bike
