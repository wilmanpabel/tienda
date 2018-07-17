package principal.android.utp.proyectoandroid.Vista;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.ArrayList;

import principal.android.utp.proyectoandroid.R;
import principal.android.utp.proyectoandroid.controlador.MySingleton;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.support.design.widget.Snackbar.make;

public class Productos extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private  String  urlControlador="http://192.168.55.206/anW/CONTROLADOR/";
    private ArrayList codigo  = new ArrayList();
    private ArrayList nombre = new ArrayList();

    private ArrayList codigoProducto  = new ArrayList();
    private ArrayList nombreProducto = new ArrayList();
    private ArrayList precio = new ArrayList();
    private ArrayList und = new ArrayList();
    private ArrayList stok = new ArrayList();

    Spinner spiner;
    ListView lista;

    private String mParam1;
    private String mParam2;

    LayoutInflater layoutInflater;

    private OnFragmentInteractionListener mListener;


    public static Productos newInstance(String param1, String param2) {
        Productos fragment = new Productos();
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
        View v=inflater.inflate(R.layout.fragment_productos, container, false);
        spiner=(Spinner) v.findViewById(R.id.categoria);
        lista=(ListView) v.findViewById(R.id.lista);
        ObtenerDatosSpinner();
        return  v;
    }

    public void ObtenerDatosSpinner()
    {
        codigo.clear();
        nombre.clear();
        String URL = urlControlador+"CategoriaControlador.php?op=1";
        StringRequest listaR = new StringRequest( URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //System.out.println(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        codigo.add(jsonArray.getJSONObject(i).getString("Id"));
                        nombre.add(jsonArray.getJSONObject(i).getString("Nombre"));

                    }
                    spiner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, nombre));
                    spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String codigoCat = String.valueOf(codigo.get(i));
                            CargarListView(Integer.parseInt(codigoCat));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            CargarListView(1);
                        }
                    });

                }catch (Exception ex)
                {
                    Toast.makeText(getContext(), "ERROR: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR DE CODIGO", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(listaR);
    }

    public void CargarListView(final int CodigoCategoria)
    {
        codigoProducto.clear();
        nombreProducto.clear();
        precio.clear();
        stok.clear();
        und.clear();

        String URL = urlControlador+"ProductoControlador.php?op=1&codigo="+CodigoCategoria;
        StringRequest listaR = new StringRequest( URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    //System.out.println(response);
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        codigoProducto.add(jsonArray.getJSONObject(i).getString("Id"));
                        nombreProducto.add(jsonArray.getJSONObject(i).getString("Nombre"));
                        precio.add(jsonArray.getJSONObject(i).getString("precio"));
                        stok.add(jsonArray.getJSONObject(i).getString("stok"));
                        und.add(jsonArray.getJSONObject(i).getString("und"));
                    }
                    lista.setAdapter(new miAdaptador(getActivity().getApplicationContext()));
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String codigoPro = String.valueOf(codigoProducto.get(position));
                            //Toast.makeText(getContext(),codigoPro,Toast.LENGTH_LONG).show();
                            String stocka = String.valueOf(stok.get(position));
                            modificarDatosDEproducto(codigoPro,stocka,CodigoCategoria);
                        }
                    });
                }catch (Exception ex)
                {
                    Toast.makeText(getContext(), "ERROR: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR DE CODIGO", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(listaR);
    }
    public void modificarDatosDEproducto(final String id, String Stock, final int cat){
        ///?op=5&stock=30&idproducto=2/
        AlertDialog.Builder dialogo= new AlertDialog.Builder(getContext());
        final EditText NuevoN= new EditText(getContext());
        NuevoN.setText(Stock);
        dialogo.setView(NuevoN);
        dialogo.setTitle("Editar Stock");
        dialogo.setNegativeButton("Cancelar",null);
        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getActivity().getApplicationContext(),NuevoN.getText().toString(),Toast.LENGTH_LONG).show();
                String URLl = urlControlador+"ProductoControlador.php?op=5"+"&stock="+NuevoN.getText().toString()+"&idproducto="+id;
                StringRequest cambiarStock =new StringRequest(URLl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        //Toast.makeText(getActivity().getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                        make(getView(), response.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        CargarListView(cat);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                MySingleton.getInstance(getContext()).addToRequestQueue(cambiarStock);
                ///Enviar al  controlador para editar el producto..///:
            }
        });
        dialogo.show();
    }

    private class miAdaptador extends android.widget.BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        TextView txtNombre,txtCodigo,lblPrecio,lblStok,lblUnd;

        public miAdaptador(Context context) {
            this.context = context;
            layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return nombreProducto.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {

            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.cuadrilla,null);

            txtCodigo = (TextView) viewGroup.findViewById(R.id.lblCodigo);
            txtNombre = (TextView)viewGroup.findViewById(R.id.lblNombre);
            lblPrecio = (TextView)viewGroup.findViewById(R.id.lprecio);
            lblStok = (TextView)viewGroup.findViewById(R.id.lstok);
            lblUnd = (TextView)viewGroup.findViewById(R.id.lund);

            txtCodigo.setText(codigoProducto.get(i).toString());
            txtNombre.setText(nombreProducto.get(i).toString());
            lblPrecio.setText(precio.get(i).toString());
            lblStok.setText(stok.get(i).toString());
            lblUnd.setText(und.get(i).toString());

            return viewGroup;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
