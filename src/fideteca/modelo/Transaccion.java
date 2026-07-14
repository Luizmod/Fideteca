package fideteca.modelo;

/**
 * Clase Transaccion
 * Representa un evento del sistema (prestamo, devolucion, reserva).
 * Estos objetos se apilan en la estructura Pila para llevar
 * el historial de operaciones (la ultima transaccion queda "arriba").
 */
public class Transaccion {

    private String tipo;        // "PRESTAMO", "DEVOLUCION", "RESERVA"
    private String codigoLibro;
    private String detalle;

    public Transaccion(String tipo, String codigoLibro, String detalle) {
        this.tipo = tipo;
        this.codigoLibro = codigoLibro;
        this.detalle = detalle;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCodigoLibro() {
        return codigoLibro;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        return "[" + tipo + "] Libro: " + codigoLibro + " - " + detalle;
    }
}
