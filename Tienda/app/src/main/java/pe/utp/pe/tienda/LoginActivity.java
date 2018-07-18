package pe.utp.pe.tienda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pe.utp.pe.tienda.Vista.Principal;
import pe.utp.pe.tienda.controlador.Usuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button ingresar,regresar,registrarme;
    EditText usuario, contrasenia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ingresar = findViewById(R.id.ingresar);
        regresar = findViewById(R.id.regresar);
        registrarme = findViewById(R.id.registrarme);
        ingresar.setOnClickListener(this);
        registrarme.setOnClickListener(this);
        regresar.setOnClickListener(this);
        usuario= findViewById(R.id.usuario);
        contrasenia = findViewById(R.id.contrasenia);
    }
    @Override
    public void onClick(View v) {
        if (v==ingresar){
            if (TextUtils.isEmpty(usuario.getText().toString()) || TextUtils.isEmpty(contrasenia.getText().toString())){
                Toast.makeText(getApplicationContext(),"Ingresar Usuario o contraseña",Toast.LENGTH_LONG).show();
            }else{
                Usuario usuob=new Usuario();
                usuob.ingresarASY(usuario.getText().toString(),contrasenia.getText().toString(),this);
            }
        }
        if (v==regresar){
            Intent princii = new Intent(this, Principal.class);
            this.startActivity(princii);
            //Toast.makeText(getApplicationContext(),"Registrarme",Toast.LENGTH_LONG).show();

        }

        if (v==registrarme){
            Intent intent = new Intent(this, Registrarme.class);
            this.startActivity(intent);
            //Toast.makeText(getApplicationContext(),"Registrarme",Toast.LENGTH_LONG).show();
        }
    }
}
