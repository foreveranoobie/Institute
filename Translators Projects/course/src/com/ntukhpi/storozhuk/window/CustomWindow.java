package com.ntukhpi.storozhuk.window;

import com.ntukhpi.storozhuk.Course;
import com.ntukhpi.storozhuk.magazine.processor.MagazineProcessor;
import com.ntukhpi.storozhuk.manager.ManagerTableElement;
import com.ntukhpi.storozhuk.manager.ManagerTableElementValue;
import com.ntukhpi.storozhuk.window.panels.GrammarPanel;
import com.ntukhpi.storozhuk.window.panels.MagazinePanel;
import com.ntukhpi.storozhuk.window.panels.ManagingTablePanel;
import com.ntukhpi.storozhuk.window.panels.TransitionTablePanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CustomWindow extends JFrame {

    private Course course;
    private CardLayout cardLayout;
    private GrammarPanel grammarPanel;
    private ManagingTablePanel managingTablePanel;
    private TransitionTablePanel transitionTablePanel;
    private MagazinePanel magazinePanel;
    private JPanel panel;
    private Container container;

    public CustomWindow(Course course) {
        this.course = course;
        setMaximumSize(new Dimension(1920, 1080));
        container = getContentPane();
        container.setMinimumSize(new Dimension(250, 100));
        grammarPanel = new GrammarPanel(course, this);
        updatePanels();
        cardLayout = new CardLayout();
        container.setLayout(cardLayout);
        setMenuItems();
        setTitle("LR(1) Recognizer");
        setLayout(new BorderLayout());
        panel = createPanel();
        add(BorderLayout.CENTER, new JScrollPane(panel));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout.minimumLayoutSize(this);
        cardLayout.preferredLayoutSize(this);
        cardLayout.maximumLayoutSize(this);
        cardLayout.show(panel, "panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setMenuItems() {
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        JMenuItem rules = new JMenuItem("Rules");
        rules.addActionListener(e -> {
            try {
                panel.remove(0);
            } catch (Exception ex) {
            }
            panel.add(grammarPanel);
            panel.updateUI();
        });
        viewMenu.add(rules);

        JMenuItem transitionTable = new JMenuItem("Transition table");
        transitionTable.addActionListener(e -> {
            try {
                panel.remove(0);
            } catch (Exception ex) {
            }
            panel.add(transitionTablePanel);
            panel.updateUI();
        });
        viewMenu.addSeparator();
        viewMenu.add(transitionTable);

        JMenuItem managingTable = new JMenuItem("Managing table");
        managingTable.addActionListener(e -> {
            try {
                panel.remove(0);
            } catch (Exception ex) {
            }
            panel.add(managingTablePanel);
            panel.updateUI();
        });
        viewMenu.addSeparator();
        viewMenu.add(managingTable);

        JMenuItem magazine = new JMenuItem("Magazine");
        magazine.addActionListener(e -> {
            try {
                panel.remove(0);
            } catch (Exception ex) {
            }
            panel.add(magazinePanel);
            panel.updateUI();
        });
        viewMenu.addSeparator();
        viewMenu.add(magazine);

        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(cardLayout);
        panel.setSize(1280, 1000);
        return panel;
    }

    public void updatePanels() {
        managingTablePanel = new ManagingTablePanel(course);
        transitionTablePanel = new TransitionTablePanel(course);
        magazinePanel = new MagazinePanel(course, new MagazineProcessor());
    }
}
