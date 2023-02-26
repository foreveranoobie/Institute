package com.ntukhpi.storozhuk.panel;

import static java.util.Optional.of;
import static javax.swing.SwingConstants.CENTER;

import com.ntukhpi.storozhuk.analyze.HopfieldAnalyzer;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableModel;

public class IdealSymbolsPanel extends JPanel {

    private final NumbersInfo numbersInfo;
    private final HopfieldAnalyzer hopfieldAnalyzer;
    private final InputSymbolPanel inputSymbolPanel;

    private List<List<JLabel>> idealLabels;
    private List<JLabel> relationWeights;
    private JLabel thetaValue;

    private JTable symbolNamesTable;
    private String[] columnNames = {"#1", "#2", "#3"};
    private Object[][] rowData = new Object[1][3];

    public IdealSymbolsPanel(NumbersInfo numbersInfo, HopfieldAnalyzer hopfieldAnalyzer,
            InputSymbolPanel inputSymbolPanel) {
        super();
        this.numbersInfo = numbersInfo;
        this.hopfieldAnalyzer = hopfieldAnalyzer;
        this.inputSymbolPanel = inputSymbolPanel;

        JPanel submainPanel = new JPanel();

        BoxLayout panelLayout = new BoxLayout(submainPanel, BoxLayout.Y_AXIS);
        submainPanel.setLayout(panelLayout);

        submainPanel.add(initIdealSymbolsTable());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initSymbolLabels());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initAnalyzeSymbolsButton());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initRelationWeightsPanel());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initThetaPanel());

        add(submainPanel);
    }

    private JComponent initIdealSymbolsTable() {
        symbolNamesTable = new JTable(rowData, columnNames);
        symbolNamesTable.setShowHorizontalLines(true);
        symbolNamesTable.setShowVerticalLines(true);
        symbolNamesTable.setPreferredSize(new Dimension(100, 38));
        symbolNamesTable.setMaximumSize(new Dimension(100, 38));
        JScrollPane scrollPane = new JScrollPane(symbolNamesTable);
        scrollPane.setPreferredSize(new Dimension(100, 38));
        scrollPane.setMaximumSize(new Dimension(100, 38));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private void initRowNames() {
        TableModel tableModel = symbolNamesTable.getModel();
        tableModel.setValueAt("Symbol", 0, 0);
    }

    private Component initThetaPanel() {
        thetaValue = new JLabel();
        thetaValue.setPreferredSize(new Dimension(100, 25));
        return thetaValue;
    }

    private JPanel initSymbolLabels() {
        final JPanel panel = new JPanel();

        idealLabels = new ArrayList<>(numbersInfo.getTotalSymbolsCount());

        List<JPanel> symbolPanels = new ArrayList<>(numbersInfo.getTotalSymbolsCount());
        for (int i = 0; i < numbersInfo.getTotalSymbolsCount(); i++) {
            JPanel singleSymbolPanel = initSingleSymbolLabel(i);
            symbolPanels.add(singleSymbolPanel);
            panel.add(singleSymbolPanel);
        }

        return panel;
    }

    private JPanel initSingleSymbolLabel(int panelNumber) {
        Dimension preferredLabelSize = new Dimension(25, 25);

        JPanel symbolPanel = new JPanel();
        symbolPanel.setLayout(new GridBagLayout());

        GridBagConstraints horizontalConstraints = new GridBagConstraints();
        horizontalConstraints.gridx = 2;

        JLabel symbolTitle = new JLabel();
        symbolTitle.setHorizontalAlignment(CENTER);
        symbolTitle.setText(String.format("S%d", panelNumber));
        symbolTitle.setPreferredSize(preferredLabelSize);
        symbolPanel.add(symbolTitle, horizontalConstraints);

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);

        List<JLabel> labels = new ArrayList<>(numbersInfo.getColumnsCount() * numbersInfo.getRowsCount());
        for (int r = 0; r < numbersInfo.getRowsCount(); r++) {
            for (int c = 0; c < numbersInfo.getColumnsCount(); c++) {
                horizontalConstraints.gridx = c;
                horizontalConstraints.gridy = r + 1;
                JLabel label = new JLabel();
                label.setOpaque(false);
                label.setBackground(Color.WHITE);
                label.setPreferredSize(preferredLabelSize);
                label.addMouseListener(getSymbolCellMouseListener(label));
                label.setVisible(true);
                label.setBorder(layoutBorder);
                label.setHorizontalAlignment(CENTER);
                symbolPanel.add(label, horizontalConstraints);
                labels.add(label);
            }
        }
        idealLabels.add(labels);
        return symbolPanel;
    }

    private JButton initAnalyzeSymbolsButton() {
        JButton analyzeButton = new JButton();
        analyzeButton.setText("Analyze");
        analyzeButton.setPreferredSize(new Dimension(85, 25));
        analyzeButton.addActionListener(event -> {
            List<List<Integer>> bipolarValues = new ArrayList<>(numbersInfo.getTotalSymbolsCount());
            for (List<JLabel> symbolLabels : idealLabels) {
                bipolarValues.add(symbolLabels.stream().map(label ->
                                "X".equals(label.getText()) ? 1 : -1)
                        .collect(Collectors.toList()));
            }
            numbersInfo.setIdealSymbolsBipolarValues(bipolarValues);

            hopfieldAnalyzer.calculateRelationWeights();
            updateWeights();

            hopfieldAnalyzer.calculateTheta();
            thetaValue.setText(String.format("Theta = %s", numbersInfo.getTheta()));

            numbersInfo.setIdealSymbolNames(
                    Arrays.stream(rowData[0]).map(Object::toString).collect(Collectors.toList()));

            //inputSymbolPanel.updateThetaRadioButtons();
        });

        return analyzeButton;
    }

    private JPanel initRelationWeightsPanel() {
        relationWeights = new ArrayList<>((numbersInfo.getRowsCount() * numbersInfo.getColumnsCount())
                * (numbersInfo.getRowsCount() * numbersInfo.getColumnsCount()));

        JPanel relationWeightsPanel = new JPanel();
        relationWeightsPanel.setLayout(new GridBagLayout());

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(45, 25);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel emptyLabel = new JLabel();
        emptyLabel.setText("Num");
        emptyLabel.setPreferredSize(preferredLabelSize);
        emptyLabel.setBorder(layoutBorder);
        relationWeightsPanel.add(emptyLabel, gridBagConstraints);

        for (int i = 1; i < numbersInfo.getColumnsCount() * numbersInfo.getRowsCount() + 1; i++) {
            JLabel headerLabel = new JLabel();
            headerLabel.setText(String.valueOf(i));
            headerLabel.setPreferredSize(preferredLabelSize);
            headerLabel.setBorder(layoutBorder);
            headerLabel.setHorizontalAlignment(CENTER);

            gridBagConstraints.gridx = i;
            relationWeightsPanel.add(headerLabel, gridBagConstraints);
        }

        gridBagConstraints.gridx = 0;
        for (int i = 1; i < numbersInfo.getColumnsCount() * numbersInfo.getRowsCount() + 1; i++) {
            JLabel headerLabel = new JLabel();
            headerLabel.setText(String.valueOf(i));
            headerLabel.setPreferredSize(preferredLabelSize);
            headerLabel.setBorder(layoutBorder);
            headerLabel.setHorizontalAlignment(CENTER);

            gridBagConstraints.gridy = i;
            relationWeightsPanel.add(headerLabel, gridBagConstraints);
        }

        for (int r = 0; r < numbersInfo.getColumnsCount() * numbersInfo.getRowsCount(); r++) {
            gridBagConstraints.gridy = r + 1;
            JLabel label;

            for (int c = 1; c < numbersInfo.getColumnsCount() * numbersInfo.getRowsCount() + 1; c++) {
                label = new JLabel();
                label.setBorder(layoutBorder);
                label.setPreferredSize(preferredLabelSize);
                gridBagConstraints.gridx = c;
                relationWeightsPanel.add(label, gridBagConstraints);
                relationWeights.add(label);
            }
        }
        return relationWeightsPanel;
    }

    private void updateWeights() {
        int index = 0;
        List<BigDecimal> weightValues = numbersInfo.getRelationWeights();
        for (JLabel weightLabel : relationWeights) {
            weightLabel.setText(weightValues.get(index++).toString());
        }
    }

    private MouseListener getSymbolCellMouseListener(JLabel label) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String labelText = label.getText();
                if (labelText == null || labelText.isEmpty()) {
                    label.setText("X");
                } else {
                    label.setText(null);
                }
            }
        };
    }
}
