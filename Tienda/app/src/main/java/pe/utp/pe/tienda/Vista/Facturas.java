package principal.android.utp.proyectoandroid.Vista;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import principal.android.utp.proyectoandroid.R;
import principal.android.utp.proyectoandroid.controlador.MySingleton;
import static android.support.design.widget.Snackbar.make;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Facturas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Facturas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Facturas extends Fragment {

    ArrayList nombres =new ArrayList();
    ArrayList idfactura =new ArrayList();
    ArrayList productos =new ArrayList();
    private  String  urlControlador="http://192.168.55.206/anW/CONTROLADOR/";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Facturas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Facturas.
     */
    // TODO: Rename and change types and number of parameters
    public static Facturas newInstance(String param1, String param2) {
        Facturas fragment = new Facturas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_facturas, container, false);
        String URL = urlControlador+"ProductoControlador.php?op=3";
        final ListView listaFacturas=v.findViewById(R.id.listaF);
        StringRequest listaFacR = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                 //      System.out.println(response);
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        nombres.add(" FT-"+jsonArray.getJSONObject(i).getString("idfact")+" "+jsonArray.getJSONObject(i).getString("nombres")+"  | "+jsonArray.getJSONObject(i).getString("total"));
                        idfactura.add(jsonArray.getJSONObject(i).getString("idfact"));
                    }
                    listaFacturas.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombres));
                    listaFacturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String idFacturaa=String.valueOf(idfactura.get(position));
                            obtenerDatosDefacutraPorId(idFacturaa);
                            //make(view, idFacturaa, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                    });
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(listaFacR);
        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void obtenerDatosDefacutraPorId(String id){
        productos.clear();
        StringRequest obtenerPorID;
        String URLl = urlControlador+"ProductoControlador.php?op=4"+"&idfact="+id;
        obtenerPorID=new StringRequest(URLl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println(response);
                try{
                    JSONArray jsR = new JSONArray(response);
                    for(int i=0; i<jsR.length(); i++) {
                        productos.add(jsR.getJSONObject(i).getString("unidad")+" "+jsR.getJSONObject(i).getString("cantidad")+" | "+jsR.getJSONObject(i).getString("nombreP")+"  |  "+jsR.getJSONObject(i).getString("precio")+"      ");
                    }
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.lista_factura, null);
                    alertDialog.setView(convertView);
                    alertDialog.setTitle("Tetalle de factura");
                   alertDialog.setPositiveButton("Listo", null);
                    ListView lv = (ListView) convertView.findViewById(R.id.listaFacturaDet);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,productos);
                    lv.setAdapter(adapter);
                    alertDialog.show();
                 //   make(getView(), Pr, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(obtenerPorID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
