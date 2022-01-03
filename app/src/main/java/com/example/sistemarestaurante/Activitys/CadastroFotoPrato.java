package com.example.sistemarestaurante.Activitys;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Helper.Permissao;
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
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroFotoPrato extends AppCompatActivity {

    //XMl
    private CircleImageView circleImagePrato;
    private ImageButton imageButtonCameraPrato, imageButtonGaleriaPrato;
    //model
    private Prato prato;
    private static final int SELECAO_CAMERA = 200;
    private static final int SELECAO_GALERIA = 300;

    //Firebase
    private  DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private StorageReference storageReference = ConfiguracaoFirebase.getStorageReference();

    private String [] permissoesnecessarias = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_foto_prato);

        //configurações inicias
        circleImagePrato = findViewById(R.id.circleImageBebida);
        imageButtonCameraPrato = findViewById(R.id.imageButtonCameraPrato);
        imageButtonGaleriaPrato = findViewById(R.id.imageButtonGaleriaPrato);
        Permissao.validarPermissoes(permissoesnecessarias,this,2);


        //recuperando extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            prato = (Prato) bundle.getSerializable("prato");
        }

        //abrindo camera para setar imagem
        imageButtonCameraPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });
        imageButtonGaleriaPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivityForResult(i, SELECAO_GALERIA);
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
                case SELECAO_CAMERA:
                    imagem = (Bitmap) data.getExtras().get("data");
                    break;
                case SELECAO_GALERIA:
                    Uri uriImagemSelecionada = data.getData();
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),uriImagemSelecionada);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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