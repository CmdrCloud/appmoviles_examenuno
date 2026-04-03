package rgonzales.app.labinventario.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import rgonzales.app.labinventario.R;
import rgonzales.app.labinventario.model.CartItem;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {
    private List<CartItem> items;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onQuantityChanged();
        void onItemRemoved(int position);
        void onUpdateQuantity(int idProducto, int nuevaCantidad);
    }

    public CarritoAdapter(List<CartItem> items, OnCartChangeListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvNombre.setText(item.getProducto().getNombre());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "%.2f Bs.", item.getProducto().getPrecio()));
        holder.tvCantidad.setText(String.format(Locale.getDefault(), "%dx", item.getCantidad()));

        holder.btnMas.setOnClickListener(v -> {
            int nuevaCantidad = item.getCantidad() + 1;
            item.setCantidad(nuevaCantidad);
            notifyItemChanged(position);
            listener.onUpdateQuantity(item.getProducto().getId(), nuevaCantidad);
            listener.onQuantityChanged();
        });

        holder.btnMenos.setOnClickListener(v -> {
            if (item.getCantidad() > 1) {
                int nuevaCantidad = item.getCantidad() - 1;
                item.setCantidad(nuevaCantidad);
                notifyItemChanged(position);
                listener.onUpdateQuantity(item.getProducto().getId(), nuevaCantidad);
                listener.onQuantityChanged();
            } else {
                int idProd = item.getProducto().getId();
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                listener.onUpdateQuantity(idProd, 0); // 0 triggers removal in DAO
                listener.onItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvCantidad;
        Button btnMas, btnMenos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCarrito);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCarrito);
            tvCantidad = itemView.findViewById(R.id.tvCantidadCarrito);
            btnMas = itemView.findViewById(R.id.btnMas);
            btnMenos = itemView.findViewById(R.id.btnMenos);
        }
    }
}
