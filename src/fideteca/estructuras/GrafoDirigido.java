package fideteca.estructuras;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Clase GrafoDirigido.
 *
 * Representa las relaciones temáticas entre las categorías
 * de los libros.
 *
 * Cada categoría corresponde a un vértice y cada relación
 * corresponde a una arista dirigida.
 *
 * @author Luis
 */
public class GrafoDirigido {

    private Map<String, LinkedList<String>> adyacencias;

    /**
     * Construye un grafo vacío.
     */
    public GrafoDirigido() {
        adyacencias = new HashMap<>();
    }

    /**
     * Agrega una categoría cuando no existe.
     *
     * @param categoria categoría que será registrada
     */
    public void agregarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return;
        }

        String categoriaExistente = buscarCategoria(
                categoria.trim()
        );

        if (categoriaExistente == null) {
            adyacencias.put(
                    categoria.trim(),
                    new LinkedList<>()
            );
        }
    }

    /**
     * Busca una categoría sin diferenciar mayúsculas.
     *
     * @param nombre categoría que se desea buscar
     * @return nombre almacenado o null
     */
    public String buscarCategoria(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (String categoria : adyacencias.keySet()) {
            if (categoria.equalsIgnoreCase(nombre.trim())) {
                return categoria;
            }
        }

        return null;
    }

    /**
     * Relaciona dos categorías.
     *
     * No permite relaciones repetidas ni relaciones de una
     * categoría consigo misma.
     *
     * @param origen categoría de origen
     * @param destino categoría de destino
     * @return true si se registró la relación
     */
    public boolean relacionarCategorias(
            String origen,
            String destino
    ) {
        if (origen == null || destino == null) {
            return false;
        }

        origen = origen.trim();
        destino = destino.trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            return false;
        }

        if (origen.equalsIgnoreCase(destino)) {
            return false;
        }

        agregarCategoria(origen);
        agregarCategoria(destino);

        String origenRegistrado = buscarCategoria(origen);
        String destinoRegistrado = buscarCategoria(destino);

        if (existeRelacion(
                origenRegistrado,
                destinoRegistrado
        )) {
            return false;
        }

        adyacencias.get(origenRegistrado)
                .add(destinoRegistrado);

        return true;
    }

    /**
     * Comprueba si una relación existe.
     *
     * @param origen categoría de origen
     * @param destino categoría de destino
     * @return true si la relación está registrada
     */
    public boolean existeRelacion(
            String origen,
            String destino
    ) {
        if (origen == null || destino == null) {
            return false;
        }

        if (!adyacencias.containsKey(origen)) {
            return false;
        }

        for (String categoriaRelacionada
                : adyacencias.get(origen)) {

            if (categoriaRelacionada.equalsIgnoreCase(destino)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Devuelve las categorías registradas.
     *
     * @return arreglo utilizado en menús desplegables
     */
    public Object[] obtenerCategorias() {
        return adyacencias.keySet().toArray();
    }

    /**
     * Devuelve la cantidad de categorías.
     *
     * @return cantidad de vértices
     */
    public int cantidadCategorias() {
        return adyacencias.size();
    }

    /**
     * Indica si el grafo está vacío.
     *
     * @return true si no hay categorías
     */
    public boolean estaVacio() {
        return adyacencias.isEmpty();
    }

    /**
     * Muestra las relaciones del grafo.
     *
     * @return texto con categorías y relaciones
     */
    public String listarRelaciones() {
        if (adyacencias.isEmpty()) {
            return "No hay categorías registradas.";
        }

        StringBuilder resultado = new StringBuilder();

        for (String categoria : adyacencias.keySet()) {
            resultado.append(categoria)
                    .append(" -> ");

            LinkedList<String> relaciones
                    = adyacencias.get(categoria);

            if (relaciones.isEmpty()) {
                resultado.append("Sin relaciones");
            } else {
                resultado.append(relaciones);
            }

            resultado.append("\n");
        }

        return resultado.toString();
    }

    /*
     * Pendiente para el avance final:
     * implementar recorrido BFS y recorrido DFS.
     */
}