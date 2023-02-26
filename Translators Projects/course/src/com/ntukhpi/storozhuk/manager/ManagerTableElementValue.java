package com.ntukhpi.storozhuk.manager;

import com.ntukhpi.storozhuk.magazine.MagazineOperation;

public class ManagerTableElementValue {

    private String symbol;
    private MagazineOperation magazineOperation;

    public ManagerTableElementValue() {
    }

    public ManagerTableElementValue(String symbol, MagazineOperation magazineOperation) {
        this.symbol = symbol;
        this.magazineOperation = magazineOperation;
    }

    public MagazineOperation getMagazineOperation() {
        return magazineOperation;
    }

    public void setMagazineOperation(MagazineOperation magazineOperation) {
        this.magazineOperation = magazineOperation;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
