package fideteca;

import excepciones.CampoVacioException;
import fideteca.estructuras.ArbolBinarioBusqueda;
import fideteca.estructuras.Cola;
import fideteca.estructuras.GrafoDirigido;
import fideteca.estructuras.ListaEnlazada;
import fideteca.estructuras.Pila;
import fideteca.modelo.Libro;
import fideteca.modelo.Reserva;
import fideteca.modelo.Transaccion;
import fideteca.modelo.Usuario;
import javax.swing.JOptionPane;

/**
 * Clase principal del sistema FideTeca.
 *
 * Estructuras utilizadas:
 *
 * Catálogo de libros: lista enlazada.
 * Historial: pila.
 * Reservas: cola.
 * Usuarios: árbol binario de búsqueda.
 * Categorías: grafo dirigido.
 *
 * @author Luis
 */
public class FideTeca {

    private static final ListaEnlazada<Libro> catalogo
            = new ListaEnlazada<>();

    private static final Pila<Transaccion> historial
            = new Pila<>();

    private static final Cola<Reserva> reservas
            = new Cola<>();

    private static final ArbolBinarioBusqueda usuarios
            = new ArbolBinarioBusqueda();

    private static final GrafoDirigido categorias
            = new GrafoDirigido();

    /**
     * Punto de entrada del programa.
     *
     * @param args argumentos de ejecución
     */
    public static void main(String[] args) {
        cargarDatosDePrueba();

        int opcion;

        do {
            opcion = mostrarMenuPrincipal();

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
                    JOptionPane.showMessageDialog(
                            null,
                            "Gracias por usar FideTeca."
                    );
                    break;

                default:
                    mostrarOpcionInvalida();
            }

        } while (opcion != 0);
    }

    /**
     * Muestra el menú principal.
     *
     * @return opción seleccionada
     */
    private static int mostrarMenuPrincipal() {
        String menu
                = "===== FIDETECA =====\n\n"
                + "1. Catálogo de libros\n"
                + "2. Historial de transacciones\n"
                + "3. Reservas de libros\n"
                + "4. Registro de usuarios\n"
                + "5. Categorías de libros\n"
                + "0. Salir\n\n"
                + "Seleccione una opción:";

        String entrada = JOptionPane.showInputDialog(
                null,
                menu,
                "FideTeca",
                JOptionPane.PLAIN_MESSAGE
        );

        if (entrada == null) {
            return 0;
        }

        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe ingresar una opción numérica."
            );

            return -1;
        }
    }

    /**
     * Valida que un campo tenga contenido.
     *
     * @param valor valor ingresado
     * @param nombreCampo nombre del campo
     * @throws CampoVacioException si el valor está vacío
     */
    private static void validarCampo(
            String valor,
            String nombreCampo
    ) throws CampoVacioException {

        if (valor == null || valor.trim().isEmpty()) {
            throw new CampoVacioException(nombreCampo);
        }
    }

    // =============================================================
    // CATÁLOGO
    // =============================================================

    private static void menuCatalogo() {
        String opcion;

        do {
            String submenu
                    = "----- CATÁLOGO DE LIBROS -----\n\n"
                    + "1. Agregar libro\n"
                    + "2. Eliminar libro\n"
                    + "3. Ver catálogo completo\n"
                    + "0. Volver";

            opcion = JOptionPane.showInputDialog(
                    null,
                    submenu,
                    "Catálogo",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                return;
            }

            switch (opcion.trim()) {
                case "1":
                    agregarLibro();
                    break;

                case "2":
                    eliminarLibro();
                    break;

                case "3":
                    JOptionPane.showMessageDialog(
                            null,
                            catalogo.listarTodo(),
                            "Catálogo",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;

                case "0":
                    break;

                default:
                    mostrarOpcionInvalida();
            }

        } while (!opcion.trim().equals("0"));
    }

    /**
     * Registra un nuevo libro.
     */
    private static void agregarLibro() {
        try {
            String codigo = JOptionPane.showInputDialog(
                    "Código del libro:"
            );

            if (codigo == null) {
                return;
            }

            validarCampo(codigo, "Código");
            codigo = codigo.trim().toUpperCase();

            if (catalogo.existeCodigo(codigo)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Ya existe un libro con el código "
                        + codigo + "."
                );
                return;
            }

            String titulo = JOptionPane.showInputDialog(
                    "Título del libro:"
            );

            if (titulo == null) {
                return;
            }

            validarCampo(titulo, "Título");

            String autor = JOptionPane.showInputDialog(
                    "Autor del libro:"
            );

            if (autor == null) {
                return;
            }

            validarCampo(autor, "Autor");

            String categoria = seleccionarCategoria();

            if (categoria == null) {
                return;
            }

            Libro nuevoLibro = new Libro(
                    codigo,
                    titulo.trim(),
                    autor.trim(),
                    categoria
            );

            catalogo.insertar(nuevoLibro);

            historial.apilar(
                    new Transaccion(
                            "ALTA_LIBRO",
                            codigo,
                            "Se agregó el libro '"
                            + titulo.trim()
                            + "' al catálogo."
                    )
            );

            JOptionPane.showMessageDialog(
                    null,
                    "Libro agregado correctamente:\n\n"
                    + nuevoLibro
            );

        } catch (CampoVacioException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Permite seleccionar una categoría o registrar una nueva.
     *
     * @return categoría seleccionada
     */
    private static String seleccionarCategoria() {
        Object[] categoriasRegistradas
                = categorias.obtenerCategorias();

        Object[] opciones
                = new Object[categoriasRegistradas.length + 1];

        for (int i = 0;
                i < categoriasRegistradas.length;
                i++) {

            opciones[i] = categoriasRegistradas[i];
        }

        opciones[opciones.length - 1]
                = "Registrar nueva categoría";

        Object seleccion = JOptionPane.showInputDialog(
                null,
                "Seleccione la categoría del libro:",
                "Categoría",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) {
            return null;
        }

        if (seleccion.toString().equals(
                "Registrar nueva categoría"
        )) {
            return registrarNuevaCategoria();
        }

        return seleccion.toString();
    }

    /**
     * Registra una categoría.
     *
     * @return categoría registrada o encontrada
     */
    private static String registrarNuevaCategoria() {
        String nuevaCategoria = JOptionPane.showInputDialog(
                null,
                "Ingrese el nombre de la categoría:",
                "Nueva categoría",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nuevaCategoria == null) {
            return null;
        }

        nuevaCategoria = nuevaCategoria.trim();

        if (nuevaCategoria.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "El nombre no puede estar vacío."
            );
            return null;
        }

        String existente = categorias.buscarCategoria(
                nuevaCategoria
        );

        if (existente != null) {
            JOptionPane.showMessageDialog(
                    null,
                    "La categoría ya estaba registrada."
            );

            return existente;
        }

        categorias.agregarCategoria(nuevaCategoria);

        return nuevaCategoria;
    }

    /**
     * Elimina un libro mediante un menú desplegable.
     */
    private static void eliminarLibro() {
        if (catalogo.estaVacia()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay libros registrados."
            );
            return;
        }

        Object[] libros = catalogo.toArray();

        Object seleccion = JOptionPane.showInputDialog(
                null,
                "Seleccione el libro que desea eliminar:",
                "Eliminar libro",
                JOptionPane.WARNING_MESSAGE,
                null,
                libros,
                libros[0]
        );

        if (seleccion == null) {
            return;
        }

        String codigo = extraerCodigo(
                seleccion.toString()
        );

        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Desea eliminar este libro?\n\n"
                + seleccion,
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado
                = catalogo.eliminarPorCodigo(codigo);

        if (eliminado) {
            historial.apilar(
                    new Transaccion(
                            "BAJA_LIBRO",
                            codigo,
                            "Se eliminó el libro del catálogo."
                    )
            );

            JOptionPane.showMessageDialog(
                    null,
                    "Libro eliminado correctamente."
            );
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "No fue posible eliminar el libro."
            );
        }
    }

    // =============================================================
    // HISTORIAL
    // =============================================================

    private static void menuHistorial() {
        String opcion;

        do {
            String submenu
                    = "----- HISTORIAL -----\n\n"
                    + "1. Ver última transacción\n"
                    + "2. Retirar última transacción\n"
                    + "3. Ver historial completo\n"
                    + "0. Volver";

            opcion = JOptionPane.showInputDialog(
                    null,
                    submenu,
                    "Historial",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                return;
            }

            switch (opcion.trim()) {
                case "1":
                    Transaccion ultima = historial.verTope();

                    if (ultima == null) {
                        JOptionPane.showMessageDialog(
                                null,
                                "No hay transacciones."
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Última transacción:\n\n"
                                + ultima
                        );
                    }
                    break;

                case "2":
                    retirarUltimaTransaccion();
                    break;

                case "3":
                    JOptionPane.showMessageDialog(
                            null,
                            historial.listarTodo(),
                            "Historial",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;

                case "0":
                    break;

                default:
                    mostrarOpcionInvalida();
            }

        } while (!opcion.trim().equals("0"));
    }

    private static void retirarUltimaTransaccion() {
        Transaccion ultima = historial.verTope();

        if (ultima == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay transacciones para retirar."
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Desea retirar esta transacción?\n\n"
                + ultima,
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        Transaccion retirada = historial.desapilar();

        JOptionPane.showMessageDialog(
                null,
                "Transacción retirada:\n\n"
                + retirada
        );
    }

    // =============================================================
    // RESERVAS
    // =============================================================

    private static void menuReservas() {
        String opcion;

        do {
            String submenu
                    = "----- RESERVAS -----\n\n"
                    + "1. Reservar un libro\n"
                    + "2. Atender siguiente reserva\n"
                    + "3. Ver fila de reservas\n"
                    + "0. Volver";

            opcion = JOptionPane.showInputDialog(
                    null,
                    submenu,
                    "Reservas",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                return;
            }

            switch (opcion.trim()) {
                case "1":
                    reservarLibro();
                    break;

                case "2":
                    atenderReserva();
                    break;

                case "3":
                    JOptionPane.showMessageDialog(
                            null,
                            reservas.listarTodo(),
                            "Fila de reservas",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;

                case "0":
                    break;

                default:
                    mostrarOpcionInvalida();
            }

        } while (!opcion.trim().equals("0"));
    }

    /**
     * Registra una reserva seleccionando el libro y el usuario.
     */
    private static void reservarLibro() {
        if (catalogo.estaVacia()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay libros registrados."
            );
            return;
        }

        Object[] libros = catalogo.toArray();

        Object seleccionLibro = JOptionPane.showInputDialog(
                null,
                "Seleccione el libro:",
                "Reservar libro",
                JOptionPane.QUESTION_MESSAGE,
                null,
                libros,
                libros[0]
        );

        if (seleccionLibro == null) {
            return;
        }

        String textoLibro = seleccionLibro.toString();
        String codigoLibro = extraerCodigo(textoLibro);
        String tituloLibro = extraerTitulo(textoLibro);

        Usuario usuarioSeleccionado
                = seleccionarUsuarioParaReserva();

        if (usuarioSeleccionado == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Desea registrar la reserva?\n\n"
                + "Libro: " + textoLibro + "\n"
                + "Usuario: " + usuarioSeleccionado,
                "Confirmar reserva",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        Reserva nuevaReserva = new Reserva(
                codigoLibro,
                tituloLibro,
                usuarioSeleccionado
        );

        reservas.encolar(nuevaReserva);

        historial.apilar(
                new Transaccion(
                        "RESERVA",
                        codigoLibro,
                        usuarioSeleccionado.getNombre()
                        + " reservó el libro "
                        + codigoLibro + "."
                )
        );

        JOptionPane.showMessageDialog(
                null,
                "Reserva registrada correctamente:\n\n"
                + nuevaReserva
        );
    }

    /**
     * Permite seleccionar un usuario o registrar uno nuevo.
     *
     * @return usuario seleccionado
     */
    private static Usuario seleccionarUsuarioParaReserva() {
        Object[] registrados = usuarios.obtenerUsuarios();

        Object[] opciones
                = new Object[registrados.length + 1];

        for (int i = 0; i < registrados.length; i++) {
            opciones[i] = registrados[i];
        }

        opciones[opciones.length - 1]
                = "Registrar nuevo usuario";

        Object seleccion = JOptionPane.showInputDialog(
                null,
                "Seleccione el usuario que realizará la reserva:",
                "Usuario de la reserva",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) {
            return null;
        }

        if (seleccion.toString().equals(
                "Registrar nuevo usuario"
        )) {
            return registrarUsuario();
        }

        return (Usuario) seleccion;
    }

    /**
     * Atiende la primera reserva de la cola.
     */
    private static void atenderReserva() {
        if (reservas.estaVacia()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay reservas pendientes."
            );
            return;
        }

        Reserva atendida = reservas.desencolar();

        JOptionPane.showMessageDialog(
                null,
                "Reserva atendida:\n\n"
                + atendida
        );

        historial.apilar(
                new Transaccion(
                        "RESERVA_ATENDIDA",
                        atendida.getCodigoLibro(),
                        "Se atendió la reserva de "
                        + atendida.getUsuario().getNombre()
                        + "."
                )
        );
    }

    // =============================================================
    // USUARIOS
    // =============================================================

    private static void menuUsuarios() {
        String opcion;

        do {
            String submenu
                    = "----- USUARIOS -----\n\n"
                    + "1. Registrar usuario\n"
                    + "2. Buscar usuario por carné\n"
                    + "3. Ver todos los usuarios\n"
                    + "0. Volver";

            opcion = JOptionPane.showInputDialog(
                    null,
                    submenu,
                    "Usuarios",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                return;
            }

            switch (opcion.trim()) {
                case "1":
                    registrarUsuario();
                    break;

                case "2":
                    buscarUsuario();
                    break;

                case "3":
                    JOptionPane.showMessageDialog(
                            null,
                            usuarios.listarEnOrden(),
                            "Usuarios",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;

                case "0":
                    break;

                default:
                    mostrarOpcionInvalida();
            }

        } while (!opcion.trim().equals("0"));
    }

    /**
     * Registra un usuario y devuelve el objeto creado.
     *
     * @return usuario registrado o null
     */
    private static Usuario registrarUsuario() {
        try {
            String carne = JOptionPane.showInputDialog(
                    "Carné del usuario:"
            );

            if (carne == null) {
                return null;
            }

            validarCampo(carne, "Carné");
            carne = carne.trim().toUpperCase();

            Usuario existente = usuarios.buscar(carne);

            if (existente != null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Ya existe un usuario con ese carné."
                );

                return existente;
            }

            String nombre = JOptionPane.showInputDialog(
                    "Nombre completo:"
            );

            if (nombre == null) {
                return null;
            }

            validarCampo(nombre, "Nombre");

            String[] tipos = {
                "Estudiante",
                "Profesor",
                "Administrativo"
            };

            String tipo = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el tipo de usuario:",
                    "Tipo de usuario",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    tipos,
                    tipos[0]
            );

            if (tipo == null) {
                return null;
            }

            Usuario nuevoUsuario = new Usuario(
                    carne,
                    nombre.trim(),
                    tipo
            );

            usuarios.insertar(nuevoUsuario);

            JOptionPane.showMessageDialog(
                    null,
                    "Usuario registrado correctamente:\n\n"
                    + nuevoUsuario
            );

            return nuevoUsuario;

        } catch (CampoVacioException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: " + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Busca un usuario mediante el carné.
     */
    private static void buscarUsuario() {
        try {
            String carne = JOptionPane.showInputDialog(
                    "Ingrese el carné:"
            );

            if (carne == null) {
                return;
            }

            validarCampo(carne, "Carné");

            Usuario encontrado = usuarios.buscar(
                    carne.trim()
            );

            if (encontrado == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "No se encontró el usuario."
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Usuario encontrado:\n\n"
                        + encontrado
                );
            }

        } catch (CampoVacioException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: " + e.getMessage()
            );
        }
    }

    // =============================================================
    // CATEGORÍAS
    // =============================================================

    private static void menuCategorias() {
        String opcion;

        do {
            String submenu
                    = "----- CATEGORÍAS -----\n\n"
                    + "1. Relacionar categorías\n"
                    + "2. Ver relaciones\n"
                    + "3. Registrar nueva categoría\n"
                    + "0. Volver";

            opcion = JOptionPane.showInputDialog(
                    null,
                    submenu,
                    "Categorías",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                return;
            }

            switch (opcion.trim()) {
                case "1":
                    relacionarCategorias();
                    break;

                case "2":
                    JOptionPane.showMessageDialog(
                            null,
                            categorias.listarRelaciones(),
                            "Relaciones",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;

                case "3":
                    String categoria
                            = registrarNuevaCategoria();

                    if (categoria != null) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Categoría disponible: "
                                + categoria
                        );
                    }
                    break;

                case "0":
                    break;

                default:
                    mostrarOpcionInvalida();
            }

        } while (!opcion.trim().equals("0"));
    }

    /**
     * Relaciona categorías mediante desplegables.
     */
    private static void relacionarCategorias() {
        if (categorias.cantidadCategorias() < 2) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe registrar al menos dos categorías."
            );
            return;
        }

        Object[] opcionesOrigen
                = categorias.obtenerCategorias();

        Object seleccionOrigen = JOptionPane.showInputDialog(
                null,
                "Seleccione la categoría de origen:",
                "Relacionar categorías",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesOrigen,
                opcionesOrigen[0]
        );

        if (seleccionOrigen == null) {
            return;
        }

        String origen = seleccionOrigen.toString();

        Object[] opcionesDestino
                = obtenerCategoriasExcepto(
                        opcionesOrigen,
                        origen
                );

        Object seleccionDestino = JOptionPane.showInputDialog(
                null,
                "Seleccione la categoría de destino:",
                "Relacionar categorías",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesDestino,
                opcionesDestino[0]
        );

        if (seleccionDestino == null) {
            return;
        }

        String destino = seleccionDestino.toString();

        boolean registrada
                = categorias.relacionarCategorias(
                        origen,
                        destino
                );

        if (registrada) {
            JOptionPane.showMessageDialog(
                    null,
                    "Relación registrada:\n\n"
                    + origen + " -> " + destino
            );
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "La relación ya existe o no es válida."
            );
        }
    }

    private static Object[] obtenerCategoriasExcepto(
            Object[] categoriasDisponibles,
            String categoriaExcluida
    ) {
        Object[] resultado
                = new Object[categoriasDisponibles.length - 1];

        int indice = 0;

        for (Object categoria : categoriasDisponibles) {
            if (!categoria.toString().equals(
                    categoriaExcluida
            )) {
                resultado[indice] = categoria;
                indice++;
            }
        }

        return resultado;
    }

    // =============================================================
    // MÉTODOS AUXILIARES
    // =============================================================

    private static String extraerCodigo(String texto) {
        if (texto == null) {
            return "";
        }

        int inicio = texto.indexOf("[");
        int fin = texto.indexOf("]");

        if (inicio == -1 || fin == -1 || inicio >= fin) {
            return "";
        }

        return texto.substring(
                inicio + 1,
                fin
        ).trim();
    }

    /**
     * Extrae el título del texto generado por Libro.toString().
     *
     * Formato esperado:
     * [L001] Título - Autor (Categoría...)
     *
     * @param texto texto del libro
     * @return título extraído
     */
    private static String extraerTitulo(String texto) {
        if (texto == null) {
            return "Título no disponible";
        }

        int cierreCodigo = texto.indexOf("]");
        int separadorAutor = texto.indexOf(" - ");

        if (cierreCodigo == -1
                || separadorAutor == -1
                || cierreCodigo >= separadorAutor) {

            return "Título no disponible";
        }

        return texto.substring(
                cierreCodigo + 1,
                separadorAutor
        ).trim();
    }

    private static void mostrarOpcionInvalida() {
        JOptionPane.showMessageDialog(
                null,
                "La opción seleccionada no es válida."
        );
    }

    /**
     * Carga información para probar el programa.
     */
    private static void cargarDatosDePrueba() {
        catalogo.insertar(
                new Libro(
                        "L001",
                        "Estructuras de Datos en Java",
                        "R. Lafore",
                        "Programación"
                )
        );

        catalogo.insertar(
                new Libro(
                        "L002",
                        "Cálculo I",
                        "J. Stewart",
                        "Matemáticas"
                )
        );

        categorias.agregarCategoria("Programación");
        categorias.agregarCategoria("Matemáticas");

        usuarios.insertar(
                new Usuario(
                        "E001",
                        "Ana Rodríguez",
                        "Estudiante"
                )
        );

        usuarios.insertar(
                new Usuario(
                        "P001",
                        "Carlos Vargas",
                        "Profesor"
                )
        );

        historial.apilar(
                new Transaccion(
                        "INICIO",
                        "L001",
                        "Carga inicial del sistema."
                )
        );
    }
}