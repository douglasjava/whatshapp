package br.com.firebase.whatsapp.services;

import android.telephony.SmsManager;
import android.util.Log;

import br.com.firebase.whatsapp.beans.BeanEnvioSms;

/**
 * Created by Dias on 07/02/2018.
 */

public class EnviaSmsService {

    private BeanEnvioSms beanEnvioSms;

    public EnviaSmsService(BeanEnvioSms beanEnvioSms) {
        this.beanEnvioSms = beanEnvioSms;
    }

    public Boolean enviaSms() {

        boolean retorno = false;

        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(beanEnvioSms.getTelefone(), null, beanEnvioSms.getMensagem(), null, null);

            Thread.sleep(10000);
            retorno = Boolean.TRUE;

        } catch (Exception e) {
            Log.e("SMS", "Falha ao enviar sms com erro: " + e.getMessage());
            return retorno;
        }

        return retorno;
    }


}
