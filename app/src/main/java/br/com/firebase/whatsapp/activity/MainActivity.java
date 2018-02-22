package br.com.firebase.whatsapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.adapter.TableAdapter;
import br.com.firebase.whatsapp.beans.Contato;
import br.com.firebase.whatsapp.beans.Usuario;
import br.com.firebase.whatsapp.exceptions.CampoVazioException;
import br.com.firebase.whatsapp.utils.Preferencias;
import br.com.firebase.whatsapp.utils.SlidingTabLayout;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getAutenticacao;
import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getFirebase;
import static br.com.firebase.whatsapp.utils.Childs.CHILD_CONTATO;
import static br.com.firebase.whatsapp.utils.Childs.CHILD_USUARIOS;
import static br.com.firebase.whatsapp.utils.Convert64Base.encode;
import static br.com.firebase.whatsapp.utils.Utils.validarCampoPreenchido;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private FirebaseAuth auth;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private DatabaseReference database;
    private String identificadorContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Whatsapp");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slt_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));


        TableAdapter tableAdapter = new TableAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tableAdapter);

        slidingTabLayout.setViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pesquisar:
                Toast.makeText(getContext(), "Menu Pesquisar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_adicionar:
                adicionar();
                return true;
            case R.id.menu_setting:
                Toast.makeText(getContext(), "Menu Configurar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void pesquisar() {

    }

    private void adicionar() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle("")
                .setCancelable(false);

        View viewPopup = getLayoutInflater().inflate(R.layout.fragment_adicionar_contato, null);
        dialog.setView(viewPopup);
        final EditText edtEmailCadastrar = viewPopup.findViewById(R.id.edtCadastroEmail);

        dialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    String emailContato = edtEmailCadastrar.getText().toString();
                    validarCampoPreenchido(emailContato);
                    validarEmailCadastrado(emailContato);
                } catch (CampoVazioException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    adicionar();
                }
            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create();
        dialog.show();
    }

    private void validarEmailCadastrado(String emailContato) {

        identificadorContato = encode(emailContato);
        database = getFirebase().child(CHILD_USUARIOS).child(identificadorContato);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    gravarContato(dataSnapshot);
                    Toast.makeText(getContext(), "Contato Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Usuário não cadastrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void gravarContato(DataSnapshot dataSnapshot) {

        Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

        Preferencias preferencias = new Preferencias(getContext());
        String identificadorUsuario = preferencias.getUsuarioLogado();

        Contato contato = new Contato(
                identificadorUsuario,
                usuarioContato.getNome(),
                usuarioContato.getEmail());

        database = getFirebase();
        database = database.child(CHILD_CONTATO)
                .child(identificadorUsuario)
                .child(identificadorContato);
        database.setValue(contato);
    }

    private void setting() {

    }

    private void logout() {
        auth = getAutenticacao();
        String usuario = auth.getCurrentUser().getEmail();
        Toast.makeText(getContext(), "Logout do usuário: " + usuario, Toast.LENGTH_SHORT).show();
        auth.signOut();
        redirectWindowLogin();
        finish();
    }

    private void redirectWindowLogin() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    private Context getContext() {
        return this;
    }

    private void dialogNovoContato() {


    }

}
