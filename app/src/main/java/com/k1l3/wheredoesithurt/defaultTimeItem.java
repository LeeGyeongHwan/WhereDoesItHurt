package com.k1l3.wheredoesithurt;

public class defaultTimeItem {
    private String AMPM,Time;

    public defaultTimeItem(){

    }
    public defaultTimeItem(String time){
        if(Integer.valueOf(time.substring(0,2))<12){
            AMPM="AM";
            Time=time.substring(0,2)+":"+time.substring(2,4);
        }
        else{
            AMPM="PM";
            Time="0"+String.valueOf(Integer.valueOf(time.substring(0,2))-12)+":"+time.substring(2,4);
        }
    }
    public String getAMPM() {
        return AMPM;
    }

    public void setAMPM(String AMPM) {
        this.AMPM = AMPM;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
