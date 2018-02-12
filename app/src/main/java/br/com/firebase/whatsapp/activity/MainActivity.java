package br.com.firebase.whatsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.config.ConfiguracaoFirebase;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getAutenticacao;
import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getFirebase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logout;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = (Button) findViewById(R.id.btnSair);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSair :
                auth = getAutenticacao();
                auth.signOut();
                redirectWindowLogin();
        }
    }

    private void redirectWindowLogin() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    private Context getContext(){
        return this;
    }
}
