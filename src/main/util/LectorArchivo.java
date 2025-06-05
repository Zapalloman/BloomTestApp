package src.main.util;
import src.main.modelo.Pregunta;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivo {
    // Lee el archivo de preguntas y devuelve una lista de objetos Pregunta
    public static List<Pregunta> leerPreguntas(File archivo) throws IOException {
        List<Pregunta> preguntas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Si es pregunta de opción múltiple
                if (linea.startsWith("[PREGUNTA_MULTIPLE]")) {
                    String texto = br.readLine();
                    List<String> opciones = new ArrayList<>();
                    String respuestaCorrecta = "";
                    String opcionLinea;
                    // Lee todas las opciones hasta encontrar [BLOOM]
                    while ((opcionLinea = br.readLine()) != null && !opcionLinea.startsWith("[BLOOM]")) {
                        if (opcionLinea.contains("*")) {
                            // Marca la opción correcta según el orden (a, b, c, ...)
                            respuestaCorrecta = String.valueOf((char)('a' + opciones.size()));
                            opcionLinea = opcionLinea.replace("*", "");
                        }
                        opciones.add(opcionLinea.trim());
                    }
                    String nivelBloom = opcionLinea != null ? opcionLinea.replace("[BLOOM]", "").trim() : "";
                    preguntas.add(new Pregunta(texto, "MULTIPLE", nivelBloom, respuestaCorrecta, opciones));
                }
                // Si es pregunta de verdadero/falso
                else if (linea.startsWith("[PREGUNTA_VF]")) {
                    String texto = br.readLine();
                    String respuestaCorrecta = "";
                    int idx = texto.indexOf("*");
                    if (idx != -1 && idx < texto.length() - 1) {
                        respuestaCorrecta = texto.substring(idx + 1).trim();
                        texto = texto.substring(0, idx).trim();
                    }
                    String nivelBloom = br.readLine().replace("[BLOOM]", "").trim();
                    preguntas.add(new Pregunta(texto, "VF", nivelBloom, respuestaCorrecta));
                }
            }
        }
        return preguntas;
    }
}