package dcc.ufla.br.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtSenha;
    Button btnEntrarLogin;
    TextView txtCadastrarLogin;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    public static final  Pattern ValidEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Help - Login");
        //get instance
        auth = FirebaseAuth.getInstance();
        //get Ui Elements
        edtEmail = (EditText)findViewById(R.id.edtEmailLogin);
        edtSenha = (EditText)findViewById(R.id.edtSenhaLogin);
        btnEntrarLogin =(Button)findViewById(R.id.btnEntrarLogin);
        txtCadastrarLogin = (TextView)findViewById(R.id.txtCadastrarLogin);
        progressDialog = new ProgressDialog(LoginActivity.this);

        txtCadastrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CadastrarUsuarioActivity.class));
            }
        });


        final Matcher matcher;

        btnEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validEmail(edtEmail.getText().toString())){
                    edtEmail.setError("Seu email está inválido: exemplo@exemplo.com é válido");
                    return;
                }

                if(!validSenha(edtSenha.getText().toString())){
                    edtSenha.setError("Sua senha deve ter no mínimo 6 caracteres ...");
                    return;
                }

                progressDialog.setMessage("Logando");
                progressDialog.show();

                auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            progressDialog.dismiss();



                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                        else{
                            //Intent i = new Intent (LoginActivity.this, DashActivity.class);
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Logado com sucesso" ,Toast.LENGTH_SHORT).show();
                            //startActivity(
                        }
                    }
                });
            }
        });

    }
    public static boolean validEmail(String email){
        Matcher matcher  = ValidEmail.matcher(email);
        return matcher.find();
    }
    public static boolean validSenha(String senha){
        if(senha.length() > 5)
            return true;

        return false;
    }


}
