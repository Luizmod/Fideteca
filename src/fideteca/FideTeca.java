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
import excepciones.CampoVacioException;

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

    static ListaEnlazada<Libro> catalogo = new ListaEnlazada<>();
    static Pila<Transaccion> historial = new Pila<>();
    static Cola<String> reservas = new Cola<>();
    static ArbolBinarioBusqueda usuarios = new ArbolBinarioBusqueda();
    static GrafoDirigido categorias = new GrafoDirigido();

    public static void main(String[] args) {

        cargarDatosDePrueba();

        int opcion;
        do {
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
                case 1: menuCatalogo(); break;
                case 2: menuHistorial(); break;
                case 3: menuReservas(); break;
                case 4: menuUsuarios(); break;
                case 5: menuCategorias(); break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Gracias por usar FideTeca.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opcion no valida.");
            }

        } while (opcion_valida());
    }

    private static boolean opcion_valida() {
        return true;
    }

    // Metodo auxiliar: valida que un texto no venga nulo ni vacio
    private static void validarCampo(String valor, String nombreCampo) throws CampoVacioException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new CampoVacioException(nombreCampo);
        }
    }

    // ---------------------------------------------------------------
    // SUBMENU 1: CATALOGO DE LIBROS
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
                try {
                    String codigo = JOptionPane.showInputDialog("Codigo del libro (ej: L001):");
                    validarCampo(codigo, "Codigo");

                    String titulo = JOptionPane.showInputDialog("Titulo:");
                    validarCampo(titulo, "Titulo");

                    String autor = JOptionPane.showInputDialog("Autor:");
                    validarCampo(autor, "Autor");

                    String categoria = JOptionPane.showInputDialog("Categoria:");
                    validarCampo(categoria, "Categoria");

                    Libro nuevo = new Libro(codigo, titulo, autor, categoria);
                    catalogo.insertar(nuevo);

                    historial.apilar(new Transaccion("ALTA_LIBRO", codigo,
                            "Se agrego el libro '" + titulo + "' al catalogo."));

                    categorias.agregarCategoria(categoria);

                    JOptionPane.showMessageDialog(null, "Libro agregado correctamente.");
                } catch (CampoVacioException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
                break;

            case "2":
                try {
                    String codigoEliminar = JOptionPane.showInputDialog("Codigo del libro a eliminar:");
                    validarCampo(codigoEliminar, "Codigo");

                    boolean eliminado = catalogo.eliminarPorCodigo(codigoEliminar);

                    if (eliminado) {
                        historial.apilar(new Transaccion("BAJA_LIBRO", codigoEliminar,
                                "Se elimino el libro del catalogo."));
                        JOptionPane.showMessageDialog(null, "Libro eliminado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontro un libro con ese codigo.");
                    }
                } catch (CampoVacioException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
                break;

            case "3":
                JOptionPane.showMessageDialog(null, catalogo.listarTodo());
                break;

            default:
                break;
        }
    }

    // ---------------------------------------------------------------
    // SUBMENU 2: HISTORIAL DE TRANSACCIONES
    // ---------------------------------------------------------------
    private static void menuHistorial() {
        String submenu = "----- Historial de transacciones -----\n"
                + "1. Ver ultima transaccion\n"
                + "2. Deshacer ultima transaccion\n"
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
    // SUBMENU 3: RESERVAS DE LIBROS AGOTADOS
    // ---------------------------------------------------------------
    private static void menuReservas() {
        String submenu = "----- Reservas de libros -----\n"
                + "1. Reservar un libro\n"
                + "2. Atender siguiente reserva\n"
                + "3. Ver fila de reservas\n"
                + "0. Volver";

        String entrada = JOptionPane.showInputDialog(null, submenu);
        if (entrada == null) return;

        switch (entrada.trim()) {
        case "1":
            if (catalogo.estaVacia()) {
                JOptionPane.showMessageDialog(null, "No hay libros registrados en el catalogo.");
                break;
            }

            Object[] opcionesLibros = catalogo.toArray();
            Object seleccion = JOptionPane.showInputDialog(
                    null,
                    "Seleccione el libro a reservar:",
                    "Reservar libro",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesLibros,
                    opcionesLibros[0]);

            if (seleccion == null) {
                break;
            }

            String textoSeleccionado = seleccion.toString();
            String codigoLibroReserva = textoSeleccionado.substring(
                    textoSeleccionado.indexOf("[") + 1,
                    textoSeleccionado.indexOf("]"));

            try {
                if (!catalogo.existeCodigo(codigoLibroReserva)) {
                    throw new LibroNoEncontradoException(codigoLibroReserva);
                }

                String nombreUsuario = JOptionPane.showInputDialog("Nombre del usuario que reserva:");
                validarCampo(nombreUsuario, "Nombre");

                String datoReserva = codigoLibroReserva + " - " + nombreUsuario;
                reservas.encolar(datoReserva);

                historial.apilar(new Transaccion("RESERVA", codigoLibroReserva,
                        nombreUsuario + " reservo el libro " + codigoLibroReserva + "."));
                JOptionPane.showMessageDialog(null, "Reserva registrada:\n" + datoReserva);

            }   catch (LibroNoEncontradoException | CampoVacioException e) {
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
            
        case "3":
            JOptionPane.showMessageDialog(null, reservas.listarTodo());
            break;

        default:
            break;
        }
    }



    // ---------------------------------------------------------------
    // SUBMENU 4: REGISTRO DE USUARIOS
    // ---------------------------------------------------------------
    private static void menuUsuarios() {
        String submenu = "----- Registro de usuarios -----\n"
                + "1. Registrar usuario\n"
                + "2. Buscar usuario por carne\n"
                + "3. Ver todos los usuarios\n"
                + "0. Volver";

        String entrada = JOptionPane.showInputDialog(null, submenu);
        if (entrada == null) return;

        switch (entrada.trim()) {
            case "1":
                try {
                    String carne = JOptionPane.showInputDialog("Carne del usuario:");
                    validarCampo(carne, "Carne");

                    String nombre = JOptionPane.showInputDialog("Nombre:");
                    validarCampo(nombre, "Nombre");

                    String tipo = JOptionPane.showInputDialog("Tipo (Estudiante/Profesor):");
                    validarCampo(tipo, "Tipo");

                    usuarios.insertar(new Usuario(carne, nombre, tipo));
                    JOptionPane.showMessageDialog(null, "Usuario registrado.");
                }   catch (CampoVacioException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
                break;

            case "2":
                try {
                    String carneBuscar = JOptionPane.showInputDialog("Carne a buscar:");
                    validarCampo(carneBuscar, "Carne");

                    Usuario encontrado = usuarios.buscar(carneBuscar);
                    if (encontrado != null) {
                        JOptionPane.showMessageDialog(null, "Usuario encontrado:\n" + encontrado);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontro ese usuario.");
                    }
                } catch (CampoVacioException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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
    // SUBMENU 5: CATEGORIAS DE LIBROS
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
                try {
                    String origen = JOptionPane.showInputDialog("Categoria de origen:");
                    validarCampo(origen, "Categoria de origen");

                    String destino = JOptionPane.showInputDialog("Categoria relacionada:");
                    validarCampo(destino, "Categoria relacionada");

                    categorias.relacionarCategorias(origen, destino);
                    JOptionPane.showMessageDialog(null, "Relacion registrada.");
                } catch (CampoVacioException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
                break;

            case "2":
                JOptionPane.showMessageDialog(null, categorias.listarRelaciones());
                break;

            default:
                break;
        }
    }

    private static void cargarDatosDePrueba() {
        catalogo.insertar(new Libro("L001", "Estructuras de Datos en Java", "R. Lafore", "Programacion"));
        catalogo.insertar(new Libro("L002", "Calculo I", "J. Stewart", "Matematicas"));
        historial.apilar(new Transaccion("ALTA_LIBRO", "L001", "Carga inicial del sistema."));
        categorias.agregarCategoria("Programacion");
        categorias.agregarCategoria("Matematicas");
    }
}