package pe.utp.pe.tienda.Vista;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import pe.utp.pe.tienda.LoginActivity;
import pe.utp.pe.tienda.R;
import pe.utp.pe.tienda.Registrarme;
import pe.utp.pe.tienda.controlador.MySingleton;

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

   public String ulrControlador= Confirguracion.urlControlador;
    public String url2= Confirguracion.urlControladorDOs;


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
        if(origen.hasExtra("idusuario")){
            idusuario = origen.getStringExtra("idusuario").trim().toString();
        }else{
            idusuario = "0";
        }
        navegacion = (NavigationView) findViewById(R.id.nav_view);

        completarDatosLeleralUsuario();

        navegacion.setNavigationItemSelectedListener(this);

        Bundle idusu= new Bundle();
        idusu.putString("usu",idusuario);
        Productos proVF=new Productos();
        proVF.setArguments(idusu);
        android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor,proVF);
        transaction.commit();
    }


    public  void  completarDatosLeleralUsuario(){
        View headerVista = navegacion.getHeaderView(0);
        nombre=(TextView) headerVista.findViewById(R.id.nombre);
        rol=(TextView)  headerVista.findViewById(R.id.cargo);
        if (idusuario.equals("0")) {
            nombre.setText("No has ingresado");
            rol.setText("");
            Menu menu = navegacion.getMenu();
            menu.add(R.id.lista, 1, Menu.NONE, "Ingresar");
            menu.getItem(0).setIcon(R.drawable.ic_menu_manage);
            menu.add(R.id.lista, 2, Menu.NONE, "Registrarme");
            menu.getItem(1).setIcon(R.drawable.ic_menu_share);
            menu.add(R.id.lista, 3, Menu.NONE, "Pasajes");
            menu.getItem(2).setIcon(R.drawable.ic_menu_gallery);
        }else{
            String URL = url2+"cliente/"+idusuario;
            StringRequest rq=new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        System.out.println(response);

                        nombre.setText(response.toString());
                        rol.setText("");
                    }catch (Exception e) {
                        Log.d("error 2",e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(rq);

            Menu menu = navegacion.getMenu();
                menu.add(R.id.lista, 1, Menu.NONE, "Pasajes");
                menu.getItem(0).setIcon(R.drawable.ic_menu_gallery);
                menu.add(R.id.lista, 2, Menu.NONE, "Mis Viajes");
                menu.getItem(1).setIcon(R.drawable.ic_menu_slideshow);
                menu.add(R.id.lista, 3, Menu.NONE, "Facturas");
                menu.getItem(2).setIcon(R.drawable.ic_menu_share);
                menu.add(R.id.lista, 4, Menu.NONE, "Mis datos");
                menu.getItem(2).setIcon(R.drawable.ic_menu_manage);
                menu.add(R.id.lista, 5, Menu.NONE, "Cerrar Sesion");
                menu.getItem(3).setIcon(R.drawable.ic_menu_camera);
        }

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
        if (idusuario.equals("0")){
            if (id == 1) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();
            }
            if (id == 2) {
                Intent intent = new Intent(this, Registrarme.class);
                this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Registrarme",Toast.LENGTH_LONG).show();
            }
            if (id == 3) {
                Bundle idusu= new Bundle();
                idusu.putString("usu",idusuario);

                Productos proVF=new Productos();
                proVF.setArguments(idusu);
                android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor,proVF);
                transaction.commit();
            }
        }else{
            if (id == 1) {
                Bundle idusu= new Bundle();
                idusu.putString("usu",idusuario);

                Productos proVF=new Productos();
                proVF.setArguments(idusu);
                android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor,proVF);
                transaction.commit();
            }
            if (id == 2) {
                Bundle idusu= new Bundle();
                idusu.putString("idusuario",idusuario);

                Cliente cliVF=new Cliente ();
                cliVF.setArguments(idusu);

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
            if (id == 4) {
              //  Intent intent = new Intent(this, Principal.class);
              //  this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Mis datos",Toast.LENGTH_LONG).show();
            }
            if (id == 5) {
                Intent intent = new Intent(this, Principal.class);
                this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Ok",Toast.LENGTH_LONG).show();
            }
        }
        }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
