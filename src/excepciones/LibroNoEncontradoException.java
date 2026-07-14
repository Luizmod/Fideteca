/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Excepcion personalizada que se lanza cuando se busca un libro
 * por un codigo que no existe en el catalogo.
 */
public class LibroNoEncontradoException extends Exception {

    public LibroNoEncontradoException(String codigo) {
        super("No existe ningun libro con el codigo: " + codigo);
    }
}
