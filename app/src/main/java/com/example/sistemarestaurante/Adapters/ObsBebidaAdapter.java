package com.example.sistemarestaurante.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ObsBebidaAdapter extends RecyclerView.Adapter<ObsBebidaAdapter.MyViewHolderObsBebida> {

    private List<BebidaPedida> bebidaPedidas;
    private List<BebidaPedida> bebidaPedidascomObs = new ArrayList<>();
    private Context context;

    public ObsBebidaAdapter(List<BebidaPedida> bebidas , Context c) {
        this.bebidaPedidas = bebidas;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolderObsBebida onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_obs_prato,parent,false);
        return new MyViewHolderObsBebida(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderObsBebida holder, @SuppressLint("RecyclerView") int position) {

        BebidaPedida bebida = bebidaPedidas.get(position);
        bebidaPedidascomObs.add(bebida);
        holder.textNomeBebidaObs.setText(bebida.getBebida().getNomeBebida());
        holder.textValorBebidaObs.setText(bebida.getBebida().getValor());

        if (bebida.getBebida().getFoto() != null){
            Glide.with(context).load(bebida.getBebida().getFoto()).into(holder.circleImageBebidaObs);
        }

        holder.buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BebidaPedida bebida1 = bebidaPedidascomObs.get(position);
                bebida1.setObs(holder.editTextObsBebida.getText().toString());
                Toast.makeText(context,"Observação Adicionada !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bebidaPedidas.size();
    }

    public class MyViewHolderObsBebida extends RecyclerView.ViewHolder {
        CircleImageView circleImageBebidaObs;
        TextView textNomeBebidaObs, textValorBebidaObs;
        EditText editTextObsBebida;
        Button buttonAdicionar;
        public MyViewHolderObsBebida(@NonNull View itemView) {
            super(itemView);
            circleImageBebidaObs = itemView.findViewById(R.id.circleImagePratoObs);
            textNomeBebidaObs = itemView.findViewById(R.id.textNomepratoObs);
            textValorBebidaObs = itemView.findViewById(R.id.textValorPratoObs);
            editTextObsBebida = itemView.findViewById(R.id.editTextObsPrato);
            buttonAdicionar = itemView.findViewById(R.id.buttonAdicionarObs);
        }
    }

    public List<BebidaPedida> getBebidaPedidascomObs() {
        return bebidaPedidascomObs;
    }
}
