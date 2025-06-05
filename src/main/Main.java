package src.main;

import src.main.gui.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Inicia la aplicación mostrando la ventana principal
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true); // Lanza la ventana principal
        });
    }
}