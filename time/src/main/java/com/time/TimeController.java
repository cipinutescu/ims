package com.time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

/**
 * Created by Ciprian on 1/6/2018.
 */
@RestController
public class TimeController {

    public String timeFromNTP;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/syncTime")
    public @ResponseBody String syncTime() throws IOException {
        String TIME_SERVER = "time-a.nist.gov";
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        long returnTime = timeInfo.getReturnTime();
        Date time = new Date(returnTime);
        timeFromNTP = time.toString();
        return time.toString();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getTimestamp")
    public @ResponseBody String getTime() throws IOException{
        if(timeFromNTP == null){
            return syncTime();
        } else {
            return timeFromNTP;
        }
    }


}
