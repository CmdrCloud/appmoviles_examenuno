package rgonzales.app.labinventario.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rgonzales.app.labinventario.R;
import rgonzales.app.labinventario.model.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> listaProductos;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,parent,false);
        return new ProductoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto p = listaProductos.get(position);
        holder.tvNombreItemProd.setText(p.getNombre());
        holder.tvPrecioItemProd.setText(String.valueOf(p.getPrecio()));
        holder.tvCantidadItemProd.setText(String.valueOf(p.getCantidad()));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(p));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(p,v);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Producto p);
        void onItemLongClick(Producto p, View v);
    }
    public ProductoAdapter(List<Producto> listaProductos, OnItemClickListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }




    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreItemProd;
        TextView tvPrecioItemProd;
        TextView tvCantidadItemProd;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreItemProd = itemView.findViewById(R.id.tvNombreItemProd);
            tvPrecioItemProd = itemView.findViewById(R.id.tvPrecioItemProd);
            tvCantidadItemProd = itemView.findViewById(R.id.tvCantidadItemProd);
        }
    }
}
