package pe.utp.pe.tienda.controlador;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

import pe.utp.pe.tienda.Vista.Confirguracion;
import pe.utp.pe.tienda.Vista.Principal;

public class Usuario  {
    private String ulrControlador= Confirguracion.urlControlador+"UsuarioControlador.php";
    private String url2= Confirguracion.urlControladorDOs;
    private  String resp;
    public void ingresarASY(String usuario, String pass, final Context cnt){
        String URL = url2+"ingresar/"+usuario+"/"+pass;
        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String rspta = String.valueOf(response).trim();
                    System.out.print(response);
                    if(rspta.equals("0"))
                    {
                        Toast.makeText(cnt, "VERIFIQUE SUS CREDENCIALES", Toast.LENGTH_SHORT).show();
                    }else
                    {
                            Intent intent = new Intent(cnt, Principal.class);
                            intent.putExtra("idusuario", rspta);
                            cnt.startActivity(intent);
                    }
                }catch (Exception ex)
                {
                    Toast.makeText(cnt, "Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error);
                Toast.makeText(cnt, "Error en el codigo "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String,String> getHeaders(){
                Map<String,String> header=new HashMap<>();
                header.put("User-Agent","resh1!");
                return  header;
            }
        };
        MySingleton.getInstance(cnt).addToRequestQueue(stringRequest);
    }

    public void obtenerDatos(final String idusuario, final Context cnt){
        String URL = ulrControlador+"?op=2&idusuario="+idusuario;
        StringRequest strR=new StringRequest( URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String,String> getHeaders(){
                Map<String,String> header=new HashMap<>();
                header.put("User-Agent","resh1!");
                return  header;
            }
        };
        MySingleton.getInstance(cnt).addToRequestQueue(strR);
    }


}

