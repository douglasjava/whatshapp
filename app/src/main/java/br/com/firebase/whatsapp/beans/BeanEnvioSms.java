package br.com.firebase.whatsapp.beans;

/**
 * Created by Dias on 07/02/2018.
 */

public class BeanEnvioSms {

    private String telefone;
    private String mensagem;
    private String token;
    private Boolean isEnviado;

    public BeanEnvioSms(String telefone, String mensagem, String token) {
        this.telefone = telefone;
        this.mensagem = mensagem;
        this.token = token;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getEnviado() {
        return isEnviado;
    }

    public void setEnviado(Boolean enviado) {
        isEnviado = enviado;
    }
}
