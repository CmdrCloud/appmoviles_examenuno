package rgonzales.app.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import rgonzales.app.labinventario.adapter.PagoProductoAdapter;
import rgonzales.app.labinventario.dao.CarritoDAO;
import rgonzales.app.labinventario.dao.VentaDAO;
import rgonzales.app.labinventario.model.CartItem;

public class PagoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tvSubtotal, tvTarifa, tvTotal;
    private Button btnPagar, btnVolver;
    private CarritoDAO carritoDao;
    private VentaDAO ventaDao;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        rv = findViewById(R.id.rvProductosPago);
        tvSubtotal = findViewById(R.id.tvSubtotalPago);
        tvTarifa = findViewById(R.id.tvTarifaPago);
        tvTotal = findViewById(R.id.tvTotalPago);
        btnPagar = findViewById(R.id.btnConfirmarPago);
        btnVolver = findViewById(R.id.btnVolverPago);

        carritoDao = new CarritoDAO(this);
        ventaDao = new VentaDAO(this);

        rv.setLayoutManager(new LinearLayoutManager(this));

        btnVolver.setOnClickListener(v -> finish());

        cargarDatos();

        btnPagar.setOnClickListener(v -> {
            procesarVenta();
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void cargarDatos() {
        carritoDao.open();
        cartItems = carritoDao.listarCarrito();
        carritoDao.close();

        PagoProductoAdapter adapter = new PagoProductoAdapter(cartItems);
        rv.setAdapter(adapter);

        double subtotal = 0;
        int totalProductos = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
            totalProductos += item.getCantidad();
        }

        double tarifa = totalProductos * 2.0; // 2 Bs. por producto
        double total = subtotal + tarifa;

        tvSubtotal.setText(String.format(Locale.getDefault(), "%.2f Bs.", subtotal));
        tvTarifa.setText(String.format(Locale.getDefault(), "%.2f Bs.", tarifa));
        tvTotal.setText(String.format(Locale.getDefault(), "%.2f Bs.", total));
    }

    private void procesarVenta() {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "No hay productos para pagar", Toast.LENGTH_SHORT).show();
            return;
        }

        double subtotal = 0;
        int totalProductos = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
            totalProductos += item.getCantidad();
        }
        double tarifa = totalProductos * 2.0;
        double total = subtotal + tarifa;

        ventaDao.open();
        ventaDao.registrarVenta(subtotal, tarifa, total, cartItems);
        ventaDao.close();

        carritoDao.open();
        carritoDao.vaciarCarrito();
        carritoDao.close();

        Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_LONG).show();
        
        Intent intent = new Intent(this, MenuProductosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
