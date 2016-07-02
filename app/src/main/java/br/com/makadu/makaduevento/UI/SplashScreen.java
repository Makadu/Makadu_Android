package br.com.makadu.makaduevento.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.SessionManager;

/**
 * Created by lucasschwalbeferreira on 10/6/15.
 */
public class SplashScreen extends ActionBarActivity {

    //ImageView img_logo_makadu;

    Class<?> activityClass;
    Class[] paramTypes = { Integer.TYPE, Integer.TYPE };

    private SessionManager session;

    Method overrideAnimation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_screen);

        //img_logo_makadu = (ImageView)findViewById(R.id.img_splash_logo);

        try {
            activityClass = Class.forName("android.app.Activity");
            overrideAnimation = activityClass.getDeclaredMethod(
                    "overridePendingTransition", paramTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.your_fade_in_anim);
                //img_logo_makadu.startAnimation(fadeoutAnim);
                /*SessionManager session = new SessionManager(getApplicationContext());
                session.setLogin(true);
                if(!session.isLoggedIn()) {
                    Intent i = new Intent(SplashScreen.this,
                            Login.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashScreen.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }*/

                session = new SessionManager(getApplicationContext());

                if (session.isLoggedIn()) {
                    startActivity(new Intent(SplashScreen.this,Tab_Main.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, CreateAccountActivity.class));
                    finish();
                }

                if (overrideAnimation != null) {
                    try {
                        overrideAnimation.invoke(SplashScreen.this, android.R.anim.fade_in,
                                android.R.anim.fade_out);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 3000);
    }
}
