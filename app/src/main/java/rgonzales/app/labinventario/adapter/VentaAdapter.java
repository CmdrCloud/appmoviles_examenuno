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
import rgonzales.app.labinventario.dao.VentaDAO;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {
    private List<VentaDAO.VentaInfo> ventas;

    public VentaAdapter(List<VentaDAO.VentaInfo> ventas) {
        this.ventas = ventas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VentaDAO.VentaInfo v = ventas.get(position);
        holder.tvId.setText("ID Venta: #" + v.id);
        holder.tvFecha.setText("Fecha: " + v.fecha);
        holder.tvTotal.setText(String.format(Locale.getDefault(), "%.2f Bs.", v.total));
    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvFecha, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdVenta);
            tvFecha = itemView.findViewById(R.id.tvFechaVenta);
            tvTotal = itemView.findViewById(R.id.tvTotalVentaItem);
        }
    }
}
