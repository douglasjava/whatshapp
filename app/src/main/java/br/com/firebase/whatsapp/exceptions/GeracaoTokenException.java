package br.com.firebase.whatsapp.exceptions;

/**
 * Created by Dias on 04/02/2018.
 */

public class GeracaoTokenException extends RuntimeException {

    public GeracaoTokenException() {
        super();
    }

    public GeracaoTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeracaoTokenException(String message) {
        super(message);
    }

    public GeracaoTokenException(Throwable cause) {
        super(cause);
    }
}
