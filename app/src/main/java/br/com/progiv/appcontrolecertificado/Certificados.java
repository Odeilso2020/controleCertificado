package br.com.progiv.appcontrolecertificado;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.com.progiv.appcontrolecertificado.DAO.CertificadosDAO;
import br.com.progiv.appcontrolecertificado.Model.CertificadosModel;

public class Certificados extends AppCompatActivity {

    EditText txtTitulo;
    EditText txtDescricao;
    EditText txtQuantidade;
    Button btnModificar;
    CertificadosModel editarCertificado;
    CertificadosModel certificado;
    CertificadosDAO certificadosDAO;

    //
    EditText txtFoto;
    Button btnFoto;
    ImageView img;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificados);

        certificado = new CertificadosModel();
        certificadosDAO = new CertificadosDAO(Certificados.this);

        Intent intent = getIntent();
        editarCertificado = (CertificadosModel)intent.getSerializableExtra("selectCertificado"); // criando um alias do main para certificados

        txtTitulo = (EditText)findViewById(R.id.tituloCertificado);
        txtDescricao = (EditText)findViewById(R.id.descricaoCertificado);
        txtQuantidade = (EditText)findViewById(R.id.quantidadeHoras);
        btnModificar = (Button)findViewById(R.id.btnModificar);
        txtFoto = (EditText)findViewById(R.id.textoImagem);

        //
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }

        img = (ImageView)findViewById(R.id.img1);
        btnFoto = (Button)findViewById(R.id.btnTirarFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });
        //

        if(editarCertificado != null){
            btnModificar.setText("Modificar");
            txtTitulo.setText(editarCertificado.getTitulo());
            txtDescricao.setText(editarCertificado.getDescricao());
            txtQuantidade.setText(String.valueOf(editarCertificado.getQuantidade()));
            //
            txtFoto.setText(editarCertificado.getFoto());
            try{
                byte[] imagemEmBytes;
                imagemEmBytes = Base64.decode(txtFoto.getText().toString(), Base64.DEFAULT);
                Bitmap imagemDecodificada = BitmapFactory.decodeByteArray(imagemEmBytes, 0, imagemEmBytes.length);
                img.setImageBitmap(imagemDecodificada);
            }catch (Exception ex){
                Toast.makeText(this,"Erro na recuperação", Toast.LENGTH_LONG);
            }
            //
            certificado.setId(editarCertificado.getId());
        }else{
            btnModificar.setText("Cadastrar");
        }

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                certificado.setTitulo(txtTitulo.getText().toString());
                certificado.setDescricao(txtDescricao.getText().toString());
                certificado.setQuantidade(Integer.parseInt(txtQuantidade.getText().toString()));
                certificado.setFoto(txtFoto.getText().toString());
                if(btnModificar.getText().toString().equals("Cadastrar")){
                    // Insert
                    certificadosDAO.salvarCertificado(certificado);
                }else{
                    // Update
                    certificadosDAO.alterarCertificado(certificado);
                }
                certificadosDAO.close();
                finish();
            }
        });
    }

    //
    public void tirarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap)extras.get("data");
            img.setImageBitmap(imagem);

            // Conversao
            byte[] fotoEmBytes;
            ByteArrayOutputStream streamDaFotoEmByte = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.PNG, 70, streamDaFotoEmByte);
            fotoEmBytes = streamDaFotoEmByte.toByteArray();
            String fotoEmString = Base64.encodeToString(fotoEmBytes, Base64.DEFAULT);
            txtFoto.setText(fotoEmString);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //
}