package src.main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import src.main.modelo.Pregunta;
import src.main.util.LectorArchivo;
import java.io.File;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
    private JButton btnIniciarPrueba;
    private JLabel lblAsignatura; // Etiqueta para mostrar la asignatura
    private List<Pregunta> preguntas;
    private String asignatura = "";

    public VentanaPrincipal() {
        setTitle("Sistema de Pruebas - Taxonomía de Bloom");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Etiqueta de asignatura (vacía al inicio)
        lblAsignatura = new JLabel("Asignatura: ");
        lblAsignatura.setFont(new Font("Arial", Font.BOLD, 16));
        lblAsignatura.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(lblAsignatura);

        // Botón para cargar el archivo de preguntas
        JButton btnCargarArchivo = new JButton("Cargar archivo de ítemes");
        btnCargarArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCargarArchivo.addActionListener(e -> cargarArchivo());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(btnCargarArchivo);

        // Botón para iniciar la prueba (deshabilitado hasta que se cargue un archivo)
        btnIniciarPrueba = new JButton("Iniciar prueba");
        btnIniciarPrueba.setEnabled(false);
        btnIniciarPrueba.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciarPrueba.addActionListener(e -> iniciarPrueba());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(btnIniciarPrueba);
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                preguntas = LectorArchivo.leerPreguntas(archivoSeleccionado);
                asignatura = obtenerAsignatura(archivoSeleccionado);
                if (preguntas == null || preguntas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se encontraron preguntas en el archivo.");
                    btnIniciarPrueba.setEnabled(false);
                    lblAsignatura.setText("Asignatura: ");
                } else {
                    lblAsignatura.setText("Asignatura: " + asignatura);
                    JOptionPane.showMessageDialog(this, "Preguntas cargadas: " + preguntas.size());
                    btnIniciarPrueba.setEnabled(true);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage());
                lblAsignatura.setText("Asignatura: ");
            }
        }
    }

    // Lee la asignatura desde la primera línea del archivo txt
    private String obtenerAsignatura(File archivo) throws IOException {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("[ASIGNATURA]")) {
                    return linea.replace("[ASIGNATURA]", "").trim();
                }
            }
        }
        return "";
    }

    private void iniciarPrueba() {
        if (preguntas != null && !preguntas.isEmpty()) {
            new VentanaPrueba(preguntas).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No hay preguntas cargadas.");
        }
    }
}
