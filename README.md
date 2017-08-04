# concurrent-api-rate-limiter

After having cloned the app, the app could be started by running mvn spring-boot:run under /rateLimiter .

It will load all hotel data to cache from hoteldb.csv file. And it will also load api keys from apikeys.csv file.

Acccess points

1. curl http://localhost:8080/hotels/1 -H "AUTHORIZED-API-KEY:abcXYZkeyAPI-4"  -X GET

Above rest url will give hotel details of city id 1.

2. URL to access price of the hotels in ascending order of price

http://localhost:8080/hotels/1/price/sort/asc -H "AUTHORIZED-API-KEY:abcXYZkeyAPI-0"  -X GET

3. URL to access price of the hotels in descending order of price

http://localhost:8080/hotels/1/price/sort/desc -H "AUTHORIZED-API-KEY:abcXYZkeyAPI-0"  -X GET
