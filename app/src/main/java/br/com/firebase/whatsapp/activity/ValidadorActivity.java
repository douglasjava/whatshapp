package br.com.firebase.whatsapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import br.com.firebase.whatsapp.utils.Preferencias;
import br.com.firebase.whatsapp.R;

import static br.com.firebase.whatsapp.utils.Utils.mascaraEditText;

public class ValidadorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtCodValidor;
    private Button btnValidar;
    private Preferencias preferencias;
    private HashMap<String, String> dadosUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        edtCodValidor = (EditText) findViewById(R.id.edtCodigoValidacao);
        btnValidar = (Button) findViewById(R.id.btnValidar);

        mascaraEditText(edtCodValidor, "NNNN");

        /** Recuperando token passado**/
        Intent it = getIntent();
        edtCodValidor.setText(it.getStringExtra("token"));

        btnValidar.setOnClickListener(this);
        btnValidar.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnValidar:
                preferencias = new Preferencias(this);
                dadosUsuarios = preferencias.getDadosUsuarios();

                String tokenGerado = dadosUsuarios.get("token");
                if(tokenGerado.contentEquals(edtCodValidor.getText().toString())){
                    Toast.makeText(context(), "Token validado com sucesso", Toast.LENGTH_LONG).show();
                }

        }
    }

    public Context context() {
        return this;
    }
}
