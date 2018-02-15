package br.com.firebase.whatsapp.activity;

import android.content.Context;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.adapter.TableAdapter;
import br.com.firebase.whatsapp.utils.SlidingTabLayout;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getAutenticacao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private FirebaseAuth auth;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

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
                Toast.makeText(getContext(), "Menu Adicionar", Toast.LENGTH_SHORT).show();
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
}
