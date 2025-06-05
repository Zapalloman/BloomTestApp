package src.main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import src.main.modelo.Pregunta;

// Ventana donde el usuario responde la prueba
public class VentanaPrueba extends JFrame {
    private List<Pregunta> preguntas;
    private int preguntaActual = 0;
    private JLabel lblPregunta;
    private JPanel panelOpciones;
    private JButton btnAnterior, btnSiguiente, btnFinalizar;
    private ButtonGroup grupoOpciones;
    private List<String> respuestasUsuario = new ArrayList<>();
    private JLabel lblMensaje; // Mensaje de corrección
    private List<Boolean> preguntaRespondida = new ArrayList<>(); // Marca si ya se avanzó desde la pregunta

    public VentanaPrueba(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
        setTitle("Prueba");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en pantalla

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblPregunta = new JLabel("", SwingConstants.CENTER);
        lblPregunta.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPregunta.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(lblPregunta);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        panelOpciones = new JPanel();
        panelOpciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(panelOpciones);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        // Mensaje de corrección
        lblMensaje = new JLabel(" ");
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 14));
        lblMensaje.setForeground(Color.BLUE);
        panelPrincipal.add(lblMensaje);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");
        btnFinalizar = new JButton("Finalizar");

        btnAnterior.addActionListener(e -> mostrarAnterior());
        btnSiguiente.addActionListener(e -> mostrarSiguiente());
        btnFinalizar.addActionListener(e -> finalizarPrueba());

        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnFinalizar);
        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrincipal.add(panelBotones);

        setContentPane(panelPrincipal);

        for (int i = 0; i < preguntas.size(); i++) {
            respuestasUsuario.add("");
            preguntaRespondida.add(false);
        }

        actualizarBotones();
        if (!preguntas.isEmpty()) {
            mostrarPregunta(preguntas.get(preguntaActual));
        }
    }

    // Muestra la pregunta y sus alternativas
    private void mostrarPregunta(Pregunta p) {
        lblPregunta.setText("<html><div style='text-align:center;width:400px'>" + p.getTexto() + "</div></html>");
        panelOpciones.removeAll();

        grupoOpciones = new ButtonGroup();
        String seleccion = respuestasUsuario.get(preguntaActual);

        boolean respondida = preguntaRespondida.get(preguntaActual);
        boolean enBlanco = (seleccion == null || seleccion.isEmpty());

        List<JRadioButton> radios = new ArrayList<>();

        if (p.getTipo().equals("MULTIPLE")) {
            int n = p.getOpciones().size();
            int filas = (int)Math.ceil(n / 2.0);
            panelOpciones.setLayout(new GridLayout(filas, 2, 10, 10));
            for (int i = 0; i < n; i++) {
                char letra = (char)('a' + i);
                JRadioButton rb = new JRadioButton(p.getOpciones().get(i));
                rb.setHorizontalAlignment(SwingConstants.CENTER);
                grupoOpciones.add(rb);
                radios.add(rb);

                final int idx = i;
                rb.addActionListener(e -> {
                    if (enBlanco) {
                        guardarRespuesta();
                    }
                });

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
                panel.add(rb, BorderLayout.CENTER);
                panelOpciones.add(panel);

                if (seleccion.equals(String.valueOf(letra))) rb.setSelected(true);
                // Solo deshabilita si ya fue respondida y no está en blanco
                rb.setEnabled(enBlanco);
            }
        } else if (p.getTipo().equals("VF")) {
            panelOpciones.setLayout(new GridLayout(1, 2, 10, 10));
            JRadioButton v = new JRadioButton("Verdadero");
            JRadioButton f = new JRadioButton("Falso");
            v.setHorizontalAlignment(SwingConstants.CENTER);
            f.setHorizontalAlignment(SwingConstants.CENTER);
            grupoOpciones.add(v);
            grupoOpciones.add(f);
            radios.add(v);
            radios.add(f);

            v.addActionListener(e -> {
                if (enBlanco) {
                    guardarRespuesta();
                }
            });
            f.addActionListener(e -> {
                if (enBlanco) {
                    guardarRespuesta();
                }
            });

            JPanel panelV = new JPanel(new BorderLayout());
            panelV.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            panelV.add(v, BorderLayout.CENTER);

            JPanel panelF = new JPanel(new BorderLayout());
            panelF.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            panelF.add(f, BorderLayout.CENTER);

            panelOpciones.add(panelV);
            panelOpciones.add(panelF);

            if (seleccion.equals("V")) v.setSelected(true);
            if (seleccion.equals("F")) f.setSelected(true);

            v.setEnabled(enBlanco);
            f.setEnabled(enBlanco);
        }

        // Si ya fue respondida y no está en blanco, muestra el mensaje y deshabilita botones
        if (respondida && !enBlanco) {
            int idx = getSeleccionIndiceActual();
            if (idx != -1) {
                mostrarMensajeCorreccion(p, idx);
            }
            for (JRadioButton rb : radios) rb.setEnabled(false);
            btnSiguiente.setEnabled(true);
            btnFinalizar.setEnabled(true);
        } else {
            lblMensaje.setText(" ");
            btnSiguiente.setEnabled(true);
            btnFinalizar.setEnabled(true);
        }

        panelOpciones.revalidate();
        panelOpciones.repaint();
    }

    // Muestra mensaje de corrección según la respuesta seleccionada
    private void mostrarMensajeCorreccion(Pregunta p, int indiceSeleccionado) {
        String seleccion = "";
        if (p.getTipo().equals("MULTIPLE")) {
            seleccion = String.valueOf((char)('a' + indiceSeleccionado));
        } else if (p.getTipo().equals("VF")) {
            seleccion = (indiceSeleccionado == 0) ? "V" : "F";
        }
        // No modificar respuestasUsuario aquí, solo mostrar el mensaje
        if (seleccion.equalsIgnoreCase(p.getRespuestaCorrecta())) {
            lblMensaje.setText("¡Respuesta correcta!");
            lblMensaje.setForeground(new Color(0, 128, 0));
        } else {
            lblMensaje.setText("Respuesta incorrecta.");
            lblMensaje.setForeground(Color.RED);
        }
    }

    // Devuelve el índice de la respuesta seleccionada actual, o -1 si no hay selección
    private int getSeleccionIndiceActual() {
        String seleccion = respuestasUsuario.get(preguntaActual);
        if (seleccion != null && !seleccion.isEmpty()) {
            if (preguntas.get(preguntaActual).getTipo().equals("MULTIPLE")) {
                return seleccion.charAt(0) - 'a';
            } else if (preguntas.get(preguntaActual).getTipo().equals("VF")) {
                return seleccion.equals("V") ? 0 : 1;
            }
        }
        return -1;
    }

    // Navega a la pregunta anterior
    private void mostrarAnterior() {
        guardarRespuesta();
        if (preguntaActual > 0) {
            preguntaActual--;
            mostrarPregunta(preguntas.get(preguntaActual));
            actualizarBotones();
        }
    }

    // Navega a la siguiente pregunta o finaliza si es la última
    private void mostrarSiguiente() {
        guardarRespuesta();
        String seleccion = respuestasUsuario.get(preguntaActual);
        boolean enBlanco = (seleccion == null || seleccion.isEmpty());

        if (!preguntaRespondida.get(preguntaActual) && !enBlanco) {
            preguntaRespondida.set(preguntaActual, true);
            int idx = getSeleccionIndiceActual();
            if (idx != -1) {
                mostrarMensajeCorreccion(preguntas.get(preguntaActual), idx);
            }
            setOpcionesEnabled(false);
            btnSiguiente.setEnabled(false);
            btnFinalizar.setEnabled(false);

            Timer timer = new Timer(2000, e -> {
                if (preguntaActual < preguntas.size() - 1) {
                    preguntaActual++;
                    mostrarPregunta(preguntas.get(preguntaActual));
                    actualizarBotones();
                } else {
                    finalizarPrueba();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            // Si no respondida o está en blanco, avanza normalmente
            if (preguntaActual < preguntas.size() - 1) {
                preguntaActual++;
                mostrarPregunta(preguntas.get(preguntaActual));
                actualizarBotones();
            } else {
                finalizarPrueba();
            }
        }
    }

    // Al finalizar la prueba, muestra el resumen y las estadísticas
    private void finalizarPrueba() {
        guardarRespuesta();
        // Verifica si hay preguntas sin responder
        boolean faltan = false;
        for (String resp : respuestasUsuario) {
            if (resp == null || resp.isEmpty()) {
                faltan = true;
                break;
            }
        }
        if (faltan) {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "Aún tienes preguntas sin responder.\n¿Deseas finalizar la prueba de todas formas?",
                "Preguntas sin responder",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (opcion != JOptionPane.YES_OPTION) {
                return; // No finaliza, vuelve a la prueba
            }
        }
        preguntaRespondida.set(preguntaActual, true); // Marca la última como respondida
        int idx = getSeleccionIndiceActual();
        if (idx != -1) {
            mostrarMensajeCorreccion(preguntas.get(preguntaActual), idx);
        }
        int correctas = 0;
        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String respUsuario = respuestasUsuario.get(i);
            boolean esCorrecta = respUsuario != null && !respUsuario.isEmpty() && respUsuario.equalsIgnoreCase(p.getRespuestaCorrecta());
            if (esCorrecta) correctas++;
        }
        JOptionPane.showMessageDialog(this,
            "¡Prueba finalizada!\nRespuestas correctas: " + correctas + " de " + preguntas.size() +
            "\n\nPuedes ver el detalle y las estadísticas en la siguiente ventana.");
        new VentanaResumenFinal(preguntas, respuestasUsuario).setVisible(true);
        dispose();
    }

    // Actualiza el estado y texto de los botones según la pregunta actual
    private void actualizarBotones() {
        btnAnterior.setEnabled(preguntaActual > 0);
        if (preguntaActual < preguntas.size() - 1) {
            btnSiguiente.setText("Siguiente");
            btnFinalizar.setVisible(true);
        } else {
            btnSiguiente.setText("Finalizar");
            btnFinalizar.setVisible(false);
        }
    }

    // Guarda la respuesta seleccionada por el usuario para la pregunta actual
    private void guardarRespuesta() {
        String seleccion = "";
        for (AbstractButton button : java.util.Collections.list(grupoOpciones.getElements())) {
            if (button.isSelected()) {
                String text = button.getText();
                if (preguntas.get(preguntaActual).getTipo().equals("MULTIPLE")) {
                    // Detecta la letra según el orden de la opción
                    for (int i = 0; i < preguntas.get(preguntaActual).getOpciones().size(); i++) {
                        if (text.equals(preguntas.get(preguntaActual).getOpciones().get(i))) {
                            seleccion = String.valueOf((char)('a' + i));
                            break;
                        }
                    }
                } else if (preguntas.get(preguntaActual).getTipo().equals("VF")) {
                    if (text.startsWith("Verdadero")) seleccion = "V";
                    else if (text.startsWith("Falso")) seleccion = "F";
                }
            }
        }
        respuestasUsuario.set(preguntaActual, seleccion);
    }

    // Deshabilita o habilita todas las opciones de la pregunta actual
    private void setOpcionesEnabled(boolean enabled) {
        for (AbstractButton button : java.util.Collections.list(grupoOpciones.getElements())) {
            button.setEnabled(enabled);
        }
    }
}
