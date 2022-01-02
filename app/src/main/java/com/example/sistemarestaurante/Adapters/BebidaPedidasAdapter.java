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
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BebidaPedidasAdapter extends RecyclerView.Adapter<BebidaPedidasAdapter.MyViewHolderBebidas> {

    private List<Bebida> bebidaList;
    private List<BebidaPedida> bebidaPedidasquantidade = new ArrayList<>();
    private BebidaPedida bebidaPedida;
    private Context context;

    public BebidaPedidasAdapter(List<Bebida> listbebidas, Context  c) {
        this.bebidaList = listbebidas;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolderBebidas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_prato_pedidos,parent,false);
        return new MyViewHolderBebidas(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderBebidas holder, @SuppressLint("RecyclerView") int position) {
            Bebida bebida = bebidaList.get(position);
            bebidaPedida = new BebidaPedida();
            bebidaPedida.setBebida(bebida);
            bebidaPedida.setQuantidade(0);
            bebidaPedidasquantidade.add(bebidaPedida);
            holder.textNomeBebida.setText(bebida.getNomeBebida());
            holder.textValorBebida.setText("R$ "+bebida.getValor());
            holder.textQuantidade.setText(String.valueOf(0));


            if (bebida.getFoto() != null) {
                Glide.with(context).load(bebida.getFoto()).into(holder.circleImageBebida);
            }

        holder.buttonRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantidade = Integer.parseInt(holder.textQuantidade.getText().toString());
                int novaquantidade = quantidade - 1;
                if (quantidade >= 0) {
                    holder.editTextObs.setVisibility(View.VISIBLE);
                    holder.textInputLayoutout.setVisibility(View.VISIBLE);
                    BebidaPedida BebidaPedido1 = bebidaPedidasquantidade.get(position);
                    BebidaPedido1.setQuantidade(novaquantidade);
                    holder.textQuantidade.setText(String.valueOf(novaquantidade));
                }
                if (quantidade == 0 || novaquantidade == 0) {
                    holder.editTextObs.setVisibility(View.GONE);
                    holder.textInputLayoutout.setVisibility(View.GONE);

                    BebidaPedida bebidaPedida = bebidaPedidasquantidade.get(position);
                    bebidaPedida.setObs(null);
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
                    BebidaPedida BebidaPedido1 = bebidaPedidasquantidade.get(position);
                    BebidaPedido1.setQuantidade(novaquantidade);
                    //pratosequantidades.add(pratoPedido1);
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
                            BebidaPedida bebidaPedida = bebidaPedidasquantidade.get(position);
                            if (!textObs.isEmpty()){
                                bebidaPedida.setObs(textObs);
                            }
                            else {
                                bebidaPedida.setObs(null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return bebidaList.size();
    }

    public class MyViewHolderBebidas extends RecyclerView.ViewHolder{
        TextView textQuantidade, textNomeBebida, textValorBebida;
        CircleImageView circleImageBebida;
        ImageButton buttonAdicionar,buttonRemover;
        TextInputEditText editTextObs;
        TextInputLayout textInputLayoutout;

        public MyViewHolderBebidas(@NonNull View itemView) {
            super(itemView);
            textQuantidade = itemView.findViewById(R.id.textQuantidadePrato);
            textNomeBebida = itemView.findViewById(R.id.textNomePrato);
            textValorBebida = itemView.findViewById(R.id.textValorPrato);
            circleImageBebida = itemView.findViewById(R.id.circleImagePrato);
            buttonAdicionar = itemView.findViewById(R.id.imageButtonAdicionar);
            buttonRemover = itemView.findViewById(R.id.imageButtonRemover);
            editTextObs = itemView.findViewById(R.id.textinputObs);
            textInputLayoutout = itemView.findViewById(R.id.textInputLayoutOut);
        }
    }

    public List<BebidaPedida> getBebidas () {
        return bebidaPedidasquantidade;
    }
}
