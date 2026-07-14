package fideteca.estructuras;

import fideteca.modelo.Usuario;
import java.util.ArrayList;

/**
 * Clase ArbolBinarioBusqueda.
 *
 * Almacena los usuarios registrados en el sistema utilizando
 * el carné como llave de ordenamiento.
 *
 * Permite insertar, buscar, listar y obtener los usuarios
 * registrados para mostrarlos en menús desplegables.
 *
 * @author Luis
 */
public class ArbolBinarioBusqueda {

    /**
     * Nodo interno del árbol.
     */
    private class NodoArbol {

        private Usuario usuario;
        private NodoArbol izquierdo;
        private NodoArbol derecho;

        public NodoArbol(Usuario usuario) {
            this.usuario = usuario;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    private NodoArbol raiz;
    private int cantidad;

    /**
     * Construye un árbol vacío.
     */
    public ArbolBinarioBusqueda() {
        this.raiz = null;
        this.cantidad = 0;
    }

    /**
     * Inserta un usuario utilizando el carné como llave.
     *
     * @param usuario usuario que será insertado
     * @return true si fue insertado; false si ya existía
     */
    public boolean insertar(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        if (buscar(usuario.getCarne()) != null) {
            return false;
        }

        raiz = insertarRecursivo(raiz, usuario);
        cantidad++;

        return true;
    }

    /**
     * Inserta recursivamente un usuario.
     *
     * @param nodo nodo actual
     * @param usuario usuario que se desea insertar
     * @return nodo actualizado
     */
    private NodoArbol insertarRecursivo(
            NodoArbol nodo,
            Usuario usuario
    ) {
        if (nodo == null) {
            return new NodoArbol(usuario);
        }

        int comparacion = usuario.getCarne().compareToIgnoreCase(
                nodo.usuario.getCarne()
        );

        if (comparacion < 0) {
            nodo.izquierdo = insertarRecursivo(
                    nodo.izquierdo,
                    usuario
            );
        } else if (comparacion > 0) {
            nodo.derecho = insertarRecursivo(
                    nodo.derecho,
                    usuario
            );
        }

        return nodo;
    }

    /**
     * Busca un usuario por carné.
     *
     * @param carne carné que se desea buscar
     * @return usuario encontrado o null
     */
    public Usuario buscar(String carne) {
        if (carne == null) {
            return null;
        }

        NodoArbol actual = raiz;

        while (actual != null) {
            int comparacion = carne.compareToIgnoreCase(
                    actual.usuario.getCarne()
            );

            if (comparacion == 0) {
                return actual.usuario;
            }

            if (comparacion < 0) {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }

        return null;
    }

    /**
     * Devuelve los usuarios ordenados por carné.
     *
     * @return representación de los usuarios registrados
     */
    public String listarEnOrden() {
        if (estaVacio()) {
            return "No hay usuarios registrados.";
        }

        StringBuilder resultado = new StringBuilder();
        listarEnOrdenRecursivo(raiz, resultado);

        return resultado.toString();
    }

    /**
     * Recorre el árbol en orden.
     *
     * @param nodo nodo actual
     * @param resultado texto acumulado
     */
    private void listarEnOrdenRecursivo(
            NodoArbol nodo,
            StringBuilder resultado
    ) {
        if (nodo == null) {
            return;
        }

        listarEnOrdenRecursivo(
                nodo.izquierdo,
                resultado
        );

        resultado.append(nodo.usuario)
                .append("\n");

        listarEnOrdenRecursivo(
                nodo.derecho,
                resultado
        );
    }

    /**
     * Devuelve directamente los objetos Usuario almacenados.
     *
     * Este arreglo se utiliza para mostrar los usuarios
     * dentro de un menú desplegable.
     *
     * @return arreglo de usuarios ordenados por carné
     */
    public Object[] obtenerUsuarios() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        agregarUsuariosEnOrden(
                raiz,
                listaUsuarios
        );

        return listaUsuarios.toArray();
    }

    /**
     * Agrega los usuarios a una lista mediante un recorrido en orden.
     *
     * @param nodo nodo actual
     * @param listaUsuarios lista que recibirá los usuarios
     */
    private void agregarUsuariosEnOrden(
            NodoArbol nodo,
            ArrayList<Usuario> listaUsuarios
    ) {
        if (nodo == null) {
            return;
        }

        agregarUsuariosEnOrden(
                nodo.izquierdo,
                listaUsuarios
        );

        listaUsuarios.add(nodo.usuario);

        agregarUsuariosEnOrden(
                nodo.derecho,
                listaUsuarios
        );
    }

    /**
     * Indica si el árbol está vacío.
     *
     * @return true si no existen usuarios
     */
    public boolean estaVacio() {
        return raiz == null;
    }

    /**
     * Devuelve la cantidad de usuarios.
     *
     * @return cantidad de usuarios registrados
     */
    public int getCantidad() {
        return cantidad;
    }

    /*
     * Pendiente para el avance final:
     * implementar eliminarPorCarne considerando:
     *
     * 1. Nodo hoja.
     * 2. Nodo con un hijo.
     * 3. Nodo con dos hijos.
     */
}