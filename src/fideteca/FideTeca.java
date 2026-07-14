package fideteca;

import fideteca.estructuras.ArbolBinarioBusqueda;
import fideteca.estructuras.Cola;
import fideteca.estructuras.GrafoDirigido;
import fideteca.estructuras.ListaEnlazada;
import fideteca.estructuras.Pila;
import fideteca.modelo.Libro;
import fideteca.modelo.Transaccion;
import fideteca.modelo.Usuario;
import javax.swing.JOptionPane;
import excepciones.LibroNoEncontradoException;

/**
 * Clase FideTeca (clase principal / main)
 * Contiene el menu interactivo del sistema, construido con JOptionPane.
 * Desde aqui se accede a las operaciones de cada estructura de datos:
 * - Catalogo de libros -> Lista Enlazada
 * - Historial de transacciones -> Pila
 * - Reservas de libros agotados -> Cola
 * - Registro de usuarios -> Arbol Binario de Busqueda (esqueleto)
 * - Relaciones entre categorias -> Grafo Dirigido (esqueleto)
 */
public class FideTeca {

    // Estructuras de datos principales del sistema, disponibles durante
    // toda la ejecucion del programa
    static ListaEnlazada<Libro> catalogo = new ListaEnlazada<>();
    static Pila<Transaccion> historial = new Pila<>();
    static Cola<String> reservas = new Cola<>();
    static ArbolBinarioBusqueda usuarios = new ArbolBinarioBusqueda();
    static GrafoDirigido categorias = new GrafoDirigido();

    public static void main(String[] args) {

        cargarDatosDePrueba(); // algunos datos iniciales para probar el sistema

        int opcion;
        do {
            // Menu principal construido con JOptionPane
            String menu = "===== FIDETECA - Sistema de Biblioteca =====\n"
                    + "1. Catalogo de libros\n"
                    + "2. Historial de transacciones\n"
                    + "3. Reservas de libros\n"
                    + "4. Registro de usuarios\n"
                    + "5. Categorias de libros\n"
                    + "0. Salir\n\n"
                    + "Seleccione una opcion:";

            String entrada = JOptionPane.showInputDialog(null, menu, "FideTeca",
                    JOptionPane.PLAIN_MESSAGE);

            // Si el usuario cierra la ventana, salimos del programa
            if (entrada == null) {
                break;
            }

            try {
                opcion = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un numero valido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    menuCatalogo();
                    break;
                case 2:
                    menuHistorial();
                    break;
                case 3:
                    menuReservas();
                    break;
                case 4:
                    menuUsuarios();
                    break;
                case 5:
                    menuCategorias();
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Gracias por usar FideTeca.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opcion no valida.");
            }

        } while (opcion_valida());
    }

    // Metodo auxiliar simple para mantener el ciclo del menu.
    // (Se controla la salida con "break" dentro del switch/case 0)
    private static boolean opcion_valida() {
        return true;
    }

    // ---------------------------------------------------------------
    // SUBMENU 1: CATALOGO DE LIBROS (Lista Enlazada)
    // ---------------------------------------------------------------
    private static void menuCatalogo() {
        String submenu = "----- Catalogo de libros -----\n"
                + "1. Agregar libro\n"
                + "2. Eliminar libro por codigo\n"
                + "3. Ver catalogo completo\n"
                + "0. Volver";

        String entrada = JOptionPane.showInputDialog(null, submenu);
        if (entrada == null) return;

        switch (entrada.trim()) {
            case "1":
                String codigo = JOptionPane.showInputDialog("Codigo del libro (ej: L001):");
                String titulo = JOptionPane.showInputDialog("Titulo:");
                String autor = JOptionPane.showInputDialog("Autor:");
                String categoria = JOptionPane.showInputDialog("Categoria:");

                Libro nuevo = new Libro(codigo, titulo, autor, categoria);
                catalogo.insertar(nuevo);

                // Registramos la accion en el historial (Pila)
                historial.apilar(new Transaccion("ALTA_LIBRO", codigo,
                        "Se agrego el libro '" + titulo + "' al catalogo."));

                // Tambien registramos la categoria en el grafo tematico
                categorias.agregarCategoria(categoria);

                JOptionPane.showMessageDialog(null, "Libro agregado correctamente.");
                break;

            case "2":
                String codigoEliminar = JOptionPane.showInputDialog("Codigo del libro a eliminar:");
                boolean eliminado = catalogo.eliminarPorCodigo(codigoEliminar);

                if (eliminado) {
                    historial.apilar(new Transaccion("BAJA_LIBRO", codigoEliminar,
                            "Se elimino el libro del catalogo."));
                    JOptionPane.showMessageDialog(null, "Libro eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro un libro con ese codigo.");
                }
                break;

            case "3":
                JOptionPane.showMessageDialog(null, catalogo.listarTodo());
                break;

            default:
                // Si es "0" u otra cosa, simplemente volvemos al menu principal
                break;
        }
    }

