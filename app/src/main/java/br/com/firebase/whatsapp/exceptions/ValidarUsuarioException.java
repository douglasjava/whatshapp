package br.com.firebase.whatsapp.exceptions;

/**
 * Created by Dias on 09/02/2018.
 */

public class ValidarUsuarioException extends Exception {


    public ValidarUsuarioException() {
        super();
    }

    public ValidarUsuarioException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidarUsuarioException(String message) {
        super(message);
    }

    public ValidarUsuarioException(Throwable cause) {
        super(cause);
    }

}
