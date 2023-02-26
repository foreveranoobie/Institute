package com.ntukhpi.storozhuk.magazine.processor;

import com.ntukhpi.storozhuk.Course;
import com.ntukhpi.storozhuk.magazine.Folding;
import com.ntukhpi.storozhuk.manager.ManagerTableElement;
import com.ntukhpi.storozhuk.manager.ManagerTableElementValue;
import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.rule.GrammarRuleWithEntry;
import com.ntukhpi.storozhuk.transition.TransitionElement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MagazineProcessor {

    public List<MagazineData> process(Course course, String input) {
        List<MagazineData> magazineDataList = new LinkedList<>();

        LinkedList<String> symbolsInput = new LinkedList<>(Arrays.asList(input.split("")));
        symbolsInput.add("#");

        LinkedList<GrammarEntry> currentMagazineData = new LinkedList<>();
        currentMagazineData.add(new GrammarEntry("h", 1));
        LinkedList<String> inputSymbolsCopy = new LinkedList<>(symbolsInput);
        for (int symbolIndex = 0; symbolIndex < symbolsInput.size(); ) {
            String inputSymbol = symbolsInput.get(symbolIndex);
            MagazineData currentMagazineStepData = new MagazineData();
            GrammarEntry lastMagazineSymbol = currentMagazineData.peekLast();
            ManagerTableElementValue tableElementValue = getManagerTableElementByRowAndColumn(lastMagazineSymbol,
                    inputSymbol, course.getManagerTableElements());

            //Writing current magazine step
            currentMagazineStepData.setInput(inputSymbolsCopy);
            currentMagazineStepData.setMagazineState(currentMagazineData);
            currentMagazineStepData.setMagazineOperation(tableElementValue.getMagazineOperation());

            //If managing table contains transfer operation for current row and column
            if ("T".equals(tableElementValue.getMagazineOperation().getOperationSymbol())) {
                //Moving to a new step
                currentMagazineData.add(getTransitionElementFromRowAndColumn(lastMagazineSymbol, inputSymbol,
                        course.getTransitionTable()));
                symbolIndex++;
                inputSymbolsCopy.pollFirst();
            } else if ("F".equals(tableElementValue.getMagazineOperation().getOperationSymbol())) {
                GrammarRuleWithEntry grammarRule =
                        ((Folding) tableElementValue.getMagazineOperation()).getRuleWithEntries();

                List<GrammarEntry> foldingElements = new LinkedList<>();
                Iterator<GrammarEntry> reverseMagazineIterator = currentMagazineData.descendingIterator();
                GrammarEntry toPutIntoMagazine = null;
                while (reverseMagazineIterator.hasNext()) {
                    toPutIntoMagazine = reverseMagazineIterator.next();
                    if (!grammarRule.getGrammarEntries().contains(toPutIntoMagazine)) {
                        break;
                    }
                    foldingElements.add(toPutIntoMagazine);
                }

                if (foldingElements.containsAll(grammarRule.getGrammarEntries())) {
                    int magazineCleanIndex = foldingElements.size();
                    while (magazineCleanIndex-- > 0) {
                        currentMagazineData.pollLast();
                    }
                }
                currentMagazineData.add(
                        getTransitionElementFromRowAndColumn(toPutIntoMagazine, grammarRule.getLeftSymbol(),
                                course.getTransitionTable()));
            } else if ("A".equals(tableElementValue.getMagazineOperation().getOperationSymbol())) {
                System.out.println("-----" + currentMagazineStepData);
                magazineDataList.add(currentMagazineStepData);
                System.out.println("Input entry processing finished!");
                return magazineDataList;
            }

            System.out.println("-----" + currentMagazineStepData);
            magazineDataList.add(currentMagazineStepData);
        }
        return magazineDataList;
    }

    private GrammarEntry getTransitionElementFromRowAndColumn(GrammarEntry row, String columnSymbol,
            List<TransitionElement> transitionElements) {
        TransitionElement transitionElement = transitionElements.stream().filter(transition ->
                transition.getGrammarEntry().equals(row)).findFirst().get();
        return transitionElement.getTransitions().values().stream()
                .filter(transitionElementEntry -> transitionElementEntry.getSymbol().equals(columnSymbol))
                .findFirst().orElse(null);
    }

    private ManagerTableElementValue getManagerTableElementByRowAndColumn(GrammarEntry row, String symbol,
            List<ManagerTableElement> managerTableElements) {
        ManagerTableElement tableElement = managerTableElements.stream()
                .filter(managerTableElement -> managerTableElement.getGrammarEntry().equals(row)).findFirst().get();
        return tableElement.getElementValues().stream().filter(elementValue -> elementValue.getSymbol().equals(symbol))
                .findAny().orElse(null);
    }

}
