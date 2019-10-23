package com.example.appiot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    static String MqttHost = "tcp://maqiatto.com:1883";
    static String Username = "yuriinternacional86@gmail.com";
    static String Password = "1234";
    String topicStr = "yuriinternacional86@gmail.com/led/";

    MqttAndroidClient client;
    TextView txtTemp;
    TextView txtLogout;

    CardView btnLed01;
    CardView btnLed02;
    CardView btnLed03;
    CardView btnLed04;
    CardView btnLed05;
    CardView btnLed06;
    CardView btnLed07;
    CardView btnLed08;

    Switch btnLedAll;
    Switch btnVibracao;
    Switch btnSom;

    String sendMqtt01;
    String sendMqtt02;
    String sendMqtt03;
    String sendMqtt04;
    String sendMqttAll;

    ImageView imgLed01;
    ImageView imgLed02;
    ImageView imgLed03;
    ImageView imgLed04;

    TextView txtUser;
    TextView txtLoading;

    GridLayout gridLayout;
    BottomNavigationView bottomNavigation;

    //Telas utilizadas
    Intent loginActivity;

    LinearLayout mainActivity;

    ProgressBar progressBar;
    //int i = 0;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginActivity = new Intent(this, LoginActivity.class);

        inicializarBotoes();

        acoesNotConnected();

        //progressBar.setMax(100);
        //progressBar.setProgress(i);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this, MqttHost, clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(Username);
            options.setPassword(Password.toCharArray());
            final IMqttToken token = client.connect(options);

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        //while (i != 100) {
                            //progressBar.setProgress(i);
                            //sleep(100);
                            //i++;
                            token.setActionCallback(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    acoesConnected();
                                }
                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    // Something went wrong e.g. connection timeout or firewall problems
                                    Toast.makeText(MainActivity.this, "Erro ao conectar com MQTT", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, asyncActionToken.toString(), Toast.LENGTH_LONG).show();
                                    Toast.makeText(MainActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                            //client.subscribe("yuriinternacional86@gmail.com/temperatura/", 1);
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void acoesNotConnected() {
        btnLed01.setEnabled(false);
        btnLed02.setEnabled(false);
        btnLed03.setEnabled(false);
        btnLed04.setEnabled(false);
        btnLed05.setEnabled(false);
        btnLed06.setEnabled(false);
        btnLedAll.setEnabled(false);
        txtLogout.setVisibility(View.INVISIBLE);
        btnLedAll.setVisibility(View.INVISIBLE);
        btnVibracao.setVisibility(View.INVISIBLE);
        btnSom.setVisibility(View.INVISIBLE);
        txtTemp.setVisibility(View.INVISIBLE);
        txtUser.setVisibility(View.INVISIBLE);
        gridLayout.setVisibility(View.INVISIBLE);
        bottomNavigation.setVisibility(View.INVISIBLE);
        txtLoading.setVisibility(View.VISIBLE);
        //progressBar.setVisibility(View.VISIBLE);
        //mainActivity.setBackground(ContextCompat.getDrawable(this, R.drawable.bg2));
    }

    public void acoesConnected() {
        // We are connected
        //progressBar.setProgress(100);
        //i = 100;
        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        //deixar componentes visible
        txtLogout.setVisibility(View.VISIBLE);
        btnLedAll.setVisibility(View.VISIBLE);
        btnVibracao.setVisibility(View.VISIBLE);
        btnSom.setVisibility(View.VISIBLE);
        txtTemp.setVisibility(View.VISIBLE);
        txtUser.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
        bottomNavigation.setVisibility(View.VISIBLE);
        txtLoading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        btnLed01.setEnabled(true);
        btnLed02.setEnabled(true);
        btnLed03.setEnabled(true);
        btnLed04.setEnabled(true);
        btnLed05.setEnabled(true);
        btnLed06.setEnabled(true);
        btnLedAll.setEnabled(true);
        sendMqtt01 = "off1";
        sendMqtt02 = "off2";
        sendMqtt03 = "off3";
        sendMqtt04 = "off4";
        sendMqttAll = "offAll";
        enviarMqttDesligaLed("offAll", btnLed01, "off", "All");
        enviarMqttDesligaLed("offAll", btnLed02, "off", "All");
        enviarMqttDesligaLed("offAll", btnLed03, "off", "All");
        enviarMqttDesligaLed("offAll", btnLed04, "off", "All");
        subscribeMqttChannel("yuriinternacional86@gmail.com/temperatura/");
        mainActivity.setBackground(ContextCompat.getDrawable(this, R.drawable.bg));
        acoesBotoes();
        acoesNavigation();
    }

    public void acoesBotoes() {
        btnLed01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLedAll.setChecked(false);
                sendMqttAll = "offAll";
                if("off1".equals(sendMqtt01)) {
                    enviarMqttLigaLed("on1", btnLed01, "on", "1");
                }else if("on1".equals(sendMqtt01)){
                    enviarMqttDesligaLed("off1", btnLed01, "off", "1");
                }
            }
        });

        btnLed02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLedAll.setChecked(false);
                sendMqttAll = "offAll";
                if("off2".equals(sendMqtt02)) {
                    enviarMqttLigaLed("on2", btnLed02, "on", "2");
                }else if("on2".equals(sendMqtt02)){
                    enviarMqttDesligaLed("off2", btnLed02, "off", "2");
                }
            }
        });

        btnLed03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLedAll.setChecked(false);
                sendMqttAll = "offAll";
                if("off3".equals(sendMqtt03)) {
                    enviarMqttLigaLed("on3", btnLed03, "on", "3");
                }else if("on3".equals(sendMqtt03)){
                    enviarMqttDesligaLed("off3", btnLed03, "off", "3");
                }
            }
        });

        btnLed04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLedAll.setChecked(false);
                sendMqttAll = "offAll";
                if("off4".equals(sendMqtt04)) {
                    enviarMqttLigaLed("on4", btnLed04, "on", "4");
                }else if("on4".equals(sendMqtt04)){
                    enviarMqttDesligaLed("off4", btnLed04, "off", "4");
                }
            }
        });

        btnLed05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Nenhum dispositivo configurado, portanto, o botão está bloqueado.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        btnLed06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Nenhum dispositivo configurado, portanto, o botão está bloqueado.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        btnLed07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Nenhum dispositivo configurado, portanto, o botão está bloqueado.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        btnLed08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Nenhum dispositivo configurado, portanto, o botão está bloqueado.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        btnLedAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("offAll".equals(sendMqttAll)) {
                    enviarMqttLigaLed("onAll", btnLed01, "on", "All");
                    enviarMqttLigaLed("onAll", btnLed02, "on", "All");
                    enviarMqttLigaLed("onAll", btnLed03, "on", "All");
                    enviarMqttLigaLed("onAll", btnLed04, "on", "All");
                }else if("onAll".equals(sendMqttAll)){
                    enviarMqttDesligaLed("offAll", btnLed01, "off", "All");
                    enviarMqttDesligaLed("offAll", btnLed02, "off", "All");
                    enviarMqttDesligaLed("offAll", btnLed03, "off", "All");
                    enviarMqttDesligaLed("offAll", btnLed04, "off", "All");
                }
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
                        break;
                    case R.id.nav_qrcode:
                        Intent a = new Intent(MainActivity.this, QRCodeActivity.class);
                        startActivity(a);
                        finish();
                        break;
                    case R.id.nav_search:
                        Intent b = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(b);
                        finish();
                        break;
                }
                return false;
            }
        });
    }


    public void inicializarBotoes(){
        txtTemp = (TextView) findViewById(R.id.txtTemperatura);
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtLoading = (TextView) findViewById(R.id.txtLoading);
        txtLogout = (TextView) findViewById(R.id.txtLogout);
        btnLed01 = (CardView) findViewById(R.id.btnLed01);
        btnLed02 = (CardView) findViewById(R.id.btnLed02);
        btnLed03 = (CardView) findViewById(R.id.btnLed03);
        btnLed04 = (CardView) findViewById(R.id.btnLed04);
        btnLed05 = (CardView) findViewById(R.id.btnLed05);
        btnLed06 = (CardView) findViewById(R.id.btnLed06);
        btnLed07 = (CardView) findViewById(R.id.btnLed07);
        btnLed08 = (CardView) findViewById(R.id.btnLed08);
        btnLedAll = (Switch) findViewById(R.id.btnLedAll);
        btnVibracao = (Switch) findViewById(R.id.btnVibracao);
        btnSom = (Switch) findViewById(R.id.btnSom);
        imgLed01 = (ImageView) findViewById(R.id.imgLed01);
        imgLed02 = (ImageView) findViewById(R.id.imgLed02);
        imgLed03 = (ImageView) findViewById(R.id.imgLed03);
        imgLed04 = (ImageView) findViewById(R.id.imgLed04);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        mainActivity = (LinearLayout) findViewById(R.id.mainActivity);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
    }

    public void enviarMqttLigaLed(String msg, CardView btnOn, String acao, String posicao) {
        String topic = topicStr;
        String message = msg;
        try {
            client.publish(topic, message.getBytes(), 0, false);
            btnOn.setBackgroundColor(Color.parseColor("#00e676"));
            String recebido = acao + posicao;
            if("1".equals(posicao)){
                sendMqtt01 = recebido;
                imgLed01.setImageResource(R.drawable.on);
            } else if("2".equals(posicao)) {
                sendMqtt02 = recebido;
                imgLed02.setImageResource(R.drawable.on);
            } else if("3".equals(posicao)) {
                sendMqtt03 = recebido;
                imgLed03.setImageResource(R.drawable.on);
            } else if("4".equals(posicao)) {
                sendMqtt04 = recebido;
                imgLed04.setImageResource(R.drawable.on);
            } else {
                sendMqttAll = recebido;
                sendMqtt01 = "on1";
                sendMqtt02 = "on2";
                sendMqtt03 = "on3";
                sendMqtt04 = "on4";
                imgLed01.setImageResource(R.drawable.on);
                imgLed02.setImageResource(R.drawable.on);
                imgLed03.setImageResource(R.drawable.on);
                imgLed04.setImageResource(R.drawable.on);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void enviarMqttDesligaLed(String msg, CardView btnOn, String acao, String posicao) {
        String topic = topicStr;
        String message = msg;
        try {
            client.publish(topic, message.getBytes(), 0, false);
            btnOn.setBackgroundColor(Color.WHITE);
            String recebido = acao + posicao;
            if("1".equals(posicao)){
                sendMqtt01 = recebido;
                imgLed01.setImageResource(R.drawable.off);
            } else if("2".equals(posicao)) {
                sendMqtt02 = recebido;
                imgLed02.setImageResource(R.drawable.off);
            } else if("3".equals(posicao)) {
                sendMqtt03 = recebido;
                imgLed03.setImageResource(R.drawable.off);
            } else if("4".equals(posicao)) {
                sendMqtt04 = recebido;
                imgLed04.setImageResource(R.drawable.off);
            } else {
                sendMqttAll = recebido;
                sendMqtt01 = "off1";
                sendMqtt02 = "off2";
                sendMqtt03 = "off3";
                sendMqtt04 = "off4";
                imgLed01.setImageResource(R.drawable.off);
                imgLed02.setImageResource(R.drawable.off);
                imgLed03.setImageResource(R.drawable.off);
                imgLed04.setImageResource(R.drawable.off);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribeMqttChannel(String channelName) {
        try {
            txtTemp = (TextView) findViewById(R.id.txtTemperatura);
            if (client.isConnected()) {
                client.subscribe(channelName, 0);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        txtTemp.setText(new String(message.getPayload()) + " º C");

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            }
        } catch (Exception e) {
            Log.d("tag","Error :" + e);
        }
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
            txtUser.setText("E-mail: " + user.getEmail());
        }
    }
}