    // ---------------------------------------------------------------
    // SUBMENU 2: HISTORIAL DE TRANSACCIONES (Pila)
    // ---------------------------------------------------------------
    private static void menuHistorial() {
        String submenu = "----- Historial de transacciones -----\n"
                + "1. Ver ultima transaccion (tope de la pila)\n"
                + "2. Deshacer / retirar ultima transaccion\n"
                + "3. Ver historial completo\n"
                + "0. Volver";

        String entrada = JOptionPane.showInputDialog(null, submenu);
        if (entrada == null) return;

        switch (entrada.trim()) {
            case "1":
                Transaccion tope = historial.verTope();
                if (tope != null) {
                    JOptionPane.showMessageDialog(null, "Ultima transaccion:\n" + tope);
                } else {
                    JOptionPane.showMessageDialog(null, "No hay transacciones registradas.");
                }
                break;

            case "2":
                Transaccion retirada = historial.desapilar();
                if (retirada != null) {
                    JOptionPane.showMessageDialog(null, "Se retiro del historial:\n" + retirada);
                } else {
                    JOptionPane.showMessageDialog(null, "No hay transacciones para retirar.");
                }
                break;

            case "3":
                JOptionPane.showMessageDialog(null, historial.listarTodo());
                break;

            default:
                break;
        }
    }

