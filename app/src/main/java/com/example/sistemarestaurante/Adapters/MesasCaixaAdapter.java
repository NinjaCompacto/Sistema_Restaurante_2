package com.example.sistemarestaurante.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sistemarestaurante.Activitys.HistoricoPedidosCaixaActivity;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.R;

import org.w3c.dom.Text;

import java.util.List;

public class MesasCaixaAdapter extends RecyclerView.Adapter<MesasCaixaAdapter.MyViewHolder> {

    private Context context;
    private List<Mesa> listMesas;

    public MesasCaixaAdapter(List<Mesa> mesas, Context c) {
        this.context = c;
        this.listMesas = mesas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesas_caixa_adapter,parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mesa mesa = listMesas.get(position);
        holder.textNumeroMesa .setText(mesa.getNumeroMesa());
        holder.textNomeClienteCaixa.setText("Cliente: " + mesa.getNomeCliente());

        //seta ação button para abrir historico da mesa
         holder.buttonHistorico.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(context, HistoricoPedidosCaixaActivity.class);
                 i.putExtra("mesa", mesa);
                 view.getContext().startActivity(i);
             }
         });


    }

    @Override
    public int getItemCount() {
        return listMesas.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {
        private Button buttonFecharConta,buttonHistorico;
        private TextView textNumeroMesa,textNomeClienteCaixa;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonFecharConta = itemView.findViewById(R.id.buttonFecharConta);
            buttonHistorico = itemView.findViewById(R.id.buttonHistoricoCaixa);
            textNumeroMesa = itemView.findViewById(R.id.textNumeroMesaCaixa);
            textNomeClienteCaixa = itemView.findViewById(R.id.textNomeClienteCaixa);
        }
    }

}