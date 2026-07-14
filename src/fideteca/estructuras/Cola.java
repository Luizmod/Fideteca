package fideteca.estructuras;

/**
 * Clase Cola (Queue) - implementacion propia basada en nodos enlazados.
 * Se usa para gestionar la FILA DE RESERVAS de libros agotados.
 * Es FIFO (el primero en llegar es el primero en ser atendido),
 * lo cual garantiza orden y justicia entre los usuarios que reservan.
 */
public class Cola<T> {

    private Nodo<T> frente; // primer elemento de la cola (el proximo a atender)
    private Nodo<T> final_; // ultimo elemento de la cola (el ultimo en llegar)
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.final_ = null;
        this.tamanio = 0;
    }

    // Agrega un elemento al final de la cola (el usuario "hace fila")
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            // Si la cola esta vacia, el nuevo nodo es frente y final a la vez
            frente = nuevo;
            final_ = nuevo;
        } else {
            final_.siguiente = nuevo; // el ultimo nodo enlaza al nuevo
            final_ = nuevo;           // el nuevo nodo pasa a ser el final
        }
        tamanio++;
    }

    // Atiende y retira al primero de la fila
    public T desencolar() {
        if (estaVacia()) {
            return null;
        }
        T dato = frente.dato;
        frente = frente.siguiente; // el frente avanza al siguiente nodo

        if (frente == null) {
            final_ = null; // si ya no quedan nodos, tambien se limpia el final
        }
        tamanio--;
        return dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    // Devuelve la fila completa en orden (del proximo a atender al ultimo)
    public String listarTodo() {
        if (estaVacia()) {
            return "No hay reservas en espera.";
        }
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = frente;
        int contador = 1;
        while (actual != null) {
            sb.append(contador).append(". ").append(actual.dato.toString()).append("\n");
            actual = actual.siguiente;
            contador++;
        }
        return sb.toString();
    }
}
