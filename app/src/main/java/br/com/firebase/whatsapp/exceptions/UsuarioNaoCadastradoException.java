package br.com.firebase.whatsapp.exceptions;

/**
 * Created by Marques on 15/02/2018.
 */

public class UsuarioNaoCadastradoException extends Exception {
    public UsuarioNaoCadastradoException(String mensagem) {
        super(mensagem);
    }
}
