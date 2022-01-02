package com.example.sistemarestaurante.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PratoPedidosAdapter extends RecyclerView.Adapter<PratoPedidosAdapter.MyViewHolderPratos> {

    private List<Prato> pratos;
    private List<PratoPedido> pratosequantidades = new ArrayList<>();
    private Context context;
    private PratoPedido pratoPedido;

    public PratoPedidosAdapter(List<Prato> pratoslist, Context c) {
        this.pratos = pratoslist;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolderPratos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemlista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_prato_pedidos,parent,false);
        return new MyViewHolderPratos(itemlista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderPratos holder, @SuppressLint("RecyclerView") int position) {
        Prato prato = pratos.get(position);
        pratoPedido = new PratoPedido();
        pratoPedido.setPrato(prato);
        pratoPedido.setQuantidade(0);
        pratosequantidades.add(pratoPedido);
        holder.textNomePrato.setText(prato.getNomePrato());
        holder.textValorPrato.setText("R$ " + prato.getValor());
        holder.textQuantidade.setText(String.valueOf(0));

        holder.buttonRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int quantidade = Integer.parseInt(holder.textQuantidade.getText().toString());
                    int novaquantidade = quantidade - 1;
                    if (quantidade >= 0) {
                        PratoPedido pratoPedido1 = pratosequantidades.get(position);
                        pratoPedido1.setQuantidade(novaquantidade);
                        holder.textQuantidade.setText(String.valueOf(novaquantidade));
                    }
                if (quantidade == 0 || novaquantidade == 0) {
                    holder.editTextObs.setVisibility(View.GONE);
                    holder.textInputLayoutout.setVisibility(View.GONE);

                    PratoPedido pratoPedido1 = pratosequantidades.get(position);
                    pratoPedido1.setObs(null);
                }

            }
        });

        holder.buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantidade = Integer.parseInt(holder.textQuantidade.getText().toString());
                int novaquantidade = quantidade + 1;
                holder.textQuantidade.setText(String.valueOf(novaquantidade));
                if (quantidade >= 0){
                    PratoPedido pratoPedido1 = pratosequantidades.get(position);
                    pratoPedido1.setQuantidade(novaquantidade);
                }
                if (novaquantidade > 0){
                    holder.editTextObs.setVisibility(View.VISIBLE);
                    holder.textInputLayoutout.setVisibility(View.VISIBLE);

                    holder.editTextObs.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String textObs = charSequence.toString();
                            PratoPedido pratoPedido1 = pratosequantidades.get(position);
                            if (!textObs.isEmpty()) {
                                pratoPedido1.setObs(textObs);

                            }
                            else {
                                pratoPedido1.setObs(null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
            }
        });

        if (prato.getFoto() != null){
            Glide.with(context).load(prato.getFoto()).into(holder.circleImagePrato);
        }

    }

    @Override
    public int getItemCount() {
        return pratos.size();
    }

    public class MyViewHolderPratos extends RecyclerView.ViewHolder{
        TextView textQuantidade,textNomePrato,textValorPrato;
        CircleImageView circleImagePrato;
        ImageButton buttonAdicionar,buttonRemover;
        TextInputEditText editTextObs;
        TextInputLayout textInputLayoutout;

        public MyViewHolderPratos(@NonNull View itemView) {
            super(itemView);
            textQuantidade = itemView.findViewById(R.id.textQuantidadePrato);
            textNomePrato = itemView.findViewById(R.id.textNomePrato);
            textValorPrato = itemView.findViewById(R.id.textValorPrato);
            circleImagePrato = itemView.findViewById(R.id.circleImagePrato);
            buttonAdicionar = itemView.findViewById(R.id.imageButtonAdicionar);
            buttonRemover = itemView.findViewById(R.id.imageButtonRemover);
            editTextObs = itemView.findViewById(R.id.textinputObs);
            textInputLayoutout = itemView.findViewById(R.id.textInputLayoutOut);
        }
    }

    public List<PratoPedido> getpedidos () {

        return  pratosequantidades;

    }
}