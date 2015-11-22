package baranek.vojtech.audiomanager;

/**
 * Created by Farmas on 08.11.2015.
 */
public class TimerProfile {


    private int id;

    private int zacCas;
    private int casDoKonce;
    private int zacRez;
    private int konRez;
    private int zacMedia, zacVyzvaneni, zacAlarm, konMedia, konVyzvaneni, konAlarm;
    private boolean konZap;
    private String dny;
    private String nazev;
    private int position;

    public TimerProfile(int id, String nazev, int zacCas, int casDoKonce, int zacRez, int konRez, int zacMedia, int zacVyzvaneni, int zacAlarm, int konMedia, int konVyzvaneni, int konAlarm, boolean konZap, String dny) {
        this.id = id;
        this.zacCas = zacCas;
        this.casDoKonce = casDoKonce;
        this.zacRez = zacRez;
        this.konRez = konRez;
        this.zacMedia = zacMedia;
        this.zacVyzvaneni = zacVyzvaneni;
        this.zacAlarm = zacAlarm;
        this.konMedia = konMedia;
        this.konVyzvaneni = konVyzvaneni;
        this.konAlarm = konAlarm;
        this.konZap = konZap;
        this.dny = dny;
        this.nazev = nazev;
        this.position = position;
    }

    public TimerProfile() {
    }

    public TimerProfile(TimerProfile t) {
        this.id = t.getId();
        this.nazev = t.getNazev();

    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public int getZacVyzvaneni() {
        return zacVyzvaneni;
    }

    public void setZacVyzvaneni(int zacVyzvaneni) {
        this.zacVyzvaneni = zacVyzvaneni;
    }

    public String getDny() {
        return dny;
    }

    public void setDny(String dny) {
        this.dny = dny;
    }

    public boolean isKonZap() {
        return konZap;
    }

    public void setKonZap(boolean konZap) {
        this.konZap = konZap;
    }

    public int getKonAlarm() {
        return konAlarm;
    }

    public void setKonAlarm(int konAlarm) {
        this.konAlarm = konAlarm;
    }

    public int getKonVyzvaneni() {
        return konVyzvaneni;
    }

    public void setKonVyzvaneni(int konVyzvaneni) {
        this.konVyzvaneni = konVyzvaneni;
    }

    public int getZacAlarm() {
        return zacAlarm;
    }

    public void setZacAlarm(int zacAlarm) {
        this.zacAlarm = zacAlarm;
    }

    public int getKonMedia() {
        return konMedia;
    }

    public void setKonMedia(int konMedia) {
        this.konMedia = konMedia;
    }

    public int getZacMedia() {
        return zacMedia;
    }

    public void setZacMedia(int zacMedia) {
        this.zacMedia = zacMedia;
    }

    public int getCasDoKonce() {
        return casDoKonce;
    }

    public void setCasDoKonce(int casDoKonce) {
        this.casDoKonce = casDoKonce;
    }


//    public int getKonMin(){ return getCasDoKonce()%60;  }
//    public int getKonHod(){ return getCasDoKonce()/60;  }


//    public int getZacMin(){ return getZacCas()%60;  }
//    public int getZacHod(){ return getZacCas()/60;  }

    public int getZacCas() {
        return zacCas;
    }

    public void setZacCas(int zacCas) {
        this.zacCas = zacCas;
    }

    public int getZacRez() {
        return zacRez;
    }

    public void setZacRez(int zacRez) {
        this.zacRez = zacRez;
    }

    public int getKonRez() {
        return konRez;
    }

    public void setKonRez(int konRez) {
        this.konRez = konRez;
    }



}



