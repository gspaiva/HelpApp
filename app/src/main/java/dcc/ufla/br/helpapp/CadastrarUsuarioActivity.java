package dcc.ufla.br.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dcc.ufla.br.helpapp.models.User;


public class CadastrarUsuarioActivity extends AppCompatActivity{


    EditText edtNome;
    EditText edtEmail;
    EditText edtSenha;
    EditText edtRepetirSenha;
    EditText edtEndereco;
    Button btnConfirmarCadastro;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        getSupportActionBar().setTitle("Cadastro de usuário");

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


                User user = new User(edtNome.getText().toString(),edtEmail.getText().toString(),edtSenha.getText().toString()
                ,edtEndereco.getText().toString());

                DatabaseReference usersRef = mRootRef.child("users");
                usersRef.push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getBaseContext(),"Dados salvos com sucesso",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });





    }


}
