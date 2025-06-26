import javax.swing.JOptionPane;

public class EntradaUsuario {
    public static String pedirNombreJugador() {
        String nombre = "";
        while (nombre == null || nombre.trim().isEmpty()) {
            nombre = JOptionPane.showInputDialog(null, "Ingrese su nombre:", "Nombre del jugador", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que querés salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        }
        return nombre.trim();
    }
}