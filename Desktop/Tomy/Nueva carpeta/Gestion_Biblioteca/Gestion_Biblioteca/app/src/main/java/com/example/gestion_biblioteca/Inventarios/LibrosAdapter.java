package com.example.gestion_biblioteca.Inventarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestion_biblioteca.R;
import java.util.List;

public class LibrosAdapter extends RecyclerView.Adapter<LibrosAdapter.LibroViewHolder> {
    private List<Libros> libros;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Libros libro);
    }


    public LibrosAdapter(List<Libros> libros, OnItemClickListener listener) {
        this.libros = libros;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventarios, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libros libro = libros.get(position);
        holder.tvNombre.setText(libro.getNombre());
        holder.tvCategoria.setText(libro.getCategoria());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(libro));
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class LibroViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;
        TextView tvCategoria;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
        }
    }
}