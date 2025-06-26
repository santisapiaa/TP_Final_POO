import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String nombre = EntradaUsuario.pedirNombreJugador();

        String[] dificultades = {"Fácil", "Medio", "Difícil"};
        String dificultadSeleccionada = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona la dificultad:",
                "Dificultad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                dificultades,
                dificultades[0]
        );

        Dificultad dificultad = switch (dificultadSeleccionada) {
            case "Medio" -> Dificultad.MEDIO;
            case "Difícil" -> Dificultad.DIFICIL;
            default -> Dificultad.FACIL;
        };

        JFrame frame = new JFrame("Galaga");
        PanelDeJuego panel = new PanelDeJuego(nombre, dificultad);
        frame.add(panel);
        frame.setSize(600, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        panel.start();
    }
}