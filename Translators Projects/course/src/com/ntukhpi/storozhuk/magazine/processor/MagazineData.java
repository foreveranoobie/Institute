package com.ntukhpi.storozhuk.magazine.processor;

import com.ntukhpi.storozhuk.magazine.MagazineOperation;
import com.ntukhpi.storozhuk.rule.GrammarEntry;
import java.util.LinkedList;
import java.util.List;

public class MagazineData {
    private List<String> input;
    private List<GrammarEntry> magazineState;
    private MagazineOperation magazineOperation;

    public MagazineData(){
        input = new LinkedList<>();
        magazineState = new LinkedList<>();
    }

    public MagazineData(List<String> input, List<GrammarEntry> magazineState, MagazineOperation magazineOperation){
        this.input = new LinkedList<>(input);
        this.magazineState = new LinkedList<>(magazineState);
        this.magazineOperation = magazineOperation;
    }

    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = new LinkedList<>(input);
    }

    public List<GrammarEntry> getMagazineState() {
        return magazineState;
    }

    public void setMagazineState(List<GrammarEntry> magazineState) {
        this.magazineState = new LinkedList<>(magazineState);
    }

    public MagazineOperation getMagazineOperation() {
        return magazineOperation;
    }

    public void setMagazineOperation(MagazineOperation magazineOperation) {
        this.magazineOperation = magazineOperation;
    }

    @Override
    public String toString() {
        return "MagazineData{" +
                "input=" + input +
                ", magazineState=" + magazineState +
                ", magazineOperation=" + magazineOperation +
                '}';
    }
}
