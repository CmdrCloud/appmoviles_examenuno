package rgonzales.app.labinventario.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rgonzales.app.labinventario.R;
import rgonzales.app.labinventario.model.Producto;

public class MenuProductoAdapter extends RecyclerView.Adapter<MenuProductoAdapter.ViewHolder> {
    private List<Producto> productos;
    private Map<Integer, Integer> cantidadesSeleccionadas;

    public MenuProductoAdapter(List<Producto> productos) {
        this.productos = productos;
        this.cantidadesSeleccionadas = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto p = productos.get(position);
        holder.tvNombre.setText(p.getNombre());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "%.2f Bs.", p.getPrecio()));

        int cant = cantidadesSeleccionadas.getOrDefault(p.getId(), 0);
        holder.tvCantidad.setText(String.valueOf(cant));

        holder.btnMas.setOnClickListener(v -> {
            int current = cantidadesSeleccionadas.getOrDefault(p.getId(), 0);
            if (current < p.getCantidad()) {
                cantidadesSeleccionadas.put(p.getId(), current + 1);
                notifyItemChanged(position);
            }
        });

        holder.btnMenos.setOnClickListener(v -> {
            int current = cantidadesSeleccionadas.getOrDefault(p.getId(), 0);
            if (current > 0) {
                cantidadesSeleccionadas.put(p.getId(), current - 1);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public Map<Integer, Integer> getCantidadesSeleccionadas() {
        return cantidadesSeleccionadas;
    }

    public void clearSelections() {
        cantidadesSeleccionadas.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvCantidad;
        Button btnMas, btnMenos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreMenuProd);
            tvPrecio = itemView.findViewById(R.id.tvPrecioMenuProd);
            tvCantidad = itemView.findViewById(R.id.tvCantidadMenu);
            btnMas = itemView.findViewById(R.id.btnMasMenu);
            btnMenos = itemView.findViewById(R.id.btnMenosMenu);
        }
    }
}
