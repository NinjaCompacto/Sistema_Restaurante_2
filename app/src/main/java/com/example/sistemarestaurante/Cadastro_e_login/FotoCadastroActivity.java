package com.example.sistemarestaurante.Cadastro_e_login;

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
import android.widget.Button;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Helper.Permissao;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class FotoCadastroActivity extends AppCompatActivity {
    //permissões
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA
    };
    //XML
    private CircleImageView circleImageView;
    private StorageReference storageReference;
    //Model
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_cadastro);

        //configurações iniciais
        circleImageView = findViewById(R.id.circleImageView);
        storageReference = ConfiguracaoFirebase.getStorageReference();

        //recupera dados do usuario
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            usuario = (Usuario) bundle.getSerializable("dadosusuario");
        }

        //permissão para validação
        Permissao.validarPermissoes(permissoesNecessarias,this,1);

        //seta listener para a imagem
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivityForResult(i, 100);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {

                switch (requestCode){
                    //recupera imagem capturada pela camera
                    case 100 :
                        imagem = (Bitmap)data.getExtras().get("data");
                        break;
                }


                if (imagem != null) {
                    //seta imagem no ImageView
                    circleImageView.setImageBitmap(imagem);

                    //recuperar dados da imagem para o FirebaseStorage
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //salvar imagem no firebase
                    final StorageReference imagemref = storageReference
                            .child("Imagens")
                            .child("perfil")
                            .child(usuario.getId())
                            .child("perfil.jpeg");
                    UploadTask uploadTask = imagemref.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FotoCadastroActivity.this,"Erro ao fazer Upload da Imagem",Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    usuario.setFoto(url.toString());
                                    //salva dados do usuario no firebase Database
                                    usuario.salvarUsuario();
                                    Toast.makeText(FotoCadastroActivity.this, "Sucesso ao enviar imagem",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}