package br.com.firebase.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.firebase.whatsapp.R;
import br.com.firebase.whatsapp.beans.Contato;

/**
 * Created by Marques on 15/02/2018.
 */

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private Context context;
    private ArrayList<Contato> contatos;

    public ContatoAdapter(@NonNull Context context, @NonNull ArrayList<Contato> contatos) {
        super(context, 0, contatos);
        this.context = context;
        this.contatos = contatos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (contatos != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_contato, parent, false);
            TextView nomeContato = view.findViewById(R.id.tv_nome);
            TextView emailContato = view.findViewById(R.id.tv_email);
            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());
        }

        return view;
    }
}
