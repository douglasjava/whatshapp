package br.com.firebase.whatsapp.exceptions;

/**
 * Created by Dias on 07/02/2018.
 */

public class EnviarSmsExecption extends Exception{


    public EnviarSmsExecption() {
        super();
    }

    public EnviarSmsExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public EnviarSmsExecption(String message) {
        super(message);
    }

    public EnviarSmsExecption(Throwable cause) {
        super(cause);
    }

}
