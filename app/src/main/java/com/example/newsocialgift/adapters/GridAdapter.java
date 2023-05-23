package com.example.newsocialgift.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.GridItem;
import com.example.newsocialgift.R;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<GridItem> items;
    private OnItemClickListener listener; // Agregado: Listener de clic

    public GridAdapter(List<GridItem> items) {
        this.items = items;
    }

    // Agregado: Setter para el listener de clic
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GridItem item = items.get(position);
        holder.textView.setText(item.getText());
        holder.container.setBackgroundColor(item.getColor());

        // Agregado: Configuración del clic en el ítem
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            container = itemView.findViewById(R.id.container);
        }
    }

    // Agregado: Interfaz personalizada para el listener de clic
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
