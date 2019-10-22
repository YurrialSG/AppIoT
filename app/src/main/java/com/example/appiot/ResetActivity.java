package com.example.appiot;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    CardView btnReset;
    CardView btnVoltar;

    EditText edtEmail;

    //Telas utilizadas
    Intent loginActivity;

    private FirebaseAuth auth;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        loginActivity = new Intent(this, LoginActivity.class);
        alertDialog = new android.app.AlertDialog.Builder(ResetActivity.this).create();
        alertDialog.setMessage("Carregando...");
        inicializarBotoes();
        acoesBotoes();
    }

    private void inicializarBotoes() {
        btnReset = (CardView) findViewById(R.id.btnReset);
        btnVoltar = (CardView) findViewById(R.id.btnVoltar);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
    }

    private void acoesBotoes() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().trim().isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ResetActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Preencha com e-mail cadastrado no sistema.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    String email = edtEmail.getText().toString().trim();
                    alertDialog.show();
                    resetSenha(email);
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(loginActivity);
                finish();
            }
        });
    }

    private void resetSenha(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(ResetActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        if(task.isSuccessful()) {
                            alert("E-mail enviado para alterar a senha");
                            startActivity(loginActivity);
                            finish();
                        } else {
                            alert("E-mail não cadastrado no sistema.");
                            edtEmail.setText("");
                            edtEmail.setFocusable(true);
                        }
                    }
                });
    }

    private void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
