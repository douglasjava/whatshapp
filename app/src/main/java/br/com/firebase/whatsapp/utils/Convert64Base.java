package br.com.firebase.whatsapp.utils;

import android.util.Base64;

/**
 * Created by Marques on 15/02/2018.
 */

public class Convert64Base {

    public static String encode(String codigo) {
        return Base64.encodeToString(codigo.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decode(String codigo) {
        return new String(Base64.decode(codigo, Base64.DEFAULT));
    }

}
