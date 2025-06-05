package src.main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import src.main.modelo.Pregunta;

public class VentanaEstadisticasBloom extends JFrame {
    public VentanaEstadisticasBloom(List<Pregunta> preguntas, List<String> respuestasUsuario) {
        setTitle("Estadísticas por Nivel Bloom");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Map: Bloom -> [total, correctas, incorrectas]
        Map<String, int[]> estadisticas = new LinkedHashMap<>();

        // Inicializa los tipos de Bloom esperados
        String[] tiposBloom = {"Crear", "Evaluar", "Analizar", "Aplicar", "Entender", "Recordar"};
        for (String tipo : tiposBloom) {
            estadisticas.put(tipo.toLowerCase(), new int[3]); // [total, correctas, incorrectas]
        }

        // Calcula estadísticas
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

        // Construye el resumen
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

        JTextArea area = new JTextArea(resumen.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(area);

        add(scroll, BorderLayout.CENTER);
    }
}