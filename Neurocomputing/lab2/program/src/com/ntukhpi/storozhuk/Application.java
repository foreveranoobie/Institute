package com.ntukhpi.storozhuk;

import com.ntukhpi.storozhuk.frame.MainFrame;
import com.ntukhpi.storozhuk.panel.InputPanel;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

public class Application {
    public static void main(String[] args){
        MainFrame frame = new MainFrame();
        /*InputPanel inputPanel = new InputPanel();
        JFrame frame = new JFrame();
        frame.setTitle("Perceptron");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.CENTER, inputPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, new Button("txt"));
        //frame.pack();
        frame.setVisible(true);*/
    }
}
