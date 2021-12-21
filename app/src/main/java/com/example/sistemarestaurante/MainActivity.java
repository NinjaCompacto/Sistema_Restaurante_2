package com.example.sistemarestaurante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Firebase.UsuarioFireBase;
import com.example.sistemarestaurante.Funcoes.BarActivity;
import com.example.sistemarestaurante.Funcoes.CaixaActivity;
import com.example.sistemarestaurante.Funcoes.CentralReposicaoActivity;
import com.example.sistemarestaurante.Funcoes.CozinhaActivity;
import com.example.sistemarestaurante.Funcoes.EstoqueActivity;
import com.example.sistemarestaurante.Funcoes.GarcomActivity;
import com.example.sistemarestaurante.Model.Base64Custom;
import com.example.sistemarestaurante.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    //Firebase
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();

    //XML
    private TextView textViewBoasVindas;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurações iniciais
        textViewBoasVindas = findViewById(R.id.textViewBoasVindas);
        createNotificationChannel();



        //recupearando email do usuario logado e  tranformando em seu id
        String emailUsuarioLogado = UsuarioFireBase.getUsuarioLogado().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuarioLogado);

        DatabaseReference funcionariosRef = databaseReference.child("funcionarios").child(idUsuario);
        funcionariosRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    usuario = task.getResult().getValue(Usuario.class);
                    textViewBoasVindas.setText("Bem-Vindo, "+ usuario.getNome() + " !");
                    Log.d("firebase",usuario.getFuncao());

                    switch (usuario.getFuncao()){
                        case "Garçom" :
                            Intent iGarçom = new Intent(MainActivity.this, GarcomActivity.class);
                            finish();
                            startActivity(iGarçom);
                            break;
                        case "Estoque":
                            Intent iEstoque = new Intent(MainActivity.this, EstoqueActivity.class);
                            finish();
                            startActivity(iEstoque);
                            break;
                        case "Bar":
                            Intent iBar = new Intent(MainActivity.this,BarActivity.class);
                            finish();
                            startActivity(iBar);
                            break;
                        case "Cozinha":
                            Intent iCozinha = new Intent(MainActivity.this, CozinhaActivity.class);
                            finish();
                            startActivity(iCozinha);
                            break;
                        case "Central de Reposição":
                            Intent iCentral= new Intent(MainActivity.this, CentralReposicaoActivity.class);
                            finish();
                            startActivity(iCentral);
                        case "Caixa" :
                            Intent iCaixa= new Intent(MainActivity.this, CaixaActivity.class);
                            finish();
                            startActivity(iCaixa);
                    }
                }
            }
        });



    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}