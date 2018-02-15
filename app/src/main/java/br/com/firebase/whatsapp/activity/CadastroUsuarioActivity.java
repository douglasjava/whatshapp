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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.beans.Usuario;
import br.com.firebase.whatsapp.exceptions.CampoVazioException;
import br.com.firebase.whatsapp.utils.Convert64Base;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getAutenticacao;
import static br.com.firebase.whatsapp.utils.Convert64Base.encode;
import static br.com.firebase.whatsapp.utils.Utils.textView;
import static br.com.firebase.whatsapp.utils.Utils.validarCampoPreenchido;

public class CadastroUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button cadastrar;
    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.edtCadastroNome);
        email = (EditText) findViewById(R.id.edtCadastroEmail);
        senha = (EditText) findViewById(R.id.edtCadastroSenha);

        cadastrar = (Button) findViewById(R.id.btnCadastrar);
        cadastrar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCadastrar:
                try {
                    usuario = new Usuario(textView(nome), textView(email), textView(senha));
                    validarCampoPreenchido(textView(nome), textView(email), textView(senha));
                    cadastrarUsuario();
                } catch (CampoVazioException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void cadastrarUsuario() {
        auth = getAutenticacao();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            salvarUsuario(task);
                            Toast.makeText(getContext(), "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                            openWindowMain();
                            finish();

                        } else {
                            tratamentoExcecaoCreate(task);
                        }
                    }
                });
    }

    private void openWindowMain() {
        startActivity(new Intent(CadastroUsuarioActivity.this, MainActivity.class));
    }

    private void tratamentoExcecaoCreate(Task<AuthResult> task) {
        String ex = "";

        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            ex = "Digite uma senha mais forte, contendo mais caracteres e com letras e números!";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            ex = "Digite um e-mail válido!";
        } catch (FirebaseAuthUserCollisionException e) {
            ex = "E-mail já cadastrado!";
        } catch (Exception e) {
            ex = "Erro ao cadastrar novo usuário, favor entrar em contato com o suporte";
            Log.e("ERRO_LOGAR", "Falha ao cadastrar novo usuário com erro: " + e.getMessage());
        }

        Toast.makeText(getContext(), ex, Toast.LENGTH_LONG).show();
    }

    public Context getContext() {
        return this;
    }

    public void salvarUsuario(Task<AuthResult> task) {
        String idUsuario = encode(usuario.getEmail());
        usuario.setId(idUsuario);
        usuario.salvar();
    }
}
