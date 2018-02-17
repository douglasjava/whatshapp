package br.com.firebase.whatsapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.activity.ConversaActivity;
import br.com.firebase.whatsapp.adapter.ContatoAdapter;
import br.com.firebase.whatsapp.beans.Contato;
import br.com.firebase.whatsapp.utils.Preferencias;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getFirebase;
import static br.com.firebase.whatsapp.utils.Childs.CHILD_CONTATO;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference database;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        database.addValueEventListener(valueEventListenerContatos);
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        database.removeEventListener(valueEventListenerContatos);
        Log.i("ValueEventListener", "onStop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        contatos = new ArrayList<Contato>();

        listView = (ListView) view.findViewById(R.id.lv_contatos);
        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);

        recuperarContatos();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contato contato = contatos.get(position);

                Intent intent = new Intent(getContext(), ConversaActivity.class);
                intent.putExtra("nome", contato.getNome());
                intent.putExtra("email", contato.getEmail());

                startActivity(intent);
            }
        });

        return view;
    }

    private void recuperarContatos() {

        String idUsuario = new Preferencias(getActivity()).getUsuarioLogado();

        database = getFirebase().child(CHILD_CONTATO).child(idUsuario);
        valueEventListenerContatos = getValueEventListenerContatos();

    }

    private ValueEventListener getValueEventListenerContatos() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contatos.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    contatos.add(data.getValue(Contato.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }


}
