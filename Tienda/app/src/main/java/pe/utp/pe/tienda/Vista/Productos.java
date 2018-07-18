package pe.utp.pe.tienda.Vista;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import pe.utp.pe.tienda.LoginActivity;
import pe.utp.pe.tienda.R;
import pe.utp.pe.tienda.controlador.MySingleton;

import com.github.snowdream.android.widget.SmartImageView;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.support.design.widget.Snackbar.make;

public class Productos extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private  String  urlControlador=Confirguracion.urlControlador;
    private  String  url2=Confirguracion.urlControladorDOs;
    private ArrayList codigo  = new ArrayList();
    private ArrayList nombre = new ArrayList();

    private ArrayList codigoProducto  = new ArrayList();
    private ArrayList nombreProducto = new ArrayList();
    private ArrayList precio = new ArrayList();
    private ArrayList und = new ArrayList();
    private ArrayList stok = new ArrayList();
    private ArrayList imagen = new ArrayList();

    Spinner spiner;
    ListView lista;

    private String idusuario;
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
            idusuario = getArguments().getString("usu");

        }
        Toast.makeText(getContext(),idusuario,Toast.LENGTH_LONG).show();

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
        String URL2 = url2+"empresas";
        StringRequest listaR = new StringRequest( URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        codigo.add(jsonArray.getJSONObject(i).getString("id"));
                        nombre.add(jsonArray.getJSONObject(i).getString("nombre"));

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
        imagen.clear();

        String URL = urlControlador+"ProductoControlador.php?op=1&codigo="+1;

        String URL2 = url2+"pasajeEmpresa/"+CodigoCategoria;

        StringRequest listaR = new StringRequest( URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    System.out.println(response);
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        codigoProducto.add(jsonArray.getJSONObject(i).getString("id"));
                        nombreProducto.add("Origen: "+jsonArray.getJSONObject(i).getString("origen_id")+"\n Destino: "+jsonArray.getJSONObject(i).getString("destinos_id")+"\n "+jsonArray.getJSONObject(i).getString("fecha"));
                        precio.add(jsonArray.getJSONObject(i).getString("precio"));
                        stok.add(jsonArray.getJSONObject(i).getString("cantidad"));
                        und.add(jsonArray.getJSONObject(i).getString("moneda"));
                        imagen.add(jsonArray.getJSONObject(i).getString("moneda"));
                    }
                    lista.setAdapter(new miAdaptador(getActivity().getApplicationContext()));
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String codigoPro = String.valueOf(codigoProducto.get(position));
                            //Toast.makeText(getContext(),codigoPro,Toast.LENGTH_LONG).show();
                            String stocka = String.valueOf(stok.get(position));
                            comprarPasaje(codigoPro,CodigoCategoria);
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





    public void comprarPasaje(final String id,  final int cat){
        ///?op=5&stock=30&idproducto=2/
        AlertDialog.Builder dialogo= new AlertDialog.Builder(getContext());
        dialogo.setTitle("Comprar");
        dialogo.setNegativeButton("Cancelar",null);
        //Toast.makeText(getContext(),idusuario,Toast.LENGTH_LONG).show();
        if (idusuario.equals("0")){
            dialogo.setMessage("Ingrese a su cuenta por favor");
            dialogo.setNegativeButton("Ingresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            getContext().startActivity(intent);
                        }
                    }
            );
        }else{
            dialogo.setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getActivity().getApplicationContext(),NuevoN.getText().toString(),Toast.LENGTH_LONG).show();
                    String URLl = url2+"comprar/"+idusuario+"/"+id;
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
        }

        dialogo.show();
    }

    private class miAdaptador extends android.widget.BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        TextView txtNombre,txtCodigo,lblPrecio,lblStok,lblUnd;

        SmartImageView smartImage;

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
            smartImage = (SmartImageView)viewGroup.findViewById(R.id.imagenProducto);

            /// LLINK DE DONDE ESTA LA IMAGEN  DEL DESTINO /// con el campo imagen
            String urlfinal = "https://www.peru.travel/Portals/_default/Images/Events/aniversario_ciudad_arequipa.jpg";

            Rect rect = new Rect(smartImage.getLeft(),smartImage.getTop(),smartImage.getRight(),smartImage.getBottom());

            smartImage.setImageUrl(urlfinal,rect);
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
