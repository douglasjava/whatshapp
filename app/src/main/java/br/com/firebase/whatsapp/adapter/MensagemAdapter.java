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
import br.com.firebase.whatsapp.beans.Mensagem;
import br.com.firebase.whatsapp.utils.Preferencias;

/**
 * Created by Marques on 17/02/2018.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(@NonNull Context context,  @NonNull ArrayList<Mensagem> mensagens) {
        super(context, 0, mensagens);
        this.context = context;
        this.mensagens = mensagens;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(mensagens != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            Mensagem mensagem = mensagens.get(position);

            if (isUsuarioAtual(mensagem.getIdUsuario())) {
                view = inflater.inflate(R.layout.lista_mensagem_remetente, parent, false);
            } else {
                view = inflater.inflate(R.layout.lista_mensagem_destinatario, parent, false);
            }

            TextView tvMensagem = view.findViewById(R.id.tv_mensagem);
            tvMensagem.setText(mensagem.getMensagem());

        }

        return view;
    }

    private Boolean isUsuarioAtual(String usuario){
        Boolean retorno = false;
        Preferencias preferencias = new Preferencias(context);
        if(usuario.equalsIgnoreCase(preferencias.getUsuarioLogado())){
            retorno = true;
        }
        return retorno;
    }
}
