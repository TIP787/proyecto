package com.example.gestion_biblioteca.Socios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestion_biblioteca.R;

import java.util.List;

public class SocioAdapter extends RecyclerView.Adapter<SocioAdapter.SocioViewHolder> {
    private List<Socios> socios;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Socios socio);
    }

    public SocioAdapter(List<Socios> socios, OnItemClickListener listener) {
        this.socios = socios;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public SocioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_socios, parent, false);
        return new SocioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocioViewHolder holder, int position) {
        Socios socio = socios.get(position);
        holder.txtNombre.setText(socio.getNombre());
        holder.txtDni.setText(socio.getDni());
        holder.txtTelefono.setText(socio.getTelefono());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(socio));
    }

    @Override
    public int getItemCount() {
        return socios.size();
    }

    public static class SocioViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDni, txtTelefono;

        public SocioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDni = itemView.findViewById(R.id.txtDni);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
        }
    }
}
