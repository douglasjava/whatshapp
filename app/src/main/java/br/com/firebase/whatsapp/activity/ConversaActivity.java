package br.com.firebase.whatsapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.adapter.MensagemAdapter;
import br.com.firebase.whatsapp.beans.Conversa;
import br.com.firebase.whatsapp.beans.Mensagem;
import br.com.firebase.whatsapp.exceptions.CampoVazioException;
import br.com.firebase.whatsapp.exceptions.ErroMensagemException;
import br.com.firebase.whatsapp.utils.Childs;
import br.com.firebase.whatsapp.utils.Preferencias;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getFirebase;
import static br.com.firebase.whatsapp.utils.Childs.CHILD_MENSAGEM;
import static br.com.firebase.whatsapp.utils.Convert64Base.encode;
import static br.com.firebase.whatsapp.utils.Utils.getTextView;
import static br.com.firebase.whatsapp.utils.Utils.validarCampoPreenchido;

public class ConversaActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private String nomeUsuarioDestinatario;
    private String emailUsuarioDestinatario;
    private EditText mensagem;
    private ImageButton enviarMensagem;
    private String idUsuarioRemetente;
    private DatabaseReference database;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    @Override
    public void onStart() {
        super.onStart();
        database.addValueEventListener(valueEventListenerMensagem);
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        database.removeEventListener(valueEventListenerMensagem);
        Log.i("ValueEventListener", "onStop");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        mensagem = findViewById(R.id.edtMensagem);
        enviarMensagem = findViewById(R.id.btEnviar);
        listView = findViewById(R.id.lv_conversas);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nomeUsuarioDestinatario = extras.getString("nome");
            emailUsuarioDestinatario = extras.getString("email");
        }

        toolbar = findViewById(R.id.tb_conversa);
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        setSupportActionBar(toolbar);

        enviarMensagem.setOnClickListener(this);

        mensagens = new ArrayList<Mensagem>();
        adapter = new MensagemAdapter(getContext(), mensagens);
        listView.setAdapter(adapter);

        database = recuperarChilds();

        valueEventListenerMensagem = getValueEventListenerMensagem();

        database.addValueEventListener(valueEventListenerMensagem);

    }

    private DatabaseReference recuperarChilds() {
        return database = getFirebase().child(Childs.CHILD_MENSAGEM)
                .child(getIdUsuarioRemetente())
                .child(getIdUsuarioDestinatario());
    }


    private ValueEventListener getValueEventListenerMensagem() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Mensagem mensagem = data.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btEnviar:
                try {
                    enviarMensagem();
                    Conversa conversaD = new Conversa(getIdUsuarioDestinatario(), nomeUsuarioDestinatario, getTextView(mensagem));
                    Conversa conversaR = new Conversa(getIdUsuarioDestinatario(), getNomeUsuario(), getTextView(mensagem));
                    salvarConversa(conversaD, conversaR);
                } catch (CampoVazioException e) {
                    Toast.makeText(getContext(), "Favor digitar uma mensagem", Toast.LENGTH_SHORT).show();
                } catch (ErroMensagemException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void salvarConversa(Conversa conversaD, Conversa conversaR) throws ErroMensagemException {
        try{
            database = getFirebase().child(Childs.CHILD_CONVERSA)
                    .child(getIdUsuarioRemetente())
                    .child(getIdUsuarioDestinatario());
            database.setValue(conversaR);

            database = getFirebase().child(Childs.CHILD_CONVERSA)
                    .child(getIdUsuarioDestinatario())
                    .child(getIdUsuarioRemetente());
            database.setValue(conversaD);

            limparTexto();
        }catch (Exception e){
            throw new ErroMensagemException("Erro ao gravar mensagem!");
        }
    }

    private void enviarMensagem() throws CampoVazioException, ErroMensagemException {
        String textoMensagem = getTextView(mensagem);
        validarCampoPreenchido(textoMensagem);
        Mensagem mensagem = new Mensagem().setIdUsuario(getIdUsuarioRemetente()).setMensagem(textoMensagem);
        salvarMensagem(mensagem);
    }

    private void salvarMensagem(Mensagem mensagem) throws ErroMensagemException {
        try {

            database = getFirebase().child(CHILD_MENSAGEM)
                    .child(getIdUsuarioRemetente())
                    .child(getIdUsuarioDestinatario()).push();
            database.setValue(mensagem);

            database = getFirebase().child(CHILD_MENSAGEM)
                    .child(getIdUsuarioDestinatario())
                    .child(getIdUsuarioRemetente()).push();
            database.setValue(mensagem);

        } catch (Exception e) {
            Log.e("ENVIAR_MENSAGEM", e.getMessage());
            throw new ErroMensagemException("Erro ao enviar mensagem");
        }
    }


    private Context getContext() {
        return this;
    }

    private String getIdUsuarioRemetente() {
        Preferencias preferencias = new Preferencias(getContext());
        return preferencias.getUsuarioLogado();
    }

    private String getIdUsuarioDestinatario() {
        return encode(emailUsuarioDestinatario);
    }

    private String getNomeUsuario(){
        Preferencias preferencias = new Preferencias(getContext());
        return preferencias.getNomeUsuario();
    }

    private void limparTexto() {
        mensagem.setText("");
    }
}
