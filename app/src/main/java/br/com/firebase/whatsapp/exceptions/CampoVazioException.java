package br.com.firebase.whatsapp.exceptions;

/**
 * Created by Dias on 07/02/2018.
 */

public class CampoVazioException extends Exception {

    public CampoVazioException() {
        super();
    }

    public CampoVazioException(String message, Throwable cause) {
        super(message, cause);
    }

    public CampoVazioException(String message) {
        super(message);
    }

    public CampoVazioException(Throwable cause) {
        super(cause);
    }
}
