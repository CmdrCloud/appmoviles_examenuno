package rgonzales.app.labinventario.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rgonzales.app.labinventario.R;
import rgonzales.app.labinventario.model.Categoria;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {
    private List<Categoria> listaCategorias;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Categoria c);
        void onItemLongClick(Categoria c,View v);
    }
    public CategoriaAdapter(List<Categoria> lista, OnItemClickListener listener)
    {
        this.listaCategorias = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // se realiza el inflate del xml para cada elemento de lista
        View v = View.inflate(parent.getContext(), R.layout.item_categoria,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria c = listaCategorias.get(position);
        holder.tvNombre.setText(c.getNombre());
        holder.tvTotal.setText(String.valueOf(c.getTotalProductos()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(c));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(c,v);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre,tvTotal;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCategoria);
            tvTotal = itemView.findViewById(R.id.tvTotalProductos);
        }
    }
}
