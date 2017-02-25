package dcc.ufla.br.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dcc.ufla.br.helpapp.models.User;


public class CadastrarUsuarioActivity extends AppCompatActivity{


    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtRepetirSenha;
    private EditText edtEndereco;
    private Button btnConfirmarCadastro;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);




        //get instance
        auth  = FirebaseAuth.getInstance();

        //Get UI elements
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        edtRepetirSenha = (EditText)findViewById(R.id.edtRepetirSenha);
        edtEndereco = (EditText)findViewById(R.id.edtEndereco);
        btnConfirmarCadastro = (Button)findViewById(R.id.btnConfirmarCadastro);


        btnConfirmarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validar campos antes de mandar

                if(!Validator.validEmail(edtEmail.getText().toString())){
                    edtEmail.setError("Seu email está inválido: exemplo@exemplo.com é válido");
                    return;
                }
                if(!Validator.validSenha(edtSenha.getText().toString())){
                    edtSenha.setError("Sua senha deve ter no mínimo 6 caracteres ...");
                    return;
                }
                if(!Validator.validConfirmaSenha(edtSenha.getText().toString(),edtRepetirSenha.getText().toString())){
                    edtRepetirSenha.setError("A senhas diferem, verifique sua senha e sua confirmação");
                    return;
                }

                final User user = new User(edtNome.getText().toString(),edtEmail.getText().toString(),edtSenha.getText().toString()
                ,edtEndereco.getText().toString());

                progressDialog = new ProgressDialog(CadastrarUsuarioActivity.this);
                progressDialog.setMessage("Cadastrando seus dados");
                progressDialog.show();
                auth.createUserWithEmailAndPassword(user.email,user.senha).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                }).addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                DatabaseReference usersRef = mRootRef.child("users");
                                usersRef.push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getBaseContext(),"Dados salvos com sucesso" ,Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(CadastrarUsuarioActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }

                                });
                            }
                        });

            }
        });





    }


}
