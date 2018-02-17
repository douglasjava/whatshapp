package br.com.firebase.whatsapp.beans;

/**
 * Created by Marques on 17/02/2018.
 */

public class Conversa {

    private String idUsuario;
    private String nome;
    private String mensagem;

    public Conversa() {
    }

    public Conversa(String idUsuario, String nome, String mensagem) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.mensagem = mensagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
