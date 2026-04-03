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

import rgonzales.app.labinventario.adapter.CarritoAdapter;
import rgonzales.app.labinventario.dao.CarritoDAO;
import rgonzales.app.labinventario.model.CartItem;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tvSubtotal, tvTotal;
    private Button btnPagar, btnVolver;
    private CarritoDAO carritoDao;
    private CarritoAdapter adapter;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        carritoDao = new CarritoDAO(this);

        rv = findViewById(R.id.rvCarrito);
        tvSubtotal = findViewById(R.id.tvSubtotalCarrito);
        tvTotal = findViewById(R.id.tvTotalCarrito);
        btnPagar = findViewById(R.id.btnIrAPagar);
        btnVolver = findViewById(R.id.btnVolverCarrito);

        rv.setLayoutManager(new LinearLayoutManager(this));

        btnVolver.setOnClickListener(v -> finish());

        btnPagar.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CarritoActivity.this, PagoActivity.class);
                startActivity(intent);
            }
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCarrito();
    }

    private void cargarCarrito() {
        carritoDao.open();
        cartItems = carritoDao.listarCarrito();
        carritoDao.close();

        adapter = new CarritoAdapter(cartItems, new CarritoAdapter.OnCartChangeListener() {
            @Override
            public void onQuantityChanged() {
                actualizarTotales();
            }

            @Override
            public void onItemRemoved(int position) {
                // El adapter ya maneja la eliminación visual y de la lista interna
                actualizarTotales();
            }

            @Override
            public void onUpdateQuantity(int idProducto, int nuevaCantidad) {
                carritoDao.open();
                carritoDao.actualizarCantidad(idProducto, nuevaCantidad);
                carritoDao.close();
            }
        });
        rv.setAdapter(adapter);
        actualizarTotales();
    }

    private void actualizarTotales() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
        tvSubtotal.setText(String.format(Locale.getDefault(), "%.2f Bs.", subtotal));
        tvTotal.setText(String.format(Locale.getDefault(), "%.2f Bs.", subtotal));
    }
}
