package dcc.ufla.br.helpapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtSenha;
    Button btnEntrarLogin;
    TextView txtCadastrarLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Help - Login");

        //get Ui Elements
        edtEmail = (EditText)findViewById(R.id.edtEmailLogin);
        edtSenha = (EditText)findViewById(R.id.edtSenhaLogin);
        btnEntrarLogin =(Button)findViewById(R.id.btnEntrarLogin);
        txtCadastrarLogin = (TextView)findViewById(R.id.txtCadastrarLogin);

        txtCadastrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),CadastrarUsuarioActivity.class);
                startActivity(i);
            }
        });
    }
}
