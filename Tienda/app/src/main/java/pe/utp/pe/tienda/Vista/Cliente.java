package principal.android.utp.proyectoandroid.Vista;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import principal.android.utp.proyectoandroid.R;
import principal.android.utp.proyectoandroid.controlador.MySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cliente extends Fragment {
    ArrayList nombres =new ArrayList();
    private  String  urlControlador="http://192.168.55.206/anW/CONTROLADOR/";

    public Cliente() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String URL = urlControlador+"ProductoControlador.php?op=2";
        View v =inflater.inflate(R.layout.fragment_cliente, container, false);
        final ListView listaCliente =v.findViewById(R.id.listaClientes);
        StringRequest clienteLista= new StringRequest(Request.Method.POST,URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    //System.out.println(response);
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        nombres.add(jsonArray.getJSONObject(i).getString("doc")+" "+jsonArray.getJSONObject(i).getString("numDoc")+" "+jsonArray.getJSONObject(i).getString("nombres"));
                    }
                    listaCliente.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombres));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } );
        MySingleton.getInstance(getContext()).addToRequestQueue(clienteLista);
        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}
