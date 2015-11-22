package baranek.vojtech.audiomanager;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Farmas on 22.11.2015.
 */
public class TimerProfileHelperTest {

    @Test
    public void testGetFormatedStartTime() throws Exception {
        TimerProfile timerProfile = new TimerProfile();
        timerProfile.setZacCas(TimerProfileHelper.getCasFromHodMin(15, 2));
        String actualString = TimerProfileHelper.getFormatedStartTime(timerProfile);
        String expectedString = "15:02";
        assertThat(actualString, equalTo(expectedString));


    }

    @Test
    public void testGetFormatedEndTime() throws Exception {
        TimerProfile timerProfile = new TimerProfile();
        timerProfile.setZacCas(TimerProfileHelper.getCasFromHodMin(15, 2));
        timerProfile.setCasDoKonce(120);
        String actualString = TimerProfileHelper.getFormatedEndTime(timerProfile);
        String expectedString = "17:02";
        assertThat(actualString, equalTo(expectedString));
    }

    @Test
    public void testGetFormatedEndTimeNextDay() throws Exception {
        TimerProfile timerProfile = new TimerProfile();
        timerProfile.setZacCas(TimerProfileHelper.getCasFromHodMin(15, 2));
        timerProfile.setCasDoKonce(1439);
        String actualString = TimerProfileHelper.getFormatedEndTime(timerProfile);
        String expectedString = "Další den 15:01";
        assertThat(actualString, equalTo(expectedString));
    }


    @Test
    public void testGetZeroBeforMinute() throws Exception {
        String aa = "05";
        int min = 5;
        assertThat(aa, is(equalTo(TimerProfileHelper.getZeroBeforMinute(min))));
    }

    @Test
    public void testGetZeroBeforMinuteArrays() throws Exception {
        String[] expectedValues = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
        int[] inputValues = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String[] actualValues = new String[expectedValues.length];

        for (int i = 0; i < expectedValues.length; i++) {

            actualValues[i] = TimerProfileHelper.getZeroBeforMinute(inputValues[i]);

        }
        assertArrayEquals(expectedValues, actualValues);

    }

    @Test
    public void testGetCasFromHodMin() throws Exception {
        int expectedValue = 80;
        int actualValue = TimerProfileHelper.getCasFromHodMin(1, 20);
        assertEquals(expectedValue, actualValue);

    }

    @Test
    public void testGetCalculatedEndTime() throws Exception {
        int expectedTimeToEnd = 10;
        int calculatedTimeToEnd = TimerProfileHelper.getCalculatedTimeToEnd(60, 70);
        assertEquals(expectedTimeToEnd, calculatedTimeToEnd);

        expectedTimeToEnd = 1439;
        calculatedTimeToEnd = TimerProfileHelper.getCalculatedTimeToEnd(7, 6);
        assertEquals(expectedTimeToEnd, calculatedTimeToEnd);


        expectedTimeToEnd = 0;
        calculatedTimeToEnd = TimerProfileHelper.getCalculatedTimeToEnd(10, 10);
        assertEquals(expectedTimeToEnd, calculatedTimeToEnd);

    }

    @Test
    public void testGetLastEndTime() throws Exception {

        TimerProfile timerProfile = new TimerProfile();
        timerProfile.setZacCas(15);
        timerProfile.setCasDoKonce(5);

        int expectedTimeToEnd = 20;
        int calculatedTimeToENd = TimerProfileHelper.getLastTimeToEnd(timerProfile);

        assertEquals(expectedTimeToEnd, calculatedTimeToENd);

        timerProfile.setZacCas(15);
        timerProfile.setCasDoKonce(1435);

        expectedTimeToEnd = 10;
        calculatedTimeToENd = TimerProfileHelper.getLastTimeToEnd(timerProfile);

        assertEquals(expectedTimeToEnd, calculatedTimeToENd);

        timerProfile.setZacCas(1000);
        timerProfile.setCasDoKonce(500);

        expectedTimeToEnd = 60;
        calculatedTimeToENd = TimerProfileHelper.getLastTimeToEnd(timerProfile);

        assertEquals(expectedTimeToEnd, calculatedTimeToENd);

    }

}