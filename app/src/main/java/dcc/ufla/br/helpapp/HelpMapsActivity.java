package dcc.ufla.br.helpapp;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dcc.ufla.br.helpapp.models.Ponto;

public class HelpMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Button btnAddMeuLocal;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private AlertDialog.Builder builder;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnAddMeuLocal = (Button)findViewById(R.id.btnAddMeuLocal);




        if(mGoogleApiClient == null){

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this).
                    addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        builder = new AlertDialog.Builder(HelpMapsActivity.this);

    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);

            mMap.setMyLocationEnabled(true);
        }

        mRootRef.child("pontos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ponto : dataSnapshot.getChildren()){
                    Ponto p = ponto.getValue(Ponto.class);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(),p.getLongitude()))
                            .title(p.getNome())
                            .snippet(p.getDescricao()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);

        final MarkerOptions markerUser = new MarkerOptions();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                inflater = HelpMapsActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_ponto_mapa_dialog,null);

                builder.setView(dialogView).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText edtAddPontoNome = (EditText)dialogView.findViewById(R.id.edtAddPontoNome);
                        final EditText edtAddPontoDescricao = (EditText)dialogView.findViewById(R.id.edtAddPontoDescricao);
                        final Ponto ponto = new Ponto(edtAddPontoNome.getText().toString(),edtAddPontoDescricao.getText().toString(),
                                latLng.latitude,latLng.longitude);

                        DatabaseReference pontosRef = mRootRef.child("pontos");
                        pontosRef.push().setValue(ponto).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mMap.addMarker(new MarkerOptions().position(latLng)
                                        .title(ponto.getNome())
                                        .snippet(ponto.getDescricao()));
                            }
                        });
                    }
                }).create().show();


            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);

        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        btnAddMeuLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation != null){

                    inflater = HelpMapsActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.add_ponto_mapa_dialog,null);

                    builder.setView(dialogView).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final EditText edtAddPontoNome = (EditText)dialogView.findViewById(R.id.edtAddPontoNome);
                            final EditText edtAddPontoDescricao = (EditText)dialogView.findViewById(R.id.edtAddPontoDescricao);
                            final Ponto ponto = new Ponto(edtAddPontoNome.getText().toString(),edtAddPontoDescricao.getText().toString(),
                                    mLastLocation.getLatitude(),mLastLocation.getLongitude());
                            DatabaseReference pontosRef = mRootRef.child("pontos");
                            pontosRef.push().setValue(ponto).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    mMap.addMarker(new MarkerOptions().position(new LatLng(ponto.getLatitude(),ponto.getLongitude()))
                                            .title(ponto.getNome()).snippet(ponto.getDescricao()));

                                }
                            });

                        }
                    }).create().show();

                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
