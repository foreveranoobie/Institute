package com.ntukhpi.storozhuk.frame;

import com.ntukhpi.storozhuk.analyzer.HammingAnalyzer;
import com.ntukhpi.storozhuk.analyzer.MaxnetAnalyzer;
import com.ntukhpi.storozhuk.panel.IdealSymbolsPanel;
import com.ntukhpi.storozhuk.panel.InputSymbolPanel;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

    private JTabbedPane tabs;
    private NumbersInfo numbersInfo;
    private HammingAnalyzer hammingAnalyzer;
    private MaxnetAnalyzer maxnetAnalyzer;

    public MainFrame() {
        super();
        tabs = new JTabbedPane();

        numbersInfo = new NumbersInfo(5, 5, 6,
                BigDecimal.valueOf(0.2), BigDecimal.valueOf(25));
        hammingAnalyzer = new HammingAnalyzer(numbersInfo);
        maxnetAnalyzer = new MaxnetAnalyzer(numbersInfo);

        IdealSymbolsPanel idealSymbolsPanel = new IdealSymbolsPanel(numbersInfo, hammingAnalyzer);
        InputSymbolPanel inputSymbolPanel = new InputSymbolPanel(numbersInfo, hammingAnalyzer, maxnetAnalyzer);
        tabs.add("Ideal symbols", new JScrollPane(idealSymbolsPanel));
        tabs.add("Input symbol", new JScrollPane(inputSymbolPanel));
        add(tabs);
        setTitle("Hamming");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
