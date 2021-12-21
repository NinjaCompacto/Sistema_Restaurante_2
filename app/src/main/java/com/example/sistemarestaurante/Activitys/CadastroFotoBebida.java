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
import com.example.sistemarestaurante.Model.Bebida;
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

public class CadastroFotoBebida extends AppCompatActivity {

    //Firebase
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private StorageReference storageReference = ConfiguracaoFirebase.getStorageReference();

    //XMl
    private CircleImageView circleImageBebida;
    //Model
    private Bebida bebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_foto_bebida);

        //configurações iniciais
        circleImageBebida = findViewById(R.id.circleImageBebida);

        //recuperando dados da bebida
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            bebida = (Bebida) bundle.getSerializable("bebida");
        }

        //seta evento de click para o circleimageviw
        circleImageBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(i,300);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            Bitmap bitmap = null;
            switch (requestCode){
                case 300:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    break;
            }
            if (bitmap != null){
                circleImageBebida.setImageBitmap(bitmap);

                //recupera dados da imagem para o firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,baos);
                byte[] dadosimagem = baos.toByteArray();

                //salva imagem no storage
                final StorageReference imagemBebidaref = storageReference
                        .child("Imagens")
                        .child("bebidas")
                        .child(bebida.getNomeBebida());
                UploadTask uploadTask = imagemBebidaref.putBytes(dadosimagem);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CadastroFotoBebida.this,"Erro ao fazer Upload da Imagem",Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagemBebidaref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri url = task.getResult();
                                bebida.setFoto(url.toString());
                                bebida.salvarBebida();
                                Toast.makeText(CadastroFotoBebida.this,"Sucesso ao fazer Upload da Imagem",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                });

            }
            else {
                Toast.makeText(CadastroFotoBebida.this,"Adinione uma foto à bebida", Toast.LENGTH_LONG).show();
            }

        }
    }
}