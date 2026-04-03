package rgonzales.app.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rgonzales.app.labinventario.adapter.MenuProductoAdapter;
import rgonzales.app.labinventario.dao.CarritoDAO;
import rgonzales.app.labinventario.dao.CategoriaDAO;
import rgonzales.app.labinventario.dao.ProductoDAO;
import rgonzales.app.labinventario.model.Categoria;
import rgonzales.app.labinventario.model.Producto;

public class MenuProductosActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fabCarrito;
    private Button btnAgregarVarios;
    private EditText etBuscar;
    private AutoCompleteTextView spinnerCategoria;
    private ProductoDAO dao;
    private CarritoDAO carritoDao;
    private CategoriaDAO categoriaDao;
    private MenuProductoAdapter adapter;
    private List<Categoria> categorias;
    private int idCategoriaSeleccionada = -1; // -1 significa "Todas"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_productos);

        rv = findViewById(R.id.rvMenuProductos);
        fabCarrito = findViewById(R.id.fabCarrito);
        btnAgregarVarios = findViewById(R.id.btnAgregarVariosAlCarrito);
        etBuscar = findViewById(R.id.etBuscarMenu);
        spinnerCategoria = findViewById(R.id.spinnerCategoriaMenu);
        
        dao = new ProductoDAO(this);
        carritoDao = new CarritoDAO(this);
        categoriaDao = new CategoriaDAO(this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        
        fabCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(MenuProductosActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        btnAgregarVarios.setOnClickListener(v -> {
            if (adapter != null) {
                Map<Integer, Integer> selecciones = adapter.getCantidadesSeleccionadas();
                if (selecciones.isEmpty()) {
                    Toast.makeText(this, "No has seleccionado ningún producto", Toast.LENGTH_SHORT).show();
                    return;
                }

                carritoDao.open();
                int count = 0;
                for (Map.Entry<Integer, Integer> entry : selecciones.entrySet()) {
                    int idProd = entry.getKey();
                    int cant = entry.getValue();
                    if (cant > 0) {
                        for(int i=0; i<cant; i++) {
                            carritoDao.agregarOIncrementar(idProd);
                        }
                        count++;
                    }
                }
                carritoDao.close();

                if (count > 0) {
                    Toast.makeText(this, "Productos agregados al carrito", Toast.LENGTH_SHORT).show();
                    adapter.clearSelections();
                } else {
                    Toast.makeText(this, "Selecciona al menos 1 unidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarProductos();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        configurarSpinnerCategorias();
        cargarProductos();
    }

    private void configurarSpinnerCategorias() {
        categoriaDao.open();
        categorias = categoriaDao.listarConConteo();
        categoriaDao.close();

        List<String> nombresCategorias = new ArrayList<>();
        nombresCategorias.add("Todas las categorías");
        for (Categoria c : categorias) {
            nombresCategorias.add(c.getNombre());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nombresCategorias);
        spinnerCategoria.setAdapter(spinnerAdapter);
        spinnerCategoria.setText(nombresCategorias.get(0), false);

        spinnerCategoria.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                idCategoriaSeleccionada = -1;
            } else {
                idCategoriaSeleccionada = categorias.get(position - 1).getId();
            }
            filtrarProductos();
        });
    }

    private void cargarProductos() {
        filtrarProductos();
    }

    private void filtrarProductos() {
        String query = etBuscar.getText().toString().trim();
        dao.open();
        List<Producto> lista;
        if (idCategoriaSeleccionada == -1) {
            lista = dao.buscarPorNombre(query);
        } else {
            // Filtrado manual por categoría y nombre ya que el DAO no tiene buscarPorNombreYCategoria
            List<Producto> porCategoria = dao.listarPorCategoria(idCategoriaSeleccionada);
            lista = new ArrayList<>();
            for (Producto p : porCategoria) {
                if (p.getNombre().toLowerCase().contains(query.toLowerCase())) {
                    lista.add(p);
                }
            }
        }
        dao.close();

        adapter = new MenuProductoAdapter(lista);
        rv.setAdapter(adapter);
    }
}
