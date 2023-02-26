package com.ntukhpi.storozhuk.frame;

import com.ntukhpi.storozhuk.analyze.HopfieldAnalyzer;
import com.ntukhpi.storozhuk.panel.IdealSymbolsPanel;
import com.ntukhpi.storozhuk.panel.InputSymbolPanel;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

    private JTabbedPane tabs;
    private NumbersInfo numbersInfo;
    private HopfieldAnalyzer hopfieldAnalyzer;

    private IdealSymbolsPanel idealSymbolsPanel;
    private InputSymbolPanel inputSymbolPanel;

    public MainFrame() {
        super();
        tabs = new JTabbedPane();
        numbersInfo = new NumbersInfo(5, 5, 3);
        hopfieldAnalyzer = new HopfieldAnalyzer(numbersInfo);

        inputSymbolPanel = new InputSymbolPanel(numbersInfo, hopfieldAnalyzer);
        idealSymbolsPanel = new IdealSymbolsPanel(numbersInfo, hopfieldAnalyzer, inputSymbolPanel);

        tabs.add("Ideal symbols", new JScrollPane(idealSymbolsPanel));
        tabs.add("Input symbols", new JScrollPane(inputSymbolPanel));

        add(tabs);
        setTitle("Hopfield");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
