package principal.android.utp.proyectoandroid.Vista;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import principal.android.utp.proyectoandroid.R;
import principal.android.utp.proyectoandroid.controlador.MySingleton;

import static android.support.design.widget.Snackbar.make;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Productos.OnFragmentInteractionListener,Cliente.OnFragmentInteractionListener,Facturas.OnFragmentInteractionListener {
    String idusuario;
    NavigationView navegacion ;
    String[] usuarioD;
    TextView nombre;
    TextView rol;
    private ListView listaMenu;
    private ArrayAdapter<String> adaptador;
    DrawerLayout drawer;
    String ulrControlador="http://192.168.55.206/anW/CONTROLADOR/UsuarioControlador.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent origen = getIntent();
        idusuario = origen.getStringExtra("idusuario").trim().toString();
        navegacion = (NavigationView) findViewById(R.id.nav_view);

        completarDatosLeleralUsuario();

        navegacion.setNavigationItemSelectedListener(this);

    }


    public  void  completarDatosLeleralUsuario(){
        View headerVista = navegacion.getHeaderView(0);
        nombre=(TextView) headerVista.findViewById(R.id.nombre);
        rol=(TextView)  headerVista.findViewById(R.id.cargo);
        String URL = ulrControlador+"op=2&idusuario="+idusuario;
        StringRequest rq=new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject (response);
                    String nombres=jsonObject.getString("nombres");
                    String roll=jsonObject.getString("rol");
                    nombre.setText(nombres);
                    rol.setText(roll);
                }catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(rq);

        String URL2 = ulrControlador+"op=3&idusuario="+idusuario;
        StringRequest reqMenu =new StringRequest(URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObjectMenu=new JSONObject (response);
                    Menu menu = navegacion.getMenu();
                    String produc=jsonObjectMenu.getString("productos");
                    String usuarios=jsonObjectMenu.getString("usuarios");
                    String configuracion=jsonObjectMenu.getString("configuracion");
                    if (produc.equals("si")){
                        menu.add(R.id.lista, 1, Menu.NONE, "Productos");
                        menu.getItem(0).setIcon(R.drawable.ic_menu_gallery);
                    }
                    if (usuarios.equals("si")){
                        menu.add(R.id.lista, 2, Menu.NONE, "Clientes");
                        menu.getItem(1).setIcon(R.drawable.ic_menu_slideshow);
                    }
                    if (configuracion.equals("si")){
                        menu.add(R.id.lista, 3, Menu.NONE, "Facturas");
                        menu.getItem(2).setIcon(R.drawable.ic_menu_manage);
                    }
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(reqMenu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.nav_view){

        }
        obtenerMenuNav(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void obtenerMenuNav(Integer id){
            if (id == 1) {
                Productos proVF=new Productos();
                android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor,proVF);
                transaction.commit();
            }
            if (id == 2) {
                Cliente cliVF=new Cliente ();
                android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor,cliVF);
                transaction.commit();
            }
            if (id == 3) {
                Facturas factuVF=new Facturas ();
                android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor,factuVF);
                transaction.commit();
            }
        }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
