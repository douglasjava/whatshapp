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
import br.com.firebase.whatsapp.adapter.ConversaAdapter;
import br.com.firebase.whatsapp.beans.Contato;
import br.com.firebase.whatsapp.beans.Conversa;
import br.com.firebase.whatsapp.utils.Convert64Base;
import br.com.firebase.whatsapp.utils.Preferencias;

import static br.com.firebase.whatsapp.config.ConfiguracaoFirebase.getFirebase;
import static br.com.firebase.whatsapp.utils.Childs.CHILD_CONTATO;
import static br.com.firebase.whatsapp.utils.Childs.CHILD_CONVERSA;
import static br.com.firebase.whatsapp.utils.Convert64Base.decode;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference database;
    private ValueEventListener valueEventListenerConversas;


    public ConversasFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        database.addValueEventListener(valueEventListenerConversas);
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        database.removeEventListener(valueEventListenerConversas);
        Log.i("ValueEventListener", "onStop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        conversas = new ArrayList<Conversa>();
        listView = (ListView) view.findViewById(R.id.lv_conversa);
        adapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter(adapter);

        recuperarConversas();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversa conversa = conversas.get(position);
                Intent intent = new Intent(getContext(), ConversaActivity.class);
                intent.putExtra("nome", conversa.getNome());
                intent.putExtra("email", decode(conversa.getIdUsuario()));

                startActivity(intent);
            }
        });

        return view;
    }


    private void recuperarConversas() {
        String idUsuario = new Preferencias(getActivity()).getUsuarioLogado();
        database = getFirebase().child(CHILD_CONVERSA).child(idUsuario);
        valueEventListenerConversas = getValueEventListenerConversas();
    }

    private ValueEventListener getValueEventListenerConversas() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversas.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    conversas.add(data.getValue(Conversa.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

}
