package com.ntukhpi.storozhuk.panel;

import static javax.swing.SwingConstants.CENTER;

import com.ntukhpi.storozhuk.analyzer.LearnAnalyzer;
import com.ntukhpi.storozhuk.corrector.AlphaWeightCorrector;
import com.ntukhpi.storozhuk.corrector.GammaWeightCorrector;
import com.ntukhpi.storozhuk.corrector.WeightCorrector;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class InputPanel extends JPanel {

    private final NumbersInfo numbersInfo;

    private List<JLabel> labels = new ArrayList<>(25);
    private LearnAnalyzer learnAnalyzer;
    private BoxLayout boxLayout;
    private JPanel alphaCorrectionPanel;
    private JPanel answerPanel;
    private JLabel answerLabel;
    private JTextField correctionField;
    private Map<Boolean, WeightCorrector> weightCorrectorMap;
    List<Integer> associativeOutputBinaries;

    public InputPanel(LearnAnalyzer learnAnalyzer, NumbersInfo numbersInfo) {
        super();
        this.numbersInfo = numbersInfo;
        initWeightCorrectorMap();
        JPanel layoutedPanel = new JPanel();
        boxLayout = new BoxLayout(layoutedPanel, BoxLayout.Y_AXIS);
        layoutedPanel.setLayout(boxLayout);

        this.learnAnalyzer = learnAnalyzer;
        JPanel inputSymbolLayout = initInputSymbolLayout();
        JButton analyzeButton = initAnalyzeButton();
        JPanel checkBoxPanel = initLearnRadioButtonsPanel();
        this.alphaCorrectionPanel = initAlphaCorrectorStepPanel();
        alphaCorrectionPanel.setVisible(false);

        answerPanel = initResultPanel();

        inputSymbolLayout.add(Box.createVerticalGlue());
        layoutedPanel.add(inputSymbolLayout);
        layoutedPanel.add(checkBoxPanel);
        layoutedPanel.add(analyzeButton);
        layoutedPanel.add(alphaCorrectionPanel);
        layoutedPanel.add(answerPanel);

        add(layoutedPanel);
    }

    private JPanel initInputSymbolLayout() {
        JPanel inputSymbolPanel = new JPanel();
        inputSymbolPanel.setLayout(new GridBagLayout());
        GridBagConstraints horizontalConstraints = new GridBagConstraints();
        horizontalConstraints.fill = GridBagConstraints.HORIZONTAL;
        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                horizontalConstraints.gridx = c;
                horizontalConstraints.gridy = r;
                JLabel label = new JLabel();
                label.setOpaque(false);
                label.setBackground(Color.WHITE);
                label.setPreferredSize(new Dimension(25, 25));
                label.addMouseListener(getSymbolCellMouseListener(label));
                label.setVisible(true);
                label.setBorder(layoutBorder);
                label.setHorizontalAlignment(CENTER);
                inputSymbolPanel.add(label, horizontalConstraints);
                labels.add(label);
            }
        }
        inputSymbolPanel.setAlignmentX(CENTER_ALIGNMENT);
        return inputSymbolPanel;
    }

    private JButton initAnalyzeButton() {
        JButton analyzeButton = new JButton("Analyze symbol");
        analyzeButton.setPreferredSize(new Dimension(100, 25));
        analyzeButton.addActionListener(event -> {
            List<Integer> cellValues = labels.stream().map(label -> "X".equals(label.getText()) ? 1 : 0)
                    .collect(Collectors.toList());

            List<BigDecimal> associativeInputs = new ArrayList<>(learnAnalyzer.calculateAssociativeInputs(cellValues));

            BigDecimal theta = numbersInfo.getTheta();

            associativeOutputBinaries = associativeInputs.stream()
                    .map(associativeInput -> {
                        if (associativeInput.compareTo(theta) < 0) {
                            return 0;
                        } else {
                            return 1;
                        }
                    })
                    .collect(Collectors.toList());

            BigDecimal reactiveInput = learnAnalyzer.calculateReactiveInput(associativeOutputBinaries);
            String symbol;
            if (reactiveInput.compareTo(numbersInfo.getAverageReactiveTheta()) < 0) {
                symbol = numbersInfo.getNegativeReactiveSymbol();
                numbersInfo.setLastRecognizedSymbolPositive(false);
            } else {
                symbol = numbersInfo.getPositiveReactiveSymbol();
                numbersInfo.setLastRecognizedSymbolPositive(true);
            }
            answerLabel.setText(String.format("Answer is: %s?", symbol));
            answerPanel.setVisible(true);
        });
        analyzeButton.setAlignmentX(0.485f);
        return analyzeButton;
    }

    private JPanel initLearnRadioButtonsPanel() {
        JPanel checkBoxPanel = new JPanel();

        JRadioButton alphaRadioButton = new JRadioButton();
        alphaRadioButton.setText("Alpha Conf.");
        alphaRadioButton.setPreferredSize(new Dimension(120, 25));
        alphaRadioButton.addActionListener(event -> {
            numbersInfo.setAlphaConfirmationSelected(true);
            alphaCorrectionPanel.setVisible(true);
        });

        JRadioButton gammaRadioButton = new JRadioButton();
        gammaRadioButton.setText("Gamma Conf.");
        gammaRadioButton.setPreferredSize(new Dimension(120, 25));
        gammaRadioButton.addActionListener(event -> {
            alphaCorrectionPanel.setVisible(true);
            numbersInfo.setAlphaConfirmationSelected(false);
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(alphaRadioButton);
        buttonGroup.add(gammaRadioButton);

        checkBoxPanel.add(alphaRadioButton);
        checkBoxPanel.add(gammaRadioButton);

        checkBoxPanel.setAlignmentX(CENTER_ALIGNMENT);

        return checkBoxPanel;
    }

    private JPanel initAlphaCorrectorStepPanel() {
        JPanel correctorPanel = new JPanel();

        JLabel label = new JLabel();
        label.setText("Correction step");
        label.setPreferredSize(new Dimension(150, 25));

        correctionField = new JTextField();
        correctionField.setPreferredSize(new Dimension(60, 25));

        correctorPanel.add(label);
        correctorPanel.add(correctionField);

        return correctorPanel;
    }

    private JPanel initResultPanel() {
        JPanel correctorPanel = new JPanel();
        correctorPanel.setLayout(new GridBagLayout());

        answerLabel = new JLabel();
        answerLabel.setPreferredSize(new Dimension(120, 25));

        correctorPanel.add(answerLabel);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 1;

        JButton yesButton = new JButton();
        yesButton.setPreferredSize(new Dimension(65, 25));
        yesButton.setText("Yes");
        yesButton.addActionListener(event -> correctorPanel.setVisible(false));

        correctorPanel.add(yesButton, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        JButton noButton = new JButton();
        noButton.setPreferredSize(new Dimension(50, 25));
        noButton.setText("No");
        noButton.addActionListener(event -> {
            boolean isAlphaConfirmationSelected = numbersInfo.isAlphaConfirmationSelected();
            numbersInfo.setCorrectionStep(BigDecimal.valueOf(Float.parseFloat(correctionField.getText())));
            weightCorrectorMap.get(isAlphaConfirmationSelected)
                    .correctReactiveInputWeights(numbersInfo.getReactiveWeights(), associativeOutputBinaries,
                            numbersInfo.isLastRecognizedSymbolPositive());
            correctorPanel.setVisible(false);
        });

        correctorPanel.add(noButton, gridBagConstraints);

        correctorPanel.setVisible(false);

        return correctorPanel;
    }

    private void initWeightCorrectorMap() {
        weightCorrectorMap = new HashMap<>();
        weightCorrectorMap.put(true, new AlphaWeightCorrector(numbersInfo));
        weightCorrectorMap.put(false, new GammaWeightCorrector(numbersInfo));
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
}
