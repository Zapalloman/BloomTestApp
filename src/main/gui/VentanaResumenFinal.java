package src.main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import src.main.modelo.Pregunta;

// Ventana que muestra el detalle de respuestas y estadísticas por nivel Bloom
public class VentanaResumenFinal extends JFrame {
    public VentanaResumenFinal(List<Pregunta> preguntas, List<String> respuestasUsuario) {
        setTitle("Resumen Final");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // Pestaña de detalle de respuestas
        JTextArea areaDetalle = new JTextArea(generarDetalle(preguntas, respuestasUsuario));
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollDetalle = new JScrollPane(areaDetalle);
        tabs.addTab("Detalle de respuestas", scrollDetalle);

        // Pestaña de estadísticas por Bloom
        JTextArea areaEstadisticas = new JTextArea(generarEstadisticas(preguntas, respuestasUsuario));
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollEstadisticas = new JScrollPane(areaEstadisticas);
        tabs.addTab("Estadísticas por Bloom", scrollEstadisticas);

        add(tabs, BorderLayout.CENTER);
    }

    // Genera el texto del detalle de respuestas
    private String generarDetalle(List<Pregunta> preguntas, List<String> respuestasUsuario) {
        StringBuilder detalle = new StringBuilder();
        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String respUsuario = respuestasUsuario.get(i);
            boolean esCorrecta = respUsuario != null && !respUsuario.isEmpty() && respUsuario.equalsIgnoreCase(p.getRespuestaCorrecta());
            detalle.append("Pregunta ").append(i + 1).append(": ");
            if (esCorrecta) {
                detalle.append("✔ Correcta");
            } else {
                detalle.append("✘ Incorrecta");
            }
            detalle.append(" | Tu respuesta: ").append(
                respUsuario == null || respUsuario.isEmpty() ? "Sin responder" : respUsuario
            );
            if (!esCorrecta) {
                detalle.append(" | Correcta: ").append(p.getRespuestaCorrecta());
            }
            detalle.append("\n");
        }
        return detalle.toString();
    }

    // Genera el texto de las estadísticas por nivel Bloom
    private String generarEstadisticas(List<Pregunta> preguntas, List<String> respuestasUsuario) {
        Map<String, int[]> estadisticas = new LinkedHashMap<>();
        String[] tiposBloom = {"Crear", "Evaluar", "Analizar", "Aplicar", "Entender", "Recordar"};
        for (String tipo : tiposBloom) {
            estadisticas.put(tipo.toLowerCase(), new int[3]); // [total, correctas, incorrectas]
        }
        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String bloom = p.getNivelBloom().toLowerCase();
            int[] stats = estadisticas.getOrDefault(bloom, new int[3]);
            stats[0]++; // total
            String respUsuario = respuestasUsuario.get(i);
            if (respUsuario != null && !respUsuario.isEmpty() && respUsuario.equalsIgnoreCase(p.getRespuestaCorrecta())) {
                stats[1]++; // correctas
            } else {
                stats[2]++; // incorrectas
            }
            estadisticas.put(bloom, stats);
        }
        StringBuilder resumen = new StringBuilder();
        resumen.append(String.format("%-12s %-10s %-12s %-12s %-12s %-12s\n", "Tipo Bloom", "Total", "Correctas", "% Correctas", "Incorrectas", "% Incorrectas"));
        resumen.append("-------------------------------------------------------------------------------\n");
        for (String tipo : tiposBloom) {
            int[] stats = estadisticas.get(tipo.toLowerCase());
            int total = stats[0];
            int correctas = stats[1];
            int incorrectas = stats[2];
            double pctCorrectas = total > 0 ? (100.0 * correctas / total) : 0;
            double pctIncorrectas = total > 0 ? (100.0 * incorrectas / total) : 0;
            resumen.append(String.format("%-12s %-10d %-12d %-12.1f %-12d %-12.1f\n",
                tipo, total, correctas, pctCorrectas, incorrectas, pctIncorrectas));
        }
        return resumen.toString();
    }
}