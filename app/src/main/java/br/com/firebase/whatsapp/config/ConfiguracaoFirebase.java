package br.com.firebase.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Dias on 08/02/2018.
 */

public final class ConfiguracaoFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    public static FirebaseAuth getAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}
