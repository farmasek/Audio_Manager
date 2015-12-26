package baranek.vojtech.audiomanager;

import org.junit.Test;

import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmControl;

import static org.junit.Assert.*;

/**
 * Created by Farmas on 22.12.2015.
 */
public class AlarmControlTest {

    @Test
    public void testConvertTimeToReceiveToString() throws Exception {

        long input = 55200000;
        String exp = "Další změna profilu za: " + 15 +":"+ 20;
        String actual = AlarmControl.convertTimeToReceiveToString(input);
        assertEquals(exp,actual);


    }
}