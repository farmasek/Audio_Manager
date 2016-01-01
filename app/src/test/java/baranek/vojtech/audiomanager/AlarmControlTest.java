package baranek.vojtech.audiomanager;

import org.junit.Test;

import java.util.Calendar;

import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmControl;

import static org.junit.Assert.*;

/**
 * Created by Farmas on 22.12.2015.
 */
public class AlarmControlTest {

    @Test
    public void testConvertTimeToReceiveToString() throws Exception {

   /*     long input = 55200000;
        String exp = "Další změna profilu za: " + 15 +":"+ 20;
        String actual = AlarmControl.convertTimeToReceiveToString(input);
        assertEquals(exp,actual);*/


    }

    @Test
    public void testNextDayStringFromTimetoReceive() throws Exception {

        long receiverTime = 1451495100000L;

        String exp = "Další změna v St 18:05";
        String actual = AlarmControl.getStringForNextDayFromReceiveTime(receiverTime);
        assertEquals(exp,actual);


    }
}