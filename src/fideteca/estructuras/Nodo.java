package fideteca.estructuras;

/**
 * Clase Nodo
 * Representa un nodo generico usado por la Lista Enlazada.
 * Guarda un dato de tipo T y una referencia al siguiente nodo.
 */
public class Nodo<T> {

    T dato;
    Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
