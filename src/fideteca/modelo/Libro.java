package fideteca.modelo;

/**
 * Clase Libro
 * Representa un material bibliografico dentro del catalogo de FideTeca.
 * Esta clase es el "dato" que se almacena dentro de la Lista Enlazada
 * del catalogo.
 */
public class Libro {

    // Atributos principales del libro
    private String codigo;      // Identificador unico del libro (ej: "L001")
    private String titulo;
    private String autor;
    private String categoria;   // Ej: "Programacion", "Matematicas", etc.
    private boolean disponible; // true = hay ejemplares disponibles

    // Constructor: se ejecuta al crear un nuevo libro
    public Libro(String codigo, String titulo, String autor, String categoria) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.disponible = true; // por defecto, un libro nuevo esta disponible
    }

    // Metodos "getter" y "setter" para acceder y modificar los atributos
    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Representacion en texto del libro, util para mostrarlo en el menu
    @Override
    public String toString() {
        return "[" + codigo + "] " + titulo + " - " + autor
                + " (Categoria: " + categoria + ") - "
                + (disponible ? "Disponible" : "Prestado");
    }
}
