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
import android.widget.ImageButton;
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
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FotoCadastroActivity extends AppCompatActivity {
    //permissões
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //XML
    private CircleImageView circleImageView;
    private StorageReference storageReference;
    private ImageButton imageButtonCamera, imageButtonGaleria;
    //Model
    private Usuario usuario;
    private final static int SELECAO_CAMERA = 600;
    private final static int SELECAO_GALERIA = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_cadastro);

        //configurações iniciais
        circleImageView = findViewById(R.id.circleImageView);
        storageReference = ConfiguracaoFirebase.getStorageReference();
        imageButtonCamera = findViewById(R.id.imageButtonCameraCadastro);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleriaCadastro);

        //recupera dados do usuario
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            usuario = (Usuario) bundle.getSerializable("dadosusuario");
        }

        //permissão para validação
        Permissao.validarPermissoes(permissoesNecessarias,this,1);

        //seta listener para a imagem
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivityForResult(i, 100);
                }
            }
        });
        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
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
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {

                switch (requestCode){
                    //recupera imagem capturada pela camera
                    case SELECAO_CAMERA:
                        imagem = (Bitmap)data.getExtras().get("data");
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