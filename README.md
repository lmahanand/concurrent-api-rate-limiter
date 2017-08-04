# concurrent-api-rate-limiter

After having cloned the app, the app could be started by either running mvn spring-boot:run under /rateLimiter or by directly running the class RateLimiterApplication.java.

It will load all hotel data to cache from hoteldb.csv file. And it will also load api keys from apikeys.csv file.

Acccess points

1. curl http://localhost:8080/hotels/1 -H "AUTHORIZED-API-KEY:abcXYZkeyAPI-4"  -X GET

Above rest url will give hotel details of city id 1.

2. URL to access price of the hotels in ascending order of price

curl http://localhost:8080/hotels/1/price/sort/asc -H "AUTHORIZED-API-KEY:abcXYZkeyAPI-0"  -X GET

3. URL to access price of the hotels in descending order of price

curl http://localhost:8080/hotels/1/price/sort/desc -H "AUTHORIZED-API-KEY:abcXYZkeyAPI-0"  -X GET


At the moment it has loaded 4 api keys:

API-KEY,RATE-LIMIT

abcXYZkeyAPI-0,1

abcXYZkeyAPI-1

abcXYZkeyAPI-2,1

abcXYZkeyAPI-4,2
