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

# Apache Benchmark metrics

Total Number of requests : 1000
-
Number of concurrent requests : 100
-

Below is the command to get AB metrics
-
ab -c 100 -n 1000 -H 'AUTHORIZED-API-KEY:abcXYZkeyAPI-4' -v 1 localhost:8080/hotels/1/price/sort/asc


# Metrics



Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /hotels/1/price/sort/asc
Document Length:        198 bytes

Concurrency Level:      100

Time taken for tests:   1.470 seconds
-
Complete requests:      1000
-
Failed requests:        0
-
Non-2xx responses:      1000
-
Requests per second:    680.40 [#/sec] (mean)
-
Time per request:       146.973 [ms] (mean)
-
Time per request:       1.470 [ms] (mean, across all concurrent requests)
-
Transfer rate:          234.70 [Kbytes/sec] received
-


Percentage of the requests served within a certain time (ms)
-
  50%    142
  
  66%    165
  
  75%    181
  
  80%    189
  
  90%    194
  
  95%    197
  
  98%    210
  
  99%    221
  
 100%    351 (longest request)
 

