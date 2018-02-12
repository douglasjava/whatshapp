package br.com.firebase.whatsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.beans.Usuario;
import br.com.firebase.whatsapp.exceptions.CampoVazioException;
import br.com.firebase.whatsapp.exceptions.ConexaoException;
import br.com.firebase.whatsapp.exceptions.ValidarUsuarioException;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getAutenticacao;
import static br.com.firebase.whatsapp.utils.Utils.textView;
import static br.com.firebase.whatsapp.utils.Utils.validaPermissoes;
import static br.com.firebase.whatsapp.utils.Utils.validarCampoPreenchido;
import static br.com.firebase.whatsapp.utils.Utils.verificarConexao;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText senha;
    private Button logar;
    private Usuario usuario;
    private FirebaseAuth auth;
    private View progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validaPermissoes(context(), this);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.edtEmail);
        senha = (EditText) findViewById(R.id.edtSenha);

        logar = (Button) findViewById(R.id.btnlogar);
        logar.setOnClickListener(this);
    }


    public Context context() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogar:
                try {

                    progressDialog = findViewById(R.id.progressLogin);
                    progressDialog.setVisibility(View.VISIBLE);

                    verificarConexao(context());
                    validarCampoPreenchido(textView(email), textView(senha));
                    usuario = new Usuario(textView(email), textView(senha));
                    validarLogin();

                    progressDialog.setVisibility(View.GONE);

                } catch (CampoVazioException e) {
                    Toast.makeText(context(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ValidarUsuarioException e) {
                    Toast.makeText(context(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ConexaoException e) {
                    Toast.makeText(context(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void verificarUsuarioLogado() {
        auth = getAutenticacao();
        if (auth.getCurrentUser() != null) {
            openWindowMain();
        }
    }

    private void validarLogin() throws ValidarUsuarioException {

        try {
            auth = getAutenticacao();
            auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                openWindowMain();
                                Toast.makeText(context(), "Usuário logado com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                tratamentoExcecaoLogar(task);
                            }
                        }
                    });

        } catch (Exception e) {
            Log.e("ERRO_LOGAR", e.getMessage());
            throw new ValidarUsuarioException("Falha ao validar usuário");
        }
    }

    private void openWindowMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

        Toast.makeText(context(), ex, Toast.LENGTH_LONG).show();
    }

    public void abrirCadastroUsuario(View view) {
        startActivity(new Intent(LoginActivity.this, CadastroUsuarioActivity.class));
    }
}
