package br.com.makadu.makaduevento.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lucasschwalbeferreira on 21/03/16.
 */


public class ResponseJson implements Serializable {

    // JSON RETROFIT
    public String resposta;
    public String result;
    public String erro;
    public  String data;

}
