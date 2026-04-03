package rgonzales.app.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tvTotal;
    private Button btnPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        rv = findViewById(R.id.rvCarrito);
        tvTotal = findViewById(R.id.tvTotalCarrito);
        btnPagar = findViewById(R.id.btnIrAPagar);

        rv.setLayoutManager(new LinearLayoutManager(this));
        
        // Aquí se cargaría la lista de productos seleccionados. 
        // Para este ejemplo, solo mostramos el botón funcional.
        
        btnPagar.setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, PagoActivity.class);
            startActivity(intent);
        });
        
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Carrito de Compras");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
