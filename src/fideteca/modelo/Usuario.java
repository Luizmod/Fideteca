package fideteca.modelo;

/**
 * Clase Usuario.
 *
 * Representa a una persona registrada en la biblioteca.
 * Cada usuario se almacena en el árbol binario de búsqueda
 * utilizando el carné como identificador único.
 *
 * @author Luis
 */
public class Usuario {

    private String carne;
    private String nombre;
    private String tipo;

    /**
     * Construye un usuario.
     *
     * @param carne identificador único del usuario
     * @param nombre nombre completo
     * @param tipo tipo de usuario
     */
    public Usuario(String carne, String nombre, String tipo) {
        this.carne = carne;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getCarne() {
        return carne;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "[" + carne + "] "
                + nombre
                + " - "
                + tipo;
    }
}