package com.ntukhpi.storozhuk.panel;

import static com.ntukhpi.storozhuk.util.MathUtil.DEFAULT_BIG_DECIMAL_ROUND;
import static com.ntukhpi.storozhuk.util.StringUtil.U_INPUT_PREFIX;
import static javax.swing.SwingConstants.CENTER;

import com.ntukhpi.storozhuk.analyzer.LearnAnalyzer;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class LearnPanel extends JPanel {

    private NumbersInfo numbersInfo;
    private LearnAnalyzer learnAnalyzer;

    private JButton learnButton;
    private List<JLabel> firstSymbolLabels = new ArrayList<>();
    private List<JLabel> secondSymbolLabels = new ArrayList<>();

    private List<JLabel> firstSymbolAssociativeInputLabels;
    private List<JLabel> secondSymbolAssociativeInputLabels;

    private List<BigDecimal> firstSymbolAssociativeInputValues;
    private List<BigDecimal> secondSymbolAssociativeInputValues;

    private List<JLabel> reactiveInputLabels = new ArrayList<>(3);
    private List<BigDecimal> reactiveInputValues = new ArrayList<>(2);

    private GridBagLayout mainLayout;

    public LearnPanel(NumbersInfo numbersInfo, LearnAnalyzer learnAnalyzer) {
        super();
        mainLayout = new GridBagLayout();
        this.numbersInfo = numbersInfo;
        this.learnAnalyzer = learnAnalyzer;
        initIdealSymbolsLayout();
        initLearnButton();
        initAssociativeInputs();
        addThetaInputWindow();
        addReactiveInputsTable();
    }

    private void initIdealSymbolsLayout() {
        setLayout(mainLayout);
        GridBagConstraints horizontalConstraints = new GridBagConstraints();
        horizontalConstraints.fill = GridBagConstraints.NONE;
        horizontalConstraints.gridy = 0;
        horizontalConstraints.gridx = 0;
        JPanel symbolsPanel = new JPanel();
        symbolsPanel.add(initFirstSymbolLayout(horizontalConstraints));
        symbolsPanel.add(initSecondSymbolLayout(horizontalConstraints));
        mainLayout.setConstraints(symbolsPanel, horizontalConstraints);
        add(symbolsPanel);
    }

    private void printAssociativeInputs(List<BigDecimal> firstSymbolInputs, List<BigDecimal> secondSymbolInputs) {
        int inputIndex = 0;
        for (JLabel inputLabel : firstSymbolAssociativeInputLabels) {
            inputLabel.setText(firstSymbolInputs.get(inputIndex++).round(DEFAULT_BIG_DECIMAL_ROUND).toString());
        }
        inputIndex = 0;
        for (JLabel inputLabel : secondSymbolAssociativeInputLabels) {
            inputLabel.setText(secondSymbolInputs.get(inputIndex++).round(DEFAULT_BIG_DECIMAL_ROUND).toString());
        }
    }

    public MouseListener getSymbolCellMouseListener(JLabel label) {
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

    private JPanel initFirstSymbolLayout(GridBagConstraints bagConstraints) {
        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("-1");
        descriptionLabel.setHorizontalAlignment(CENTER);

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);

        JPanel firstLabelPanel = new JPanel();
        firstLabelPanel.setLayout(new GridBagLayout());
        bagConstraints.gridx = 2;
        firstLabelPanel.add(descriptionLabel, bagConstraints);

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                bagConstraints.gridx = c;
                bagConstraints.gridy = r + 1;
                JLabel label = new JLabel();
                label.setOpaque(false);
                label.setBackground(Color.WHITE);
                label.setPreferredSize(new Dimension(25, 25));
                label.addMouseListener(getSymbolCellMouseListener(label));
                label.setVisible(true);
                label.setBorder(layoutBorder);
                label.setHorizontalAlignment(CENTER);
                add(label, bagConstraints);
                firstSymbolLabels.add(label);
                firstLabelPanel.add(label, bagConstraints);
            }
        }
        return firstLabelPanel;
    }

    private JPanel initSecondSymbolLayout(GridBagConstraints bagConstraints) {
        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("+1");
        descriptionLabel.setHorizontalAlignment(CENTER);

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        JPanel secondLabelPanel = new JPanel();
        secondLabelPanel.setLayout(new GridBagLayout());
        bagConstraints.gridx = 2;
        bagConstraints.gridy = 0;
        secondLabelPanel.add(descriptionLabel, bagConstraints);

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                bagConstraints.gridx = c;
                bagConstraints.gridy = r + 1;
                JLabel label = new JLabel();
                label.setOpaque(false);
                label.setBackground(Color.WHITE);
                label.setPreferredSize(new Dimension(25, 25));
                label.addMouseListener(getSymbolCellMouseListener(label));
                label.setVisible(true);
                label.setBorder(layoutBorder);
                label.setHorizontalAlignment(CENTER);
                secondSymbolLabels.add(label);
                secondLabelPanel.add(label, bagConstraints);
            }
        }
        GridBagConstraints secondLabelConstraint = new GridBagConstraints();
        secondLabelConstraint.insets = new Insets(0, 10, 0, 0);
        return secondLabelPanel;
    }

    private void initLearnButton() {
        JPanel buttonPanel = new JPanel();
        learnButton = new JButton();
        learnButton.setText("Learn");
        learnButton.addActionListener(event -> {
            List<Integer> firstCellValues = firstSymbolLabels.stream().map(label -> "X".equals(label.getText()) ? 1 : 0)
                    .collect(Collectors.toList());
            List<BigDecimal> sensoryToAssociativeFirst = learnAnalyzer.calculateAssociativeInputs(firstCellValues);
            firstSymbolAssociativeInputValues = new ArrayList<>(sensoryToAssociativeFirst);

            List<Integer> secondCellValues = secondSymbolLabels.stream().map(label ->
                    "X".equals(label.getText()) ? 1 : 0).collect(Collectors.toList());
            List<BigDecimal> sensoryToAssociativeSecond = learnAnalyzer.calculateAssociativeInputs(secondCellValues);
            secondSymbolAssociativeInputValues = new ArrayList<>(sensoryToAssociativeSecond);

            printAssociativeInputs(sensoryToAssociativeFirst, sensoryToAssociativeSecond);
        });
        GridBagConstraints horizontalConstraints = new GridBagConstraints();
        horizontalConstraints.fill = GridBagConstraints.HORIZONTAL;
        horizontalConstraints.gridx = 0;
        horizontalConstraints.gridy = 1;
        horizontalConstraints.gridwidth = 0;
        horizontalConstraints.gridheight = 0;
        horizontalConstraints.insets = new Insets(180, 0, 0, 0);
        buttonPanel.add(learnButton);
        mainLayout.setConstraints(buttonPanel, horizontalConstraints);
        add(buttonPanel);
    }

    private void initAssociativeInputs() {
        firstSymbolAssociativeInputLabels = new ArrayList<>(numbersInfo.getAssociativeNeuronsNumber());
        secondSymbolAssociativeInputLabels = new ArrayList<>(numbersInfo.getAssociativeNeuronsNumber());
        JPanel associativeTablePanel = new JPanel();
        associativeTablePanel.setLayout(new GridBagLayout());

        GridBagConstraints inPanelConstraint = new GridBagConstraints();

        Dimension preferredLabelSize = new Dimension(50, 22);

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        JLabel emptyLabel = new JLabel();
        emptyLabel.setBorder(layoutBorder);
        emptyLabel.setPreferredSize(preferredLabelSize);
        associativeTablePanel.add(emptyLabel, inPanelConstraint);

        for (int i = 1; i < numbersInfo.getAssociativeNeuronsNumber() + 1; i++) {
            inPanelConstraint.gridx = i;
            JLabel inputLabel = new JLabel();
            inputLabel.setBorder(layoutBorder);
            inputLabel.setPreferredSize(preferredLabelSize);
            inputLabel.setHorizontalAlignment(0);
            String labelText = String.format("UinA%d", i);
            inputLabel.setText(labelText);
            associativeTablePanel.add(inputLabel, inPanelConstraint);
        }

        emptyLabel = new JLabel();
        emptyLabel.setBorder(layoutBorder);
        emptyLabel.setPreferredSize(preferredLabelSize);
        emptyLabel.setText("-1");
        inPanelConstraint.gridx = 0;
        inPanelConstraint.gridy = 1;
        associativeTablePanel.add(emptyLabel, inPanelConstraint);
        addEmptyLabels(associativeTablePanel, numbersInfo.getAssociativeNeuronsNumber(),
                inPanelConstraint, 1, 1, preferredLabelSize, firstSymbolAssociativeInputLabels);
        emptyLabel = new JLabel();
        emptyLabel.setBorder(layoutBorder);
        emptyLabel.setText("+1");
        emptyLabel.setPreferredSize(preferredLabelSize);
        inPanelConstraint.gridx = 0;
        inPanelConstraint.gridy = 2;
        associativeTablePanel.add(emptyLabel, inPanelConstraint);
        addEmptyLabels(associativeTablePanel, numbersInfo.getAssociativeNeuronsNumber(), inPanelConstraint, 1, 2,
                preferredLabelSize,
                secondSymbolAssociativeInputLabels);

        GridBagConstraints horizontalConstraints = new GridBagConstraints();
        horizontalConstraints.gridy = 2;
        horizontalConstraints.gridx = 0;
        horizontalConstraints.gridwidth = 0;
        horizontalConstraints.gridheight = 0;
        horizontalConstraints.insets = new Insets(300, 0, 0, 0);
        mainLayout.setConstraints(associativeTablePanel, horizontalConstraints);
        add(associativeTablePanel);
    }

    private void addThetaInputWindow() {
        GridBagConstraints thetaPanelConstraint = new GridBagConstraints();
        thetaPanelConstraint.gridy = 3;
        thetaPanelConstraint.gridx = 0;
        thetaPanelConstraint.gridwidth = 0;
        thetaPanelConstraint.gridheight = 0;
        thetaPanelConstraint.insets = new Insets(425, 0, 0, 0);

        JPanel thetaPanel = new JPanel();
        thetaPanel.setLayout(new GridBagLayout());

        JLabel text = new JLabel("Theta");
        text.setPreferredSize(new Dimension(35, 25));
        thetaPanel.add(text);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, 25));
        thetaPanel.add(textField);

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(100, 25));
        button.setText("Set Theta");
        button.addActionListener(
                event -> {
                    BigDecimal newTheta = BigDecimal.valueOf(Float.parseFloat(textField.getText()));
                    numbersInfo.setTheta(newTheta);
                });
        thetaPanel.add(button);

        mainLayout.setConstraints(thetaPanel, thetaPanelConstraint);
        add(thetaPanel);
    }

    private void addReactiveInputsTable() {
        JPanel reactiveInputsTablePanel = new JPanel();
        reactiveInputsTablePanel.setLayout(new GridBagLayout());

        Dimension preferredLabelSize = new Dimension(60, 22);

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);

        GridBagConstraints inputTableConstraint = new GridBagConstraints();
        JLabel rowNameLabel = new JLabel();
        rowNameLabel.setText(String.format("%s%s", U_INPUT_PREFIX, "inR(-1)"));
        rowNameLabel.setHorizontalAlignment(CENTER);
        rowNameLabel.setBorder(layoutBorder);
        rowNameLabel.setPreferredSize(preferredLabelSize);
        reactiveInputsTablePanel.add(rowNameLabel, inputTableConstraint);

        inputTableConstraint.gridx = 1;
        rowNameLabel = new JLabel();
        rowNameLabel.setText(String.format("%s%s", U_INPUT_PREFIX, "inR(+1)"));
        rowNameLabel.setHorizontalAlignment(CENTER);
        rowNameLabel.setBorder(layoutBorder);
        rowNameLabel.setPreferredSize(preferredLabelSize);
        reactiveInputsTablePanel.add(rowNameLabel, inputTableConstraint);

        inputTableConstraint.gridx = 2;
        rowNameLabel = new JLabel();
        rowNameLabel.setText("Theta");
        rowNameLabel.setHorizontalAlignment(CENTER);
        rowNameLabel.setBorder(layoutBorder);
        rowNameLabel.setPreferredSize(preferredLabelSize);
        reactiveInputsTablePanel.add(rowNameLabel, inputTableConstraint);

        inputTableConstraint.gridy = 1;
        for (int i = 0; i < 3; i++) {
            inputTableConstraint.gridx = i;
            JLabel label = new JLabel();
            label.setHorizontalAlignment(CENTER);
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            reactiveInputsTablePanel.add(label, inputTableConstraint);
            reactiveInputLabels.add(label);
        }

        JButton calculateReactiveInputsButton = new JButton();
        calculateReactiveInputsButton.setText("Calculate Rin");
        calculateReactiveInputsButton.setPreferredSize(new Dimension(150, 30));
        calculateReactiveInputsButton.addActionListener(event -> {
            BigDecimal theta = numbersInfo.getTheta();
            List<Integer> associativeOutputBinariesFirstSymbol =
                    firstSymbolAssociativeInputValues.stream()
                            .map(inputValue -> inputValue.compareTo(theta) >= 0 ? 1 : 0)
                            .collect(Collectors.toList());
            BigDecimal firstReactiveInput = learnAnalyzer.calculateReactiveInput(associativeOutputBinariesFirstSymbol);
            reactiveInputLabels.get(0).setText(firstReactiveInput.round(DEFAULT_BIG_DECIMAL_ROUND).toString());

            List<Integer> associativeOutputBinariesSecondSymbol =
                    secondSymbolAssociativeInputValues.stream()
                            .map(inputValue -> inputValue.compareTo(theta) >= 0 ? 1 : 0)
                            .collect(Collectors.toList());
            BigDecimal secondReactiveInput = learnAnalyzer.calculateReactiveInput(
                    associativeOutputBinariesSecondSymbol);
            reactiveInputLabels.get(1).setText(secondReactiveInput.round(DEFAULT_BIG_DECIMAL_ROUND).toString());

            BigDecimal average = firstReactiveInput.add(secondReactiveInput).divide(BigDecimal.valueOf(2));
            reactiveInputLabels.get(2).setText(average.round(DEFAULT_BIG_DECIMAL_ROUND).toString());
            numbersInfo.setAverageReactiveTheta(average);
        });
        inputTableConstraint.gridy = 2;
        inputTableConstraint.gridx = 0;
        inputTableConstraint.gridheight = 0;
        inputTableConstraint.gridwidth = 0;
        reactiveInputsTablePanel.add(calculateReactiveInputsButton, inputTableConstraint);

        GridBagConstraints reactiveInputsTablePanelConstraint = new GridBagConstraints();
        reactiveInputsTablePanelConstraint.gridy = 4;
        reactiveInputsTablePanelConstraint.gridx = 0;
        reactiveInputsTablePanelConstraint.gridwidth = 0;
        reactiveInputsTablePanelConstraint.gridheight = 0;
        reactiveInputsTablePanelConstraint.insets = new Insets(530, 0, 0, 0);
        add(reactiveInputsTablePanel, reactiveInputsTablePanelConstraint);
    }

    private void addEmptyLabels(JPanel toAddPanel, int count, GridBagConstraints constraints, int basicGridX,
            int basicGridY, Dimension preferredSize, List<JLabel> toAddLabelsList) {
        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        for (int i = 0; i < count; i++) {
            JLabel label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredSize);
            constraints.gridx = basicGridX + i;
            constraints.gridy = basicGridY;
            toAddPanel.add(label, constraints);
            toAddLabelsList.add(label);
        }
    }
}
