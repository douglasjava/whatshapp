package br.com.firebase.whatsapp.activity;

import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

/**
 * Created by Dias on 03/02/2018.
 */

public class Utils {


    protected static void mascaraEditText(EditText editText, String simpleMaskFormatter) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(simpleMaskFormatter);
        MaskTextWatcher mtw = new MaskTextWatcher(editText, smf);
        editText.addTextChangedListener(mtw);
    }
}
