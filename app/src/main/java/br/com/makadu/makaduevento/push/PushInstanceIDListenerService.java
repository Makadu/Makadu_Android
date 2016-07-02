package br.com.makadu.makaduevento.push;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class PushInstanceIDListenerService extends InstanceIDListenerService {
    public PushInstanceIDListenerService() {
    }

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
