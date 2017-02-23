package dcc.ufla.br.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;


import dcc.ufla.br.helpapp.models.User;


public class DashboardActivity extends AppCompatActivity {

    private TextView txtBemvindo;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;
    private Drawer drawer;
    private Button btnAddPontoMapa;
    private Button btnVerPontosMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setTitle("Help app - Dashboard");
        //iniatilizing the progress dialog
        progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Carregando dados");
        progressDialog.show();


        //initializing the side bar

        drawer = new DrawerBuilder().withActionBarDrawerToggle(true).withActivity(this).addDrawerItems(new PrimaryDrawerItem().withIdentifier(1).withName("Logout")).build();

        //get the instance of the textview

        txtBemvindo = (TextView)findViewById(R.id.txtBemvindo);
        btnAddPontoMapa = (Button)findViewById(R.id.btnAddPontoMapa);
        btnVerPontosMapa = (Button)findViewById(R.id.btnVerPontosMapa);

        //get instance of auth

        mAuth = FirebaseAuth.getInstance();

        //getting reference

        database = FirebaseDatabase.getInstance();

        mRef = database.getReference();

        user = mAuth.getCurrentUser();

        Query userQuery = mRef.child("users").orderByChild("email").equalTo(user.getEmail());



        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for( DataSnapshot s : dataSnapshot.getChildren()){

                    User u = s.getValue(User.class);
                    txtBemvindo.setText("Bem vindo ao Help App "+ u.nome );
                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnAddPontoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,HelpMapsActivity.class);
                startActivity(i);
            }
        });

        btnVerPontosMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}
