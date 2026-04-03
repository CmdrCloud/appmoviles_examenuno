package rgonzales.app.labinventario;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rgonzales.app.labinventario.adapter.ProductoAdapter;
import rgonzales.app.labinventario.dao.ProductoDAO;
import rgonzales.app.labinventario.model.Producto;

public class FindProduct extends AppCompatActivity {
    EditText etSearch;
    Button btnSearch;
    RecyclerView rvResult;
    ProductoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        rvResult = findViewById(R.id.rvResult);
        rvResult.setLayoutManager(new LinearLayoutManager((this)));
        dao = new ProductoDAO(this);

        btnSearch.setOnClickListener(v -> searchProducts());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Product Search");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void searchProducts() {

        String criteria = etSearch.getText().toString().trim();
        if (criteria.isEmpty()) {
            return;
        }

        dao.open();
        List<Producto> list = dao.buscarPorNombre(criteria);
        dao.close();

        ProductoAdapter adapter = new ProductoAdapter(list, null);
        rvResult.setAdapter(adapter);

    }


}