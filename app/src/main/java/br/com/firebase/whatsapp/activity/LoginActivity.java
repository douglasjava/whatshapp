package br.com.firebase.whatsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.firebase.whatsapp.R;

import static br.com.firebase.whatsapp.activity.Utils.mascaraEditText;

public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText telefone;
    private EditText prefixoTelefone;
    private Button cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nome = (EditText) findViewById(R.id.edtNome);

        telefone = (EditText) findViewById(R.id.edtTelefone);
        mascaraEditText(telefone, "(NN)NNNNN-NNNN");

        prefixoTelefone = (EditText) findViewById(R.id.edtCodPais);
        mascaraEditText(prefixoTelefone, "+NN");

        cadastrar = (Button) findViewById(R.id.btnCadastrar);

    }

    public void onClick(View view) {

    }
}
