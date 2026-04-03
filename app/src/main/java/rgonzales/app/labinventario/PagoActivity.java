package rgonzales.app.labinventario;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PagoActivity extends AppCompatActivity {

    private Button btnPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        btnPagar = findViewById(R.id.btnPagar);

        btnPagar.setOnClickListener(v -> {
            // Simulación de proceso de pago
            Toast.makeText(this, "Pago procesado con éxito. ¡Gracias por su compra!", Toast.LENGTH_LONG).show();
            // Regresar al menú principal o cerrar el flujo de compra
            finishAffinity(); // O redirigir al login/menu
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Finalizar Pago");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
