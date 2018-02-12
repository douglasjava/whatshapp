package br.com.firebase.whatsapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.text.DecimalFormat;
import java.util.Random;

import br.com.firebase.whatsapp.exceptions.CampoVazioException;
import br.com.firebase.whatsapp.exceptions.ConexaoException;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static br.com.firebase.whatsapp.utils.ParametrosPermission.MY_PERMISSIONS_REQUEST_SEND_SMS;

/**
 * Created by Dias on 03/02/2018.
 */

public class Utils {

    public static void mascaraEditText(EditText editText, String simpleMaskFormatter) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(simpleMaskFormatter);
        MaskTextWatcher mtw = new MaskTextWatcher(editText, smf);
        editText.addTextChangedListener(mtw);
    }

    public static String textView(EditText editText) {
        return editText.getText().toString();
    }

    public static void validaPermissoes(Context context, Activity activity) {
        if (!isPermissoes(context, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS)) {
            requestIsPermission(activity, MY_PERMISSIONS_REQUEST_SEND_SMS, new String[]{Manifest.permission.SEND_SMS});
        }
    }

    public static void validarCampoPreenchido(String... campos) throws CampoVazioException {
        for (String campo : campos) {
            if (campo.length() == 0 || campo == null) {
                throw new CampoVazioException("Favor preencher todos os campos! ");
            }
        }
    }

    public static String replace(String texto, String... tags) {
        for (String t : tags) {
            texto = texto.replace(t, "");
        }
        return texto;
    }

    public static String geraToken() {
        Random random = new Random();
        int numeroRamdomico = random.nextInt(9999);
        String token = String.valueOf(new DecimalFormat("0000").format(numeroRamdomico));
        return token;
    }

    /***
     * {@link PackageManager}
     * PackageManager.PERMISSION_GRANTED =  0 -> Tem permissão.
     * PackageManager.PERMISSION_DENIED  = -1 -> Não tem permissão.
     * @param context
     * @param permissoes
     * @return
     */
    public static boolean isPermissoes(Context context, String... permissoes) {
        for (String permissao : permissoes) {
            if (ContextCompat.checkSelfPermission(context, permissao) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestIsPermission(Activity activity, int requestCode, String... permissoes) {
        ActivityCompat.requestPermissions(activity, permissoes, requestCode);
    }

    public static Boolean isConexao(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        Boolean conectado = Boolean.FALSE;
        if (connectivity.getActiveNetworkInfo() != null
                && connectivity.getActiveNetworkInfo().isAvailable()
                && connectivity.getActiveNetworkInfo().isConnected()) {
            conectado = Boolean.TRUE;
        }
        return conectado;
    }

    public static void verificarConexao(Context context) throws ConexaoException {
        if (!isConexao(context)) {
            throw new ConexaoException("Sem acesso a internet, favor conectar!");
        }
    }
}
