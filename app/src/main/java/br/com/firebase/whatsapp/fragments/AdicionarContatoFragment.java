package br.com.firebase.whatsapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.firebase.whatsapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdicionarContatoFragment extends Fragment {


    public AdicionarContatoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar_contato, container, false);
        return view;
    }


}
