package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemarestaurante.Adapters.HistoricoPedidosCaixaAdapater;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

public class FecharContaCaixaActivity extends AppCompatActivity {

    private Mesa mesaSelecionada;

    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();

    //XML
    private Button buttonFecharConta;
    private TextView textValorTotal;
    private RecyclerView recyclerListaPedidosTotal;
    private HistoricoPedidosCaixaAdapater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechar_conta_caixa);

        getExtras();
        setElementsXML();
        if (mesaSelecionada.getPedidos() != null) {
            configuraRecycler();
        }
        configuraButton();
        configuraTextValorTotal();

    }

    private void getExtras () {
        if (getIntent().getExtras() != null){
            mesaSelecionada = (Mesa) getIntent().getExtras().getSerializable("mesa");
        }
    }

    private void setElementsXML () {
        buttonFecharConta = findViewById(R.id.buttonFecharConta);
        textValorTotal = findViewById(R.id.textValorTotal);
        recyclerListaPedidosTotal = findViewById(R.id.recyclerListaPedidosTotal);
    }

    private void  configuraRecycler(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new HistoricoPedidosCaixaAdapater(mesaSelecionada.getPedidos(),getApplicationContext());
        recyclerListaPedidosTotal.setHasFixedSize(true);
        recyclerListaPedidosTotal.setLayoutManager(layoutManager);
        recyclerListaPedidosTotal.setAdapter(adapter);
    }

    private void configuraTextValorTotal (){
        DecimalFormat df = new DecimalFormat("#####.00");
        if (mesaSelecionada.getPedidos() != null ) {
            float valorTotal = calculaValorTotal();
            textValorTotal.setText("R$ " + df.format(valorTotal));
        }
        else{
            textValorTotal.setText("R$ 00.00");
        }
    }

    private float calculaValorTotal (){
        float valorTotal = 0;
        List<Pedido> listpedidos = mesaSelecionada.getPedidos();

            for (Pedido pedido : listpedidos) {

                List<PratoPedido> listPratosPedidos = pedido.getComida();
                List<BebidaPedida> listBebidasPedidas = pedido.getBebida();

                if (listPratosPedidos != null) {
                    for (PratoPedido pratoPedido : listPratosPedidos) {
                        int quantidade = pratoPedido.getQuantidade();
                        float valorIndividual = Float.parseFloat(pratoPedido.getPrato().getValor());
                        valorTotal = valorTotal + (quantidade * valorIndividual);
                    }
                }

                if (listBebidasPedidas != null) {
                    for (BebidaPedida bebidaPedida : listBebidasPedidas) {
                        int quantidade = bebidaPedida.getQuantidade();
                        float valorIndividual = Float.parseFloat(bebidaPedida.getBebida().getValor());
                        valorTotal = valorTotal + (quantidade * valorIndividual);
                    }
                }
            }


        return  valorTotal;
    }

    private void configuraButton () {
        buttonFecharConta.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (mesaSelecionada.getPedidos() != null) {
                    Toast.makeText(getApplicationContext(), "Conta Fechada", Toast.LENGTH_SHORT).show();
                    gerarPdf();
                    mesaSelecionada.resetMesa();
                    mesaSelecionada.salvarmesa();
                    Toast.makeText(getApplicationContext(), "Mesa Limpa", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Não ha pedidos ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void gerarPdf () {
        PdfDocument documentoPDF = new PdfDocument();

        PdfDocument.PageInfo detalhesDaPagina = new PdfDocument.PageInfo.Builder(944, 1300, 1).create();

        PdfDocument.Page novaPagina = documentoPDF.startPage(detalhesDaPagina);

        Canvas canvas = novaPagina.getCanvas();
        Paint shapeText = new Paint();
        shapeText.setColor(Color.BLACK);
        shapeText.setTextSize(20);

        List<Pedido> listPedidos = mesaSelecionada.getPedidos();
        List<Prato> listPratos = new ArrayList<>();
        List<Bebida> listBebidas = new ArrayList<>();


        for (Pedido pedido : listPedidos) {
            if (pedido.getComida() != null) {
                for (PratoPedido pratoPedido : pedido.getComida()) {
                    listPratos.add(pratoPedido.getPrato());
                }
            }
            if (pedido.getBebida() != null) {
                for (BebidaPedida bebidaPedida : pedido.getBebida()) {
                    listBebidas.add(bebidaPedida.getBebida());
                }
            }
        }

        canvas.drawText("PRATOS:", 425, 100, shapeText);
        int y = 150;

        if (listPratos != null) {
            for (Prato prato : listPratos) {
                canvas.drawText(prato.getNomePrato(), 250, y, shapeText);
                gerarPontosPDF(250, 600, y, canvas, shapeText);
                canvas.drawText("R$" + prato.getValor(), 600, y, shapeText);
                y += 50;
            }
        }
        int position = y + 50;
        canvas.drawText("BEBIDAS:", 425, position, shapeText);
        y += 100;
        if (listBebidas != null) {
            for (Bebida bebida : listBebidas) {
                canvas.drawText(bebida.getNomeBebida(), 250, y, shapeText);
                gerarPontosPDF(250, 600, y, canvas, shapeText);
                canvas.drawText("R$" + bebida.getValor(), 600, y, shapeText);
                y += 50;
            }
        }

        float valorConta = calculaValorTotal();
        DecimalFormat df = new DecimalFormat("#####.00");

        canvas.drawText("TOTAL", 250, y+50, shapeText);
        gerarPontosPDF(250, 600, y+50, canvas, shapeText);
        canvas.drawText("R$" + df.format(valorConta), 600, y+50, shapeText);

        documentoPDF.finishPage(novaPagina);

        String dateTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());

        String targetPdf = "/storage/self/primary/Documents/PDF/" + dateTime + ".pdf";

        File filePath = new File(targetPdf);
        try {
            documentoPDF.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getApplicationContext(), "PDF GERADO !!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "FALHA AO GERAR PDF !!", Toast.LENGTH_SHORT).show();
        }
        documentoPDF.close();

    }

    private void gerarPontosPDF (int x1, int x2,int y,Canvas canvas,Paint shape){
        int x ;
        for (x = x1 + 25; x < x2 ; x = x+50){
            canvas.drawText(".",x,y,shape);
        }
    }

}