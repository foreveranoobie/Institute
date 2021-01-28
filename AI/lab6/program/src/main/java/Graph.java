import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Graph extends JFrame {
    private Map<Float, Float> minFuncValues;
    private Map<Float, Float> funcValuesOnY;
    private float maxValue;
    private int width = 700;
    private int height = 400;

    public Graph(Map<Float, Float> minFuncValues) {
        super("Population Adaptation");
        this.minFuncValues = new LinkedHashMap<>(minFuncValues);
        setSize(700, 405);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setAxisY();
    }

    private void setAxisY() {
        funcValuesOnY = new HashMap<>();
        float current;
        for (float X : minFuncValues.keySet()) {
            current = minFuncValues.get(X);
            if (current > maxValue) {
                maxValue = current;
            }
        }
        funcValuesOnY.put(maxValue, 50f);
        for (float X : minFuncValues.keySet()) {
            if (minFuncValues.get(X) != maxValue) {
                current = minFuncValues.get(X);
                funcValuesOnY.put(current, 375f - (325f / (maxValue / current)));
            }
        }
    }

    void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(50, 40, 50, 375);
        float Y;
        for (float func : funcValuesOnY.keySet()) {
            Y = funcValuesOnY.get(func);
            g2d.drawString(String.valueOf(func), 10, Y);
        }
        g2d.drawLine(50, 375, 665, 375);
        float previousLineX = 50;
        float previousLineY = 375;
        float currentX;
        float currentY;
        float sizeOfMinFunc = minFuncValues.size();
        int index = 1;
        float current;
        for (float X : minFuncValues.keySet()) {
            current = minFuncValues.get(X);
            g2d.drawString(String.valueOf(index), 650f / (sizeOfMinFunc / index), 390);
            currentX = 650f / (sizeOfMinFunc / index);
            if (current != maxValue) {
                currentY = 375f - (325f / (maxValue / current));
                if (currentY < 50) {
                    currentY = 50;
                }
            } else {
                currentY = 50f;
            }
            g2d.draw(new Line2D.Float(previousLineX, previousLineY,
                    currentX, currentY));
            g2d.drawString(String.valueOf(index), currentX, 390);
            if (currentY + 10 > 375) {
                g2d.drawString(String.valueOf(current), currentX, currentY - 15);
            } else {
                g2d.drawString(String.valueOf(current), currentX, currentY + 10);
            }
            previousLineX = currentX;
            previousLineY = currentY;
            index++;
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }
}
