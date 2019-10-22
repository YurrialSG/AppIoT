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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    CardView btnRegister;
    CardView btnVoltar;
    EditText edtEmail;
    EditText edtPassword;

    //telas utilizadas
    Intent loginActivity;

    AlertDialog alertDialog;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginActivity = new Intent(this, LoginActivity.class);
        alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setMessage("Carregando...");
        inicializarBotoes();
        acoesBotoes();
    }

    private void inicializarBotoes() {
        btnRegister = (CardView) findViewById(R.id.btnRegister);
        btnVoltar = (CardView) findViewById(R.id.btnVoltar);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    private void acoesBotoes() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                if(edtEmail.getText().toString().trim().isEmpty() ||
                   edtPassword.getText().toString().trim().isEmpty()) {
                    alertDialog.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Preencha os campos...");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if(edtPassword.getText().toString().length() <= 5) {
                    alertDialog.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Senha deve ser >= 6 caracteres.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    String email = edtEmail.getText().toString().trim();
                    String passsword = edtPassword.getText().toString().trim();
                    criarUser(email, passsword);
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

    private void criarUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        alertDialog.dismiss();
                        if(task.isSuccessful()) {
                            alert("Usuário cadastrado com sucesso!");
                            startActivity(loginActivity);
                            finish();
                        } else {
                            alert(task.getException().toString());
                            alert("Erro no cadastro de usuário.");
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
