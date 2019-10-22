package com.example.appiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    CardView btnLogin;
    CardView btnRegister;
    TextView txtEsqueceuSenha;

    //Telas para utilização
    Intent mainActivity;
    Intent registerActivity;
    Intent resetActivity;

    private FirebaseAuth auth;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setMessage("Carregando...");
        mainActivity = new Intent(this, MainActivity.class);
        registerActivity = new Intent(this, RegisterActivity.class);
        resetActivity = new Intent(this, ResetActivity.class);
        inicializarBotoes();
        acoesBotoes();
    }

    public void inicializarBotoes() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (CardView) findViewById(R.id.btnLogin);
        btnRegister= (CardView) findViewById(R.id.btnRegister);
        txtEsqueceuSenha = (TextView) findViewById(R.id.txtEsqueceuSenha);
    }

    public void acoesBotoes() {
        //ação btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                alertDialog.show();
                login(email, password);
            }
        });

        //ação btnRegister
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(registerActivity);
            }
        });

        //ação txtEsqueceuSenha
        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(resetActivity);
            }
        });
    }

    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        alertDialog.dismiss();
                        if(task.isSuccessful()) {
                            startActivity(mainActivity);
                            finish();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("E-mail/Senha incorreto(s).");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
