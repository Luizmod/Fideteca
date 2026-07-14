package fideteca.estructuras;

import fideteca.modelo.Usuario;

/**
 * Clase ArbolBinarioBusqueda (BST)
 * PENDIENTE PARA EL AVANCE FINAL: se usara para registrar usuarios
 * por carne, permitiendo busquedas rapidas (O log n).
 *
 * En este avance se deja la estructura base y la insercion funcionando,
 * como evidencia de la arquitectura planeada. La eliminacion completa
 * (con reordenamiento de subarboles) se implementara en el entregable final.
 */
public class ArbolBinarioBusqueda {

    // Nodo interno del arbol, guarda un Usuario y referencias a sus hijos
    private class NodoArbol {
        Usuario usuario;
        NodoArbol izquierdo;
        NodoArbol derecho;

        NodoArbol(Usuario usuario) {
            this.usuario = usuario;
        }
    }

    private NodoArbol raiz;

    public ArbolBinarioBusqueda() {
        this.raiz = null;
    }

    // Inserta un usuario ordenando segun su carne (comparacion alfabetica)
    public void insertar(Usuario usuario) {
        raiz = insertarRecursivo(raiz, usuario);
    }

    private NodoArbol insertarRecursivo(NodoArbol nodo, Usuario usuario) {
        if (nodo == null) {
            return new NodoArbol(usuario);
        }

        int comparacion = usuario.getCarne().compareTo(nodo.usuario.getCarne());
        if (comparacion < 0) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, usuario);
        } else if (comparacion > 0) {
            nodo.derecho = insertarRecursivo(nodo.derecho, usuario);
        }
        // Si el carne ya existe, no se duplica

        return nodo;
    }

    // Busca un usuario por carne (recorrido tipico de BST)
    public Usuario buscar(String carne) {
        NodoArbol actual = raiz;
        while (actual != null) {
            int comparacion = carne.compareTo(actual.usuario.getCarne());
            if (comparacion == 0) {
                return actual.usuario;
            } else if (comparacion < 0) {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }
        return null; // no encontrado
    }

    // Recorrido "in-order": devuelve los usuarios ordenados por carne
    public String listarEnOrden() {
        StringBuilder sb = new StringBuilder();
        listarEnOrdenRecursivo(raiz, sb);
        if (sb.length() == 0) {
            return "No hay usuarios registrados.";
        }
        return sb.toString();
    }

    private void listarEnOrdenRecursivo(NodoArbol nodo, StringBuilder sb) {
        if (nodo != null) {
            listarEnOrdenRecursivo(nodo.izquierdo, sb);
            sb.append(nodo.usuario.toString()).append("\n");
            listarEnOrdenRecursivo(nodo.derecho, sb);
        }
    }

    // TODO (avance final): implementar eliminarPorCarne(String carne)
    // considerando los 3 casos: nodo hoja, nodo con un hijo, nodo con dos hijos.
}
