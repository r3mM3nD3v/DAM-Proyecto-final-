package com.example.appservicioscomunitarios.ui;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.appservicioscomunitarios.R;
import com.example.appservicioscomunitarios.data.Servicio;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private List<Servicio> servicioList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Servicio servicio);
        void onDeleteClick(Servicio servicio);
    }

    public ServicioAdapter(List<Servicio> servicioList, Context context, OnItemClickListener listener) {
        this.servicioList = servicioList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicioList.get(position);
        holder.txtTitulo.setText(servicio.getTitulo());
        holder.txtDescripcion.setText(servicio.getDescripcion());

        if (servicio.getImagenUri() != null) {
            holder.imageViewServicio.setImageURI(Uri.parse(servicio.getImagenUri()));
        } else {
            holder.imageViewServicio.setImageResource(R.drawable.placeholder_image);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(servicio);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(servicio);
        });
    }

    @Override
    public int getItemCount() {
        return servicioList.size();
    }

    public static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion;
        ImageView imageViewServicio;
        ImageButton btnEliminar;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloServicio);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionServicio);
            imageViewServicio = itemView.findViewById(R.id.imageViewServicio);
            btnEliminar = itemView.findViewById(R.id.btnEliminarServicio);
        }
    }
}
