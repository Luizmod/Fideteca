package excepciones;

/**
 * Excepcion personalizada que se lanza cuando se intenta registrar
 * un usuario sin ingresar un nombre (campo vacio o nulo).
 */
public class NombreVacioException extends Exception {

    public NombreVacioException() {
        super("El nombre del usuario no puede estar vacio.");
    }
}
