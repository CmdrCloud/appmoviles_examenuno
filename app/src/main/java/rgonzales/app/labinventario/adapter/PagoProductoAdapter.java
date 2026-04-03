package rgonzales.app.labinventario.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import rgonzales.app.labinventario.R;
import rgonzales.app.labinventario.model.CartItem;

public class PagoProductoAdapter extends RecyclerView.Adapter<PagoProductoAdapter.ViewHolder> {
    private List<CartItem> items;

    public PagoProductoAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvNombre.setText(item.getProducto().getNombre());
        holder.tvCantidad.setText(String.format(Locale.getDefault(), "x%d", item.getCantidad()));
        double subtotal = item.getProducto().getPrecio() * item.getCantidad();
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "%.2f Bs.", subtotal));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePago);
            tvCantidad = itemView.findViewById(R.id.tvCantidadPago);
            tvPrecio = itemView.findViewById(R.id.tvPrecioPago);
        }
    }
}
