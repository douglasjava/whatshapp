package br.com.firebase.whatsapp.services;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.firebase.whatsapp.beans.Usuario;
import br.com.firebase.whatsapp.exceptions.LoginException;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getAutenticacao;

/**
 * Created by Marques on 19/02/2018.
 */

public class LoginService extends AsyncTask<Usuario, Void, Boolean> {

    private Context context;
    private LoginCallback loginCallback;
    private Dialog load;
    private FirebaseAuth auth;
    private LoginException ex;
    private Boolean retorno = Boolean.FALSE;

    public LoginService(Context context, FirebaseAuth auth, LoginCallback loginCallback) {
        this.context = context;
        this.auth = auth;
        this.loginCallback = loginCallback;
    }


    /**
     * É executado sempre antes da Thread ser iniciada, na principal
     **/
    @Override
    protected void onPreExecute() {
        load = ProgressDialog.show(context, "Aguarde", "Conectando...");
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Usuario... usuario) {

        try {
            this.auth = getAutenticacao();
            auth.signInWithEmailAndPassword(usuario[0].getEmail(), usuario[0].getSenha())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                retorno = true;
                            } else {
                                retorno = false;
                            }
                        }
                    });

        } catch (Exception e) {
            this.ex = new LoginException(e.getMessage());
        }

        loginCallback.onSuccess(retorno);
        return retorno;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (!aBoolean) {
            Toast.makeText(context, "Usuário ou senha são inválidos", Toast.LENGTH_SHORT).show();
        }
        if (this.ex != null) {
            Toast.makeText(context, this.ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        load.dismiss();
        super.onPostExecute(aBoolean);
    }

    private void tratamentoExcecaoLogar(Task<AuthResult> task) {
        String ex = "";

        try {
            throw task.getException();
        } catch (FirebaseAuthInvalidUserException e) {
            ex = "O e-mail digitado não existe ou está desabilitado!";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            ex = "Senha incorreta!";
        } catch (Exception e) {
            ex = "Erro ao logar, favor entrar em contato com o suporte";
            Log.e("ERRO_LOGAR", "Falha ao logar com erro: " + e.getMessage());
        }
    }

    public interface LoginCallback {
        void onSuccess(Boolean retorno);
    }

}
