package pe.utp.pe.tienda.Data;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.android.volley.toolbox.Volley.*;


public class Datos  {
    private String URLC="http://54.244.200.32/";

    URL url=null;
    String linea="";
    int respuesta=0;
    String res="";

    public String nombre;
    public int rol;

    public String validarUsuarioGET(String usuario, String contrasenia){
        String file="reshL.php?";
        try {
            url=new URL(URLC+file+"usuario="+usuario+"&contrasenia="+contrasenia);
            HttpURLConnection conection = (HttpURLConnection) url.openConnection();
            conection.setRequestMethod("GET");
            respuesta = conection.getResponseCode();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(conection.getInputStream());
                res=covetirAString(in);
                System.out.println(conection.getResponseMessage());
                conection.disconnect();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return res;

    }

    public String obtenerUsuario(String idusuario){
        String file="datosU.php?";
        try {
            url=new URL(URLC+file+"tkn=qwerty123&idu="+idusuario);
            //url=new URL("http://54.244.200.32/resultadoC.php?usuario=pvasquez&contrasenia=123");
            HttpURLConnection conection = (HttpURLConnection) url.openConnection();
            conection.setRequestMethod("GET");
            respuesta = conection.getResponseCode();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(conection.getInputStream());
                res=covetirAStringJson(in);
                conection.disconnect();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return res;
    }

    private String covetirAString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    private String covetirAStringJson(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
   public int obtDatosJSON(String response) {
        int res = 0;
        try {
            JSONObject obJson=new JSONObject(response);
            if (obJson.length() > 0) {
                res=obJson.getInt("idalumno");
            }
        } catch (Exception e) {
        }
        return res;
    }

}
