package fideteca.modelo;

/**
 * Clase Usuario
 * Representa a un usuario de la biblioteca (estudiante, profesor, etc).
 * Se usara mas adelante como el dato almacenado en el Arbol Binario
 * de Busqueda (registro de usuarios por carne).
 */
public class Usuario {

    private String carne;   // Identificador unico, usado como llave en el arbol
    private String nombre;
    private String tipo;    // "Estudiante", "Profesor", etc.

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
        return "[" + carne + "] " + nombre + " (" + tipo + ")";
    }
}
