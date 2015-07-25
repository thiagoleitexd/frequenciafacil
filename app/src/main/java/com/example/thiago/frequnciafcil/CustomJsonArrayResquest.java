package com.example.thiago.frequnciafcil;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



//Classe criada para possibilitar o envio do JSON por meio do POST
//sobrescrevendo alguns métodos

public class CustomJsonArrayResquest extends Request<JSONArray> {
    private Response.Listener<JSONArray> response;
    private Map<String, String> params;

    //POST
    public CustomJsonArrayResquest(int method, String url, Map<String, String> params, Response.Listener<JSONArray> response,
                                   Response.ErrorListener listener) {
        super(method, url, listener);
        this.params = params;
        this.response = response;
    }

    //GET
    public CustomJsonArrayResquest(String url, Map<String, String> params, Response.Listener<JSONArray> response,
                                   Response.ErrorListener listener) {
        super(Method.GET, url, listener);
        this.params = params;
        this.response = response;
    }

    //PARAMETROS
    @Override
    public Map<String, String> getParams() throws AuthFailureError{
        return params;
    }

    //CABEÇALHOS PERSONALIDADOS
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError{
        HashMap<String, String> header = new HashMap<String, String>();
        Log.i("APIKEY", "APIKEY");
        header.put("Authorization",MainActivityAluno.apikey);
        return (header);
    }

    @Override
    public Priority getPriority(){
        return (Priority.NORMAL);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response.success(new JSONArray(js), HttpHeaderParser.parseCacheHeaders(response)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        Log.i("Teste(X)", response.toString());
        this.response.onResponse(response);
    }
}

