package com.example.biolap.modelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biolap.R;

import java.util.List;

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder> {
    private List<PacienteLista> pacienteList;
    final PacienteAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick (PacienteLista item);
    }

    public PacienteAdapter(List<PacienteLista> pacienteList, PacienteAdapter.OnItemClickListener listener) {
        this.pacienteList = pacienteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PacienteAdapter.PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paciente_card, parent, false);
        return new PacienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteAdapter.PacienteViewHolder holder, int position) {
        PacienteLista paciente = pacienteList.get(position);
        holder.idPTextView.setText(paciente.getIdP());
        holder.nombrePTextView.setText(paciente.getNombreP());
        holder.dniTextView.setText(paciente.getDni());
        holder.fechaTextView.setText(paciente.getFecha());
        holder.bind(paciente, listener);
    }

    @Override
    public int getItemCount() {
        return pacienteList.size();
    }

    public class PacienteViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePTextView;
        TextView dniTextView;
        TextView idPTextView;
        TextView fechaTextView;

        public PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePTextView = itemView.findViewById(R.id.nombrePaciente);
            dniTextView = itemView.findViewById(R.id.dniPaciente);
            idPTextView = itemView.findViewById(R.id.idPaciente);
            fechaTextView = itemView.findViewById(R.id.fechaCarga);
        }
        public void bind(final PacienteLista item, final PacienteAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(item);
                }
            });
        }
    }
}
