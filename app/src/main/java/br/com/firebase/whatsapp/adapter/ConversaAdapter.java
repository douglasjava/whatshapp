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
import br.com.firebase.whatsapp.beans.Conversa;

/**
 * Created by Marques on 19/02/2018.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private final Context mContext;
    private final ArrayList<Conversa> conversas;

    public ConversaAdapter(@NonNull Context context, @NonNull ArrayList<Conversa> conversas) {
        super(context, 0, conversas);
        this.mContext = context;
        this.conversas = conversas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (!conversas.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_conversa, parent, false);

            TextView nomeContato = view.findViewById(R.id.tv_nome);
            TextView ultimaConversa = view.findViewById(R.id.tv_conversa);

            Conversa converra = conversas.get(position);
            nomeContato.setText(converra.getNome());
            ultimaConversa.setText(converra.getMensagem());
        }

        return view;
    }
}
