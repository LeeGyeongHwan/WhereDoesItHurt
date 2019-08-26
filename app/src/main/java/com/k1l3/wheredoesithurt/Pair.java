package com.k1l3.wheredoesithurt;

public class Pair {
    private String kind;
    private int caution;

    public Pair(String kind, int caution){
        this.kind = kind;
        this.caution = caution;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getCaution() {
        return caution;
    }

    public void setCaution(int caution) {
        this.caution = caution;
    }
}
