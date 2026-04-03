package rgonzales.app.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import rgonzales.app.labinventario.adapter.CarritoAdapter;
import rgonzales.app.labinventario.model.CartManager;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tvSubtotal, tvTotal;
    private Button btnPagar, btnVolver;
    private CartManager cartManager;
    private CarritoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        cartManager = CartManager.getInstance();

        rv = findViewById(R.id.rvCarrito);
        tvSubtotal = findViewById(R.id.tvSubtotalCarrito);
        tvTotal = findViewById(R.id.tvTotalCarrito);
        btnPagar = findViewById(R.id.btnIrAPagar);
        btnVolver = findViewById(R.id.btnVolverCarrito);

        rv.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new CarritoAdapter(cartManager.getCartItems(), new CarritoAdapter.OnCartChangeListener() {
            @Override
            public void onQuantityChanged() {
                actualizarTotales();
            }
        });
        rv.setAdapter(adapter);

        btnVolver.setOnClickListener(v -> finish());

        btnPagar.setOnClickListener(v -> {
            if (cartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CarritoActivity.this, PagoActivity.class);
                startActivity(intent);
            }
        });

        actualizarTotales();

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Usamos el header personalizado del layout
        }
    }

    private void actualizarTotales() {
        double subtotal = cartManager.getSubtotal();
        tvSubtotal.setText(String.format(Locale.getDefault(), "%.2f Bs.", subtotal));
        tvTotal.setText(String.format(Locale.getDefault(), "%.2f Bs.", subtotal));
    }
}
