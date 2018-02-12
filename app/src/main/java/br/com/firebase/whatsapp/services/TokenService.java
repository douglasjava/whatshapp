package br.com.firebase.whatsapp.services;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import br.com.firebase.whatsapp.utils.HttpUteis;
import br.com.firebase.whatsapp.beans.BeanEnvioSms;
import br.com.firebase.whatsapp.exceptions.GeracaoTokenException;

/**
 * Created by Dias on 04/02/2018.
 */

public class TokenService extends AsyncTask<String, Void, BeanEnvioSms> {

    private static final String url = "http://192.168.0.6:8080/token";
    private Context mContext;
    private TokenCallback callback;
    private GeracaoTokenException ex;
    private Dialog load;
    private EnviaSmsService enviaSmsService;
    private BeanEnvioSms beanEnvioSms;

    public TokenService(Context context, TokenCallback callback) {
        this.mContext = context;
        this.callback = callback;
    }

    /**
     * É executado sempre antes da Thread ser iniciada, na principal
     **/
    @Override
    protected void onPreExecute() {
        load = ProgressDialog.show(mContext, "Por favor Aguarde ...", "Recuperando token ...");
        super.onPreExecute();
    }


    /**
     * é o responsável pelo processamento pesado, pois ele é executado em uma Thread separada
     **/
    @Override
    protected BeanEnvioSms doInBackground(String... telefoneUsuario) {

        HttpUteis http = new HttpUteis();
        BeanEnvioSms retorno = null;

        try {

            String token = http.doGet(url);

            beanEnvioSms = new BeanEnvioSms(telefoneUsuario[0], getMensagemConfirmacao(token), token);
            enviaSmsService = new EnviaSmsService(beanEnvioSms);

            Boolean envioSms = enviaSmsService.enviaSms();
            beanEnvioSms.setEnviado(envioSms);

            retorno = beanEnvioSms;

        } catch (IOException e) {
            this.ex = new GeracaoTokenException("Erro ao conectar no serviço de geração de token " + e.getMessage());

        } catch (Exception e) {
            this.ex = new GeracaoTokenException("Erro ao recupera token " + e.getMessage());
        }

        callback.onSuccess(beanEnvioSms);
        return beanEnvioSms;
    }

    /**
     * é o que recebe o retorno do doInBackground e é chamado utilizando um Handler
     **/
    @Override
    protected void onPostExecute(BeanEnvioSms beanEnvioSms) {
        if(beanEnvioSms.getToken() == null){
            Toast.makeText(mContext, "Erro ao recuperar token", Toast.LENGTH_SHORT).show();
        }
        if(!beanEnvioSms.getEnviado()){
            Toast.makeText(mContext, "Falha ao enviar SMS", Toast.LENGTH_SHORT).show();
        }
        if (this.ex != null) {
            Toast.makeText(mContext, this.ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        load.dismiss();
        super.onPostExecute(beanEnvioSms);
    }

    /**
     * é o responsável por receber as informações para mostrar a porcentagem do download na tela para o usuário
     **/
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private String getMensagemConfirmacao(String token) {
        return token + " - Código WhatsApp de confirmação ";
    }

    public interface TokenCallback {
        void onSuccess(BeanEnvioSms objEnvioSms);
    }
}
