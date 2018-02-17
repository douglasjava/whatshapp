package br.com.firebase.whatsapp.beans;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.firebase.whatsapp.utils.Childs;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getFirebase;
import static br.com.firebase.whatsapp.utils.Childs.CHILD_USUARIOS;

/**
 * Created by Dias on 09/02/2018.
 */

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String email) {
        this.email = email;
    }

    public void salvar() {
        DatabaseReference database = getFirebase();
        database.child(CHILD_USUARIOS).child(getId()).setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public Usuario setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Usuario setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Usuario setEmail(String email) {
        this.email = email;
        return this;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public Usuario setSenha(String senha) {
        this.senha = senha;
        return this;
    }
}
