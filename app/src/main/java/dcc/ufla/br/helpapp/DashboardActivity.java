package dcc.ufla.br.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Painel de ajuda");

        setSupportActionBar(toolbar);

        //iniatilizing the progress dialog
        progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Carregando dados");
        progressDialog.show();

        //get the instance of the textview

        txtBemvindo = (TextView)findViewById(R.id.txtBemvindo);
        btnAddPontoMapa = (Button)findViewById(R.id.btnAddPontoMapa);




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

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(new PrimaryDrawerItem().withName("Logout").withIdentifier(1))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if(drawerItem.getIdentifier() == 1){
                            FirebaseAuth.getInstance().signOut();
                            Intent i = new Intent(DashboardActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                        return true;
                    }
                })
                .build();


    }
}
