package com.ntukhpi.storozhuk.frame;

import com.ntukhpi.storozhuk.analyzer.LearnAnalyzer;
import com.ntukhpi.storozhuk.panel.AssociativeToReactivePanel;
import com.ntukhpi.storozhuk.panel.IdealSymbolsPanel;
import com.ntukhpi.storozhuk.panel.InputPanel;
import com.ntukhpi.storozhuk.panel.LearnPanel;
import com.ntukhpi.storozhuk.panel.SensoryToAssociativePanel;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

    private JTabbedPane tabs;
    private InputPanel inputPanel;
    private LearnPanel learnPanel;
    private IdealSymbolsPanel idealSymbolsPanel;
    private SensoryToAssociativePanel sensoryToAssociativePanel;
    private AssociativeToReactivePanel associativeToReactivePanel;
    private LearnAnalyzer learnAnalyzer;
    private NumbersInfo numbersInfo;

    public MainFrame() {
        super();
        tabs = new JTabbedPane();

        numbersInfo = new NumbersInfo(25, 8, 1);

        idealSymbolsPanel = new IdealSymbolsPanel(numbersInfo);
        learnAnalyzer = new LearnAnalyzer(numbersInfo);
        sensoryToAssociativePanel = new SensoryToAssociativePanel(learnAnalyzer, numbersInfo);
        associativeToReactivePanel = new AssociativeToReactivePanel(numbersInfo, learnAnalyzer);
        inputPanel = new InputPanel(learnAnalyzer, numbersInfo);
        learnPanel = new LearnPanel(numbersInfo, learnAnalyzer);

        numbersInfo.setAssociativeToReactivePanel(associativeToReactivePanel);

        tabs.add("Input", inputPanel);
        tabs.add("Learn", new JScrollPane(learnPanel));
        tabs.add("Ideal Symbols", idealSymbolsPanel);
        tabs.add("S->A", sensoryToAssociativePanel);

        tabs.add("A->R", associativeToReactivePanel);
        add(tabs);
        setTitle("Perceptron");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
