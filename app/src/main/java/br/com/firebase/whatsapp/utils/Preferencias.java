package br.com.firebase.whatsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Dias on 07/02/2018.
 */

public class Preferencias {

    private String NOME_ARQUIVO = "whatsapp.preferencias";
    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferencias(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String usuario, String telefone, String token) {

        editor.putString(CHAVE_NOME, usuario)
                .putString(CHAVE_TELEFONE, telefone)
                .putString(CHAVE_TOKEN, token)
                .commit();
    }

    public HashMap<String, String> getDadosUsuarios(){

        HashMap<String, String> dadosUsuarios = new HashMap<String, String>();
        dadosUsuarios.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosUsuarios.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadosUsuarios.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadosUsuarios;
    }
}
