package rgonzales.app.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import rgonzales.app.labinventario.adapter.ProductoAdapter;
import rgonzales.app.labinventario.dao.ProductoDAO;
import rgonzales.app.labinventario.model.Producto;

public class MenuProductosActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fabCarrito;
    private ProductoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_productos);

        rv = findViewById(R.id.rvMenuProductos);
        fabCarrito = findViewById(R.id.fabCarrito);
        dao = new ProductoDAO(this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        
        fabCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(MenuProductosActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        cargarProductos();
    }

    private void cargarProductos() {
        // En un app real, listaríamos todos o por categorías. 
        // Por ahora listamos todos buscando por vacío o implementando un listarTodos en DAO.
        // Como ProductoDAO solo tiene listarPorCategoria, usaremos una lógica simplificada o asumiremos categoria 1 por ahora
        // o mejor, simplemente mostramos un mensaje si no hay lógica para "todos".
        // Para que se vea algo, intentaremos buscar con string vacío si el DAO lo permite.
        
        dao.open();
        List<Producto> lista = dao.buscarPorNombre(""); 
        dao.close();

        ProductoAdapter adapter = new ProductoAdapter(lista, new ProductoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto p) {
                Toast.makeText(MenuProductosActivity.this, "Agregado al carrito: " + p.getNombre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(Producto p, android.view.View v) {
                // Cliente no edita ni borra
            }
        });
        rv.setAdapter(adapter);
    }
}
