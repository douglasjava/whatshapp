package br.com.firebase.whatsapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.beans.Usuario;
import br.com.firebase.whatsapp.exceptions.CampoVazioException;
import br.com.firebase.whatsapp.exceptions.ConexaoException;
import br.com.firebase.whatsapp.utils.Childs;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getAutenticacao;
import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getFirebase;
import static br.com.firebase.whatsapp.utils.Convert64Base.decode;
import static br.com.firebase.whatsapp.utils.Convert64Base.encode;
import static br.com.firebase.whatsapp.utils.Utils.getTextView;
import static br.com.firebase.whatsapp.utils.Utils.salvarUsuarioLogado;
import static br.com.firebase.whatsapp.utils.Utils.validaPermissoes;
import static br.com.firebase.whatsapp.utils.Utils.validarCampoPreenchido;
import static br.com.firebase.whatsapp.utils.Utils.verificarConexao;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText senha;
    private Button logar;
    private Usuario usuario;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private ValueEventListener valueEventListenerLogin;
    private Dialog load;


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
                    verificarConexao(context());
                    validarCampoPreenchido(getTextView(email), getTextView(senha));
                    usuario = new Usuario(getTextView(email), getTextView(senha));
                    this.auth = getAutenticacao();
                    auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        getNomeUsuario(decode(usuario.getEmail()));
                                        openWindowMain();
                                        Toast.makeText(context(), "Usuário logado com sucesso!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        tratamentoExcecaoLogar(task);
                                    }
                                }
                            });
                } catch (CampoVazioException e) {
                    Toast.makeText(context(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ConexaoException e) {
                    Toast.makeText(context(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
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

    private void verificarUsuarioLogado() {
        auth = getAutenticacao();
        if (auth.getCurrentUser() != null) {
            usuario = new Usuario(auth.getCurrentUser().getEmail());
            getNomeUsuario(encode(usuario.getEmail()));
            openWindowMain();
        }
    }

    private void getNomeUsuario(String idUsuario) {
        database = getFirebase().child(Childs.CHILD_USUARIOS).child(idUsuario);
        database.addListenerForSingleValueEvent(getValueEventListenerLogin());
    }

    private ValueEventListener getValueEventListenerLogin() {
        return valueEventListenerLogin = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                salvarUsuarioLogado(context(), usuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void openWindowMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    public void abrirCadastroUsuario(View view) {
        startActivity(new Intent(LoginActivity.this, CadastroUsuarioActivity.class));
    }


}
