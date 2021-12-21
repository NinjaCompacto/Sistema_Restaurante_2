package com.example.sistemarestaurante.Activitys;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroFotoPrato extends AppCompatActivity {

    //XMl
    private CircleImageView circleImagePrato;
    //model
    private Prato prato;
    //Firebase
    private  DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private StorageReference storageReference = ConfiguracaoFirebase.getStorageReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_foto_prato);

        //configurações inicias
        circleImagePrato = findViewById(R.id.circleImageBebida);


        //recuperando extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            prato = (Prato) bundle.getSerializable("prato");
        }

        //abrindo camera para setar imagem
        circleImagePrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivityForResult(i, 200);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap imagem = null;

            switch (requestCode){
                case 200:
                    imagem = (Bitmap) data.getExtras().get("data");
                    break;
            }
            if(imagem != null){
                //seta imagem no circleimageView
                circleImagePrato.setImageBitmap(imagem);

                //recupera dados da imagem para o firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                byte[] dadosImagem = baos.toByteArray();

                //salva imagem no storage
                final StorageReference imagemPratoref = storageReference
                        .child("Imagens")
                        .child("pratos")
                        .child(prato.getNomePrato());
                //upando imagem para storage
                UploadTask uploadTask = imagemPratoref.putBytes(dadosImagem);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CadastroFotoPrato.this,"Erro ao fazer Upload da Imagem",Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagemPratoref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri url = task.getResult();
                                prato.setFoto(url.toString());
                                prato.salvarPrato();
                                Toast.makeText(CadastroFotoPrato.this,"Sucesso ao fazer Upload da Imagem",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                });
            }
            else{
                Toast.makeText(CadastroFotoPrato.this,"Adinione uma foto à bebida", Toast.LENGTH_LONG).show();
            }
        }
    }

}