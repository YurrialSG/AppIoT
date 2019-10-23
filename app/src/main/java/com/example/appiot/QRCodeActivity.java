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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class QRCodeActivity extends AppCompatActivity {

    TextView txtLogout;
    BottomNavigationView bottomNavigation;
    ViewFlipper v_flipper;

    private FirebaseAuth auth;
    private FirebaseUser user;

    Intent loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        loginActivity = new Intent(this, LoginActivity.class);
        inicializarBotoes();
        acoesNavigation();
        acoesBotoes();
        bottomNavigation.setBackgroundColor(Color.parseColor("#eceff1"));
        int images[] = {R.drawable.img01, R.drawable.flipper01};
        for(int image: images) {
            flipperImages(image);
        }
    }

    private void inicializarBotoes() {
        txtLogout = (TextView) findViewById(R.id.txtLogout);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        v_flipper = (ViewFlipper) findViewById(R.id.v_flipper);
    }

    private void acoesBotoes() {
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(QRCodeActivity.this).create();
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
                        Intent a = new Intent(QRCodeActivity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                        break;
                    case R.id.nav_qrcode:
                        break;
                    case R.id.nav_search:
                        Intent b = new Intent(QRCodeActivity.this, SearchActivity.class);
                        startActivity(b);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(this, android.R.anim.fade_in);
        v_flipper.setOutAnimation(this, android.R.anim.fade_out);
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
