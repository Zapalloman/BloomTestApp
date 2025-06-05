
Aplicación de escritorio en Java con interfaz gráfica (AWT/Swing) para administrar y aplicar pruebas educativas basadas en la Taxonomía de Bloom.  
Permite cargar preguntas desde archivos `.txt`, presentar ítems de selección múltiple o verdadero/falso, y entregar estadísticas detalladas del desempeño del usuario.  
El sistema incluye revisión visual de respuestas y mantiene modularidad del código con buena documentación.  

🧠 Características principales:
- Carga dinámica de pruebas desde archivos externos.
- Presentación uno a uno de los ítems con retroalimentación inmediata.
- Estadísticas por tipo de ítem al finalizar la prueba.
- Soporte para ítems categorizados según niveles cognitivos de Bloom.

📁 Proyecto desarrollado como tarea universitaria para el ramo **Paradigmas de Programación**.

---

### 🛠️ Compilación y ejecución desde la terminal

1. Ubícate en el directorio raíz del proyecto.
2. Ejecuta el siguiente comando para compilar los archivos `.java`:

```bash
javac -d bin src/main/modelo/Pregunta.java src/main/util/LectorArchivo.java src/main/gui/VentanaPrincipal.java src/main/gui/VentanaPrueba.java src/main/Main.java
java -cp bin src.main.Main
