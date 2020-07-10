package com.company;
import com.company.edit.Frame;

import javax.swing.*;

public class Program {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               new Frame().setVisible(true);
            }
        });
    }
}
