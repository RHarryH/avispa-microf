package com.avispa.microf;

import com.avispa.microf.gui.MainPanel;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class MicroF {

    public static void main( String[] args ) {
        JFrame frame = new JFrame("Avispa μF");
        frame.setContentPane(new MainPanel().getPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
