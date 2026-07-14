package fideteca.estructuras;

/**
 * Clase ListaEnlazada (implementacion propia, sin usar java.util.LinkedList)
 * Se usa para representar el catalogo de libros de FideTeca.
 * Operaciones principales: insertar, eliminar, buscar y recorrer.
 */
public class ListaEnlazada<T> {

    private Nodo<T> cabeza; // primer nodo de la lista
    private int tamanio;    // cantidad de elementos actuales

    public ListaEnlazada() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    // Inserta un nuevo elemento al final de la lista
    public void insertar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (cabeza == null) {
            // La lista esta vacia, el nuevo nodo pasa a ser la cabeza
            cabeza = nuevo;
        } else {
            // Recorremos hasta el ultimo nodo para enlazar el nuevo
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    // Elimina el primer elemento que cumpla la condicion (usando un codigo/llave)
    // Se recibe una "llave" en texto y una forma de comparar mediante toString()
    // para mantener la clase generica y simple de usar desde el menu.
    public boolean eliminarPorCodigo(String codigo) {
        if (cabeza == null) {
            return false; // lista vacia, no hay nada que eliminar
        }

        // Caso especial: el elemento a eliminar es la cabeza
        if (cabeza.dato.toString().contains(codigo)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }

        // Buscamos el nodo anterior al que queremos eliminar
        Nodo<T> anterior = cabeza;
        Nodo<T> actual = cabeza.siguiente;

        while (actual != null) {
            if (actual.dato.toString().contains(codigo)) {
                anterior.siguiente = actual.siguiente; // "saltamos" el nodo actual
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        return false; // no se encontro el codigo
    }

    // Busca un elemento por codigo y devuelve true si existe en la lista
    public boolean existeCodigo(String codigo) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.toString().contains(codigo)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
    
    // Devuelve un arreglo con el texto de cada elemento, util para mostrar
    // una lista desplegable (combo box) en JOptionPane
    public Object[] toArray() {
        Object[] arreglo = new Object[tamanio];
        Nodo<T> actual = cabeza;
        int i = 0;
        while (actual != null) {
            arreglo[i] = actual.dato.toString();
            actual = actual.siguiente;
            i++;
        }
        return arreglo;
    }
    
    // Devuelve un texto con todos los elementos de la lista, uno por linea
    public String listarTodo() {
        if (cabeza == null) {
            return "El catalogo esta vacio.";
        }

        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = cabeza;
        int contador = 1;
        while (actual != null) {
            sb.append(contador).append(". ").append(actual.dato.toString()).append("\n");
            actual = actual.siguiente;
            contador++;
        }
        return sb.toString();
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }
}
