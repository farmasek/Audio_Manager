package baranek.vojtech.audiomanager.model;

import android.support.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Farmas on 08.11.2015.
 */
public class TimerProfile extends RealmObject{


    @PrimaryKey
    private int id;

    private int zacCas;
    private int casDoKonce;
    private int zacRez;
    private int konRez;
    private int zacMedia, zacVyzvaneni, zacAlarm,zacOzn,
            konOzn, konMedia, konVyzvaneni, konAlarm;
    private boolean konZap;
    private String dny;
    private String nazev;

    private boolean isTimerZap;



    public TimerProfile(int id, String nazev, int zacCas, int casDoKonce, int zacRez, int konRez,
                        int zacMedia, int zacVyzvaneni, int zacAlarm, int konMedia, int konVyzvaneni, int konAlarm,
                        int zacOzn, int konOzn, boolean konZap, String dny, boolean isZap) {
        this.id = id;
        this.zacCas = zacCas;
        this.casDoKonce = casDoKonce;
        this.zacRez = zacRez;
        this.konRez = konRez;
        this.zacMedia = zacMedia;
        this.zacVyzvaneni = zacVyzvaneni;
        this.zacAlarm = zacAlarm;
        this.konMedia = konMedia;
        this.zacOzn = zacOzn;
        this.konOzn=konOzn;
        this.konVyzvaneni = konVyzvaneni;
        this.konAlarm = konAlarm;
        this.konZap = konZap;
        this.dny = dny;
        this.nazev = nazev;
        this.isTimerZap = isZap;

    }

    public TimerProfile() {
    }

    public TimerProfile(TimerProfile t) {
        this.id = t.getId();
        this.zacCas = t.getZacCas();
        this.casDoKonce = t.getCasDoKonce();
        this.zacRez = t.getZacRez();
        this.konRez = t.getKonRez();
        this.zacMedia = t.getZacMedia();
        this.zacVyzvaneni = t.getZacVyzvaneni();
        this.zacAlarm = t.getZacAlarm();
        this.konMedia = t.getKonMedia();
        this.zacOzn = t.getZacOzn();
        this.konOzn=t.getKonOzn();
        this.konVyzvaneni = t.getKonVyzvaneni();
        this.konAlarm = t.getKonAlarm();
        this.konZap = t.isKonZap();
        this.dny = t.getDny();
        this.nazev = t.getNazev();
        this.isTimerZap = t.isTimerZap();
    }

    public boolean isTimerZap() {
        return isTimerZap;
    }

    public void setIsTimerZap(boolean isTimerZap) {
        this.isTimerZap = isTimerZap;
    }

    public int getZacOzn() {
        return zacOzn;
    }

    public void setZacOzn(int zacOzn) {
        this.zacOzn = zacOzn;
    }

    public int getKonOzn() {
        return konOzn;
    }

    public void setKonOzn(int konOzn) {
        this.konOzn = konOzn;
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



