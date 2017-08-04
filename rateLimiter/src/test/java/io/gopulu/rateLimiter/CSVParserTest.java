package io.gopulu.rateLimiter;


import io.gopulu.rateLimiter.domain.Hotel;
import io.gopulu.rateLimiter.domain.CSVParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CSVParserTest {

    @Test
    public void parseFileToHotel(){
        String csv = "hoteldb.csv";

        CSVParser csvParser = new CSVParser();

        List<Hotel> list = csvParser.parseLinesToHotels(csv);
        Assert.assertEquals(26,list.size());
    }
}
