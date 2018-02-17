package br.com.firebase.whatsapp.beans;

/**
 * Created by Marques on 17/02/2018.
 */

public class Mensagem {

    private String idUsuario;
    private String mensagem;

    public Mensagem() {
    }

    public Mensagem(String idUsuario, String mensagem) {
        this.idUsuario = idUsuario;
        this.mensagem = mensagem;
    }

    public void salvar() {

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public Mensagem setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Mensagem setMensagem(String mensagem) {
        this.mensagem = mensagem;
        return this;
    }
}