    // ---------------------------------------------------------------
    // SUBMENU 3: RESERVAS DE LIBROS AGOTADOS (Cola)
    // ---------------------------------------------------------------
    private static void menuReservas() {
        String submenu = "----- Reservas de libros -----\n"
                + "1. Reservar un libro (encolar)\n"
                + "2. Atender siguiente reserva (desencolar)\n"
                + "3. Ver fila de reservas\n"
                + "0. Volver";

        String entrada = JOptionPane.showInputDialog(null, submenu);
        if (entrada == null) return;

        switch (entrada.trim()) {
        case "1":
            // Verificamos primero que haya libros en el catalogo
            if (catalogo.estaVacia()) {
                JOptionPane.showMessageDialog(null, "No hay libros registrados en el catalogo.");
                break;
            }

            // Mostramos una lista desplegable con los libros disponibles
            Object[] opcionesLibros = catalogo.toArray();
            Object seleccion = JOptionPane.showInputDialog(
                    null,
                    "Seleccione el libro a reservar:",
                    "Reservar libro",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesLibros,
                    opcionesLibros[0]);

            // Si el usuario cancela el dialogo, salimos sin hacer nada
            if (seleccion == null) {
                break;
            }

            // El texto seleccionado tiene formato: "[L001] Titulo - Autor (...)"
            // Extraemos solo el codigo, que esta entre corchetes
            String textoSeleccionado = seleccion.toString();
            String codigoLibroReserva = textoSeleccionado.substring(
                    textoSeleccionado.indexOf("[") + 1,
                    textoSeleccionado.indexOf("]"));

            try {
                // Validamos que el codigo exista en el catalogo antes de continuar
                if (!catalogo.existeCodigo(codigoLibroReserva)) {
                    throw new LibroNoEncontradoException(codigoLibroReserva);
                }

                String nombreUsuario = JOptionPane.showInputDialog("Nombre del usuario que reserva:");

                // Guardamos ambos datos juntos en la cola, con un formato legible
                String datoReserva = codigoLibroReserva + " - " + nombreUsuario;
                reservas.encolar(datoReserva);

                historial.apilar(new Transaccion("RESERVA", codigoLibroReserva,
                        nombreUsuario + " reservo el libro " + codigoLibroReserva + "."));
                JOptionPane.showMessageDialog(null, "Reserva registrada:\n" + datoReserva);

            } catch (LibroNoEncontradoException e) {
                // Capturamos la excepcion y mostramos el mensaje al usuario
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
            break;

        case "2":
            String atendido = reservas.desencolar();
            if (atendido != null) {
                JOptionPane.showMessageDialog(null, "Se atendio la reserva de:\n" + atendido);
            } else {
                JOptionPane.showMessageDialog(null, "No hay reservas pendientes.");
            }
            break;
        }
    }

    // ---------------------------------------------------------------
    // SUBMENU 4: REGISTRO DE USUARIOS (Arbol) - version inicial
    // ---------------------------------------------------------------
    private static void menuUsuarios() {
        String submenu = "----- Registro de usuarios -----\n"
                + "1. Registrar usuario\n"
                + "2. Buscar usuario por carne\n"
                + "3. Ver todos los usuarios (ordenados)\n"
                + "0. Volver";

        String entrada = JOptionPane.showInputDialog(null, submenu);
        if (entrada == null) return;

        switch (entrada.trim()) {
            case "1":
                String carne = JOptionPane.showInputDialog("Carne del usuario:");
                String nombre = JOptionPane.showInputDialog("Nombre:");
                String tipo = JOptionPane.showInputDialog("Tipo (Estudiante/Profesor):");
                usuarios.insertar(new Usuario(carne, nombre, tipo));
                JOptionPane.showMessageDialog(null, "Usuario registrado.");
                break;

            case "2":
                String carneBuscar = JOptionPane.showInputDialog("Carne a buscar:");
                Usuario encontrado = usuarios.buscar(carneBuscar);
                if (encontrado != null) {
                    JOptionPane.showMessageDialog(null, "Usuario encontrado:\n" + encontrado);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro ese usuario.");
                }
                break;

            case "3":
                JOptionPane.showMessageDialog(null, usuarios.listarEnOrden());
                break;

            default:
                break;
        }
    }

    // ---------------------------------------------------------------
    // SUBMENU 5: CATEGORIAS DE LIBROS (Grafo) - version inicial
    // ---------------------------------------------------------------
    private static void menuCategorias() {
        String submenu = "----- Categorias de libros -----\n"
                + "1. Relacionar dos categorias\n"
                + "2. Ver relaciones registradas\n"
                + "0. Volver";

        String entrada = JOptionPane.showInputDialog(null, submenu);
        if (entrada == null) return;

        switch (entrada.trim()) {
            case "1":
                String origen = JOptionPane.showInputDialog("Categoria de origen:");
                String destino = JOptionPane.showInputDialog("Categoria relacionada:");
                categorias.relacionarCategorias(origen, destino);
                JOptionPane.showMessageDialog(null, "Relacion registrada.");
                break;

            case "2":
                JOptionPane.showMessageDialog(null, categorias.listarRelaciones());
                break;

            default:
                break;
        }
    }

    // Carga algunos datos iniciales para poder probar el sistema
    // apenas se abre (util para la grabacion de evidencia en video)
    private static void cargarDatosDePrueba() {
        catalogo.insertar(new Libro("L001", "Estructuras de Datos en Java", "R. Lafore", "Programacion"));
        catalogo.insertar(new Libro("L002", "Calculo I", "J. Stewart", "Matematicas"));
        historial.apilar(new Transaccion("ALTA_LIBRO", "L001", "Carga inicial del sistema."));
        categorias.agregarCategoria("Programacion");
        categorias.agregarCategoria("Matematicas");
    }
}
