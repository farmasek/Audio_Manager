package baranek.vojtech.audiomanager.model;

/**
 * Created by Farmas on 22.11.2015.
 */
public class TimerProfileHelper {

    public static String getFormatedStartTime(TimerProfile timerProfile) {
        String formatedTime;

        formatedTime = _getTimeString(timerProfile.getZacCas() / 60, timerProfile.getZacCas() % 60);

        return formatedTime;
    }

    private static String _getTimeString(int hod, int min) {
        return hod + ":" + getZeroBeforMinute(min);
    }

    public static String getFormatedEndTime(TimerProfile timerProfile) {
        String formatedTime;
        int endTime = timerProfile.getZacCas() + timerProfile.getCasDoKonce();

        if (endTime > (1440)) {
            endTime = endTime - 1440;

            formatedTime = "Další den " + _getTimeString(endTime / 60, endTime % 60);
        } else {
            formatedTime = _getTimeString(endTime / 60, endTime % 60);
        }
        return formatedTime;
    }

    public static String getZeroBeforMinute(int min) {

        String s;
        if (min < 10)
            s = "0" + min;
        else
            s = Integer.toString(min);
        return s;
    }


    public static int getCasFromHodMin(int konHod, int konMin) {
        return konHod * 60 + konMin;
    }

    public static int getCalculatedTimeToEnd(int zacCas, int lEndTime) {

        int calculatedEndTime;

        if (lEndTime >= zacCas) {
            calculatedEndTime = lEndTime - zacCas;
        } else {
            calculatedEndTime = 24 * 60 + (lEndTime - zacCas);
        }

        return calculatedEndTime;
    }

    public static int getLastTimeToEnd(TimerProfile timerProfile) {
        int iLastTimeToEnd;

        iLastTimeToEnd = timerProfile.getCasDoKonce() + timerProfile.getZacCas();

        if (iLastTimeToEnd > 1440) {
            iLastTimeToEnd = iLastTimeToEnd - 1440;
        }


        return iLastTimeToEnd;
    }


}
