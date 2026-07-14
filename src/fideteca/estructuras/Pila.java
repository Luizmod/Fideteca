package fideteca.estructuras;

/**
 * Clase Pila (Stack) - implementacion propia basada en nodos enlazados.
 * Se usa para el HISTORIAL DE TRANSACCIONES de FideTeca.
 * Es LIFO (el ultimo en entrar es el primero en salir), ideal para
 * revisar o deshacer la operacion mas reciente primero.
 */
public class Pila<T> {

    private Nodo<T> tope; // nodo superior de la pila
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    // Agrega un elemento en el tope de la pila
    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = tope; // el nuevo nodo apunta al antiguo tope
        tope = nuevo;           // el nuevo nodo pasa a ser el tope
        tamanio++;
    }

    // Retira y devuelve el elemento del tope de la pila
    public T desapilar() {
        if (estaVacia()) {
            return null; // no hay nada que desapilar
        }
        T dato = tope.dato;
        tope = tope.siguiente; // el tope pasa a ser el siguiente nodo
        tamanio--;
        return dato;
    }

    // Consulta el elemento del tope sin eliminarlo
    public T verTope() {
        if (estaVacia()) {
            return null;
        }
        return tope.dato;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    // Devuelve el historial completo, del mas reciente al mas antiguo
    public String listarTodo() {
        if (estaVacia()) {
            return "No hay transacciones registradas.";
        }
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = tope;
        int contador = 1;
        while (actual != null) {
            sb.append(contador).append(". ").append(actual.dato.toString()).append("\n");
            actual = actual.siguiente;
            contador++;
        }
        return sb.toString();
    }
}
