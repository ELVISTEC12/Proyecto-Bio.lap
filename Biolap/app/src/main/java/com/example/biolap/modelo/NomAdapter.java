package com.example.biolap.modelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biolap.R;

import java.util.List;

public class NomAdapter extends RecyclerView.Adapter<NomAdapter.NomenclaturaViewHolder> {
    private List<NomLista> nomenclaturaList;
    final NomAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick (NomLista item);
    }

    public NomAdapter(List<NomLista> nomenclaturaList, NomAdapter.OnItemClickListener listener) {
        this.nomenclaturaList = nomenclaturaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NomenclaturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nom_card, parent, false);
        return new NomenclaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NomenclaturaViewHolder holder, int position) {
        NomLista nomenclatura = nomenclaturaList.get(position);
        holder.idTextView.setText(nomenclatura.getId());
        holder.nombreTextView.setText(nomenclatura.getNombre());
        holder.codigoTextView.setText(nomenclatura.getCodigo());
        holder.bind(nomenclatura, listener);
    }

    @Override
    public int getItemCount() {
        return nomenclaturaList.size();
    }

    public class NomenclaturaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView codigoTextView;
        TextView idTextView;

        public NomenclaturaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.NombreNomenclatura);
            codigoTextView = itemView.findViewById(R.id.codigoNomenclatura);
            idTextView = itemView.findViewById(R.id.idNomenclatura);
        }
        public void bind(final NomLista item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(item);
                }
            });
        }
    }
}

