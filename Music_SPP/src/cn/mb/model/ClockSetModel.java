package cn.mb.model;

public class ClockSetModel {

    private boolean lightOnOff;
    private String lightOnTime;
    private String lightOffTime;

    private boolean musicOnOff;
    private String musicOnTime;
    private String musicOffTime;

    private boolean fogOnOff;
    private String fogOnTime;
    private String fogOffTime;

    public boolean isLightOnOff() {
        return lightOnOff;
    }

    public void setLightOnOff(boolean lightOnOff) {
        this.lightOnOff = lightOnOff;
    }

    public String getLightOnTime() {
        return lightOnTime;
    }

    public void setLightOnTime(String lightOnTime) {
        this.lightOnTime = lightOnTime;
    }

    public String getLightOffTime() {
        return lightOffTime;
    }

    public void setLightOffTime(String lightOffTime) {
        this.lightOffTime = lightOffTime;
    }

    public boolean isMusicOnOff() {
        return musicOnOff;
    }

    public void setMusicOnOff(boolean musicOnOff) {
        this.musicOnOff = musicOnOff;
    }

    public String getMusicOnTime() {
        return musicOnTime;
    }

    public void setMusicOnTime(String musicOnTime) {
        this.musicOnTime = musicOnTime;
    }

    public String getMusicOffTime() {
        return musicOffTime;
    }

    public void setMusicOffTime(String musicOffTime) {
        this.musicOffTime = musicOffTime;
    }

    public boolean isFogOnOff() {
        return fogOnOff;
    }

    public void setFogOnOff(boolean fogOnOff) {
        this.fogOnOff = fogOnOff;
    }

    public String getFogOnTime() {
        return fogOnTime;
    }

    public void setFogOnTime(String fogOnTime) {
        this.fogOnTime = fogOnTime;
    }

    public String getFogOffTime() {
        return fogOffTime;
    }

    public void setFogOffTime(String fogOffTime) {
        this.fogOffTime = fogOffTime;
    }


}
