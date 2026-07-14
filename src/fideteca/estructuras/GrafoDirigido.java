package fideteca.estructuras;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Clase GrafoDirigido
 * PENDIENTE PARA EL AVANCE FINAL: se usara para modelar las relaciones
 * tematicas entre categorias de libros (ej: "Programacion" -> "Bases de Datos").
 *
 * En este avance se deja lista la estructura base (agregar categorias y
 * relaciones). Los recorridos (BFS/DFS) para sugerir libros relacionados
 * se implementaran en el entregable final.
 */
public class GrafoDirigido {

    // Lista de adyacencia: cada categoria apunta a una lista de categorias relacionadas
    private Map<String, LinkedList<String>> adyacencias;

    public GrafoDirigido() {
        this.adyacencias = new HashMap<>();
    }

    // Agrega una categoria (vertice) nueva al grafo, si no existe
    public void agregarCategoria(String categoria) {
        adyacencias.putIfAbsent(categoria, new LinkedList<>());
    }

    // Crea una relacion dirigida entre dos categorias (arista)
    public void relacionarCategorias(String origen, String destino) {
        agregarCategoria(origen);
        agregarCategoria(destino);
        adyacencias.get(origen).add(destino);
    }

    public String listarRelaciones() {
        if (adyacencias.isEmpty()) {
            return "No hay categorias registradas.";
        }
        StringBuilder sb = new StringBuilder();
        for (String categoria : adyacencias.keySet()) {
            sb.append(categoria).append(" -> ").append(adyacencias.get(categoria)).append("\n");
        }
        return sb.toString();
    }

    // TODO (avance final): implementar recorridoBFS(String categoriaInicio)
    // TODO (avance final): implementar recorridoDFS(String categoriaInicio)
}
