package fideteca.modelo;

/**
 * Clase Reserva.
 *
 * Representa la reserva de un libro realizada por un usuario
 * registrado en el sistema.
 *
 * Los objetos de esta clase se almacenan en la cola de reservas,
 * respetando el orden de llegada de los usuarios.
 *
 * @author Luis
 */
public class Reserva {

    private String codigoLibro;
    private String tituloLibro;
    private Usuario usuario;

    /**
     * Construye una nueva reserva.
     *
     * @param codigoLibro código del libro reservado
     * @param tituloLibro título del libro reservado
     * @param usuario usuario que realiza la reserva
     */
    public Reserva(
            String codigoLibro,
            String tituloLibro,
            Usuario usuario
    ) {
        this.codigoLibro = codigoLibro;
        this.tituloLibro = tituloLibro;
        this.usuario = usuario;
    }

    public String getCodigoLibro() {
        return codigoLibro;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "Libro: [" + codigoLibro + "] "
                + tituloLibro
                + " | Usuario: "
                + usuario.getNombre()
                + " [" + usuario.getCarne() + "]";
    }
}