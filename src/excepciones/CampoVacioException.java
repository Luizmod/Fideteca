package excepciones;

/**
 * Excepcion personalizada que se lanza cuando un campo obligatorio
 * del formulario (distinto al nombre) se deja vacio.
 */
public class CampoVacioException extends Exception {

    public CampoVacioException(String nombreCampo) {
        super("El campo '" + nombreCampo + "' no puede estar vacio.");
    }
}