package com.mycompany.doornal;

import javax.swing.SwingUtilities;

public class Doornal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}