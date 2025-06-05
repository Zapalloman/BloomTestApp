package src.main.modelo;

import java.util.List;

// Representa una pregunta, que puede ser de opción múltiple o verdadero/falso
public class Pregunta {
    private String texto; // El enunciado de la pregunta
    private String tipo; // "MULTIPLE" o "VF"
    private String nivelBloom; // Nivel de la taxonomía de Bloom
    private String respuestaCorrecta; // Letra (a, b, c, ...) o "V"/"F"
    private List<String> opciones; // Lista de alternativas (solo para MULTIPLE)

    // Constructor para preguntas de opción múltiple
    public Pregunta(String texto, String tipo, String nivelBloom, String respuestaCorrecta, List<String> opciones) {
        this.texto = texto;
        this.tipo = tipo;
        this.nivelBloom = nivelBloom;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = opciones;
    }

    // Constructor para preguntas de verdadero/falso
    public Pregunta(String texto, String tipo, String nivelBloom, String respuestaCorrecta) {
        this(texto, tipo, nivelBloom, respuestaCorrecta, null);
    }

    // Métodos para obtener los datos de la pregunta
    public String getTexto() { return texto; }
    public String getTipo() { return tipo; }
    public String getNivelBloom() { return nivelBloom; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public List<String> getOpciones() { return opciones; }
}