package com.example.appiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SearchActivity extends AppCompatActivity {


    TextView txtLogout;
    BottomNavigationView bottomNavigation;

    private FirebaseAuth auth;
    private FirebaseUser user;

    Intent loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        loginActivity = new Intent(this, LoginActivity.class);
        inicializarBotoes();
        acoesNavigation();
        acoesBotoes();
        bottomNavigation.setBackgroundColor(Color.parseColor("#eceff1"));
    }

    private void inicializarBotoes() {
        txtLogout = (TextView) findViewById(R.id.txtLogout);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
    }

    private void acoesBotoes() {
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(SearchActivity.this).create();
                alertDialog.setMessage("Deseja sair do app?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SIM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Conexao.logOut();
                                startActivity(loginActivity);
                                finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NÃO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void acoesNavigation() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent a = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                        break;
                    case R.id.nav_qrcode:
                        Intent b = new Intent(SearchActivity.this, QRCodeActivity.class);
                        startActivity(b);
                        finish();
                        break;
                    case R.id.nav_search:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        verificarUser();
    }

    private void verificarUser() {
        if(user == null) {
            finish();
        } else {

        }
    }

}
