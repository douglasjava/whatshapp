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
public class ContatosFragment extends Fragment {


    public ContatosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        return view;
    }

}
