package com.example.appiot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class QRCodeActivity extends AppCompatActivity {

    TextView txtLogout;
    BottomNavigationView bottomNavigation;
    ViewFlipper v_flipper;
    CardView btnQRCode;

    private FirebaseAuth auth;
    private FirebaseUser user;

    Intent loginActivity;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        loginActivity = new Intent(this, LoginActivity.class);
        inicializarBotoes();
        acoesNavigation();
        acoesBotoes();
        bottomNavigation.setBackgroundColor(Color.parseColor("#eceff1"));
        int images[] = {R.drawable.flipper04, R.drawable.flipper01};
        for(int image: images) {
            flipperImages(image);
        }
    }

    private void inicializarBotoes() {
        btnQRCode = (CardView) findViewById(R.id.btnQRCode);
        txtLogout = (TextView) findViewById(R.id.txtLogout);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        v_flipper = (ViewFlipper) findViewById(R.id.v_flipper);
    }

    private void acoesBotoes() {
        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Camera Scan");
                integrator.setBarcodeImageEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(true);
                integrator.setCameraId(0);
                integrator.initiateScan();
            }
        });

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
        v_flipper.setFlipInterval(8000);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        vibracao();
        if(result != null) {
            if(result.getContents() != null) {
                acaoQRCode(result.getContents());
            } else {
                acaoQRCode("Scan cancelado.");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void acaoQRCode(String msg) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(msg));
        startActivity(browserIntent);
    }

    private void vibracao() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

}
