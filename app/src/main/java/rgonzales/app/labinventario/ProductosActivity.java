package rgonzales.app.labinventario;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import rgonzales.app.labinventario.adapter.ProductoAdapter;
import rgonzales.app.labinventario.dao.ProductoDAO;
import rgonzales.app.labinventario.model.Producto;

public class ProductosActivity extends AppCompatActivity {
    //atributos
    private ProductoDAO dao;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private TextView tvTitulo;
    private int idCategoria;
    private String nombreCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        idCategoria = getIntent().getIntExtra("id_categoria",0);
        nombreCategoria = getIntent().getStringExtra("nombre_categoria");
        //inicializar las vistas
        dao = new ProductoDAO(this);
        rv = findViewById(R.id.rvProductos);
        fab = findViewById(R.id.fabAddProducto);
        tvTitulo= findViewById(R.id.tvTituloProductos);

        tvTitulo.setText("Productos:"+nombreCategoria);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Productos:"+nombreCategoria);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rv.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(v -> mostrarDialogoProducto(null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refrescarLista();
    }
    private void refrescarLista() {
        dao.open();
        List<Producto> lista = dao.listarPorCategoria(idCategoria);
        dao.close();

        ProductoAdapter adapter = new ProductoAdapter(lista, new ProductoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto p) {
                // Aquí podrías abrir un detalle del producto si quisieras
            }

            @Override
            public void onItemLongClick(Producto p, View v) {
                PopupMenu popup = new PopupMenu(ProductosActivity.this, v);
                popup.getMenu().add("Editar");
                popup.getMenu().add("Borrar");

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("Editar")) {
                        mostrarDialogoProducto(p);
                    } else if (item.getTitle().equals("Borrar")) {
                        confirmarEliminacion(p);
                    }
                    return true;
                });
                popup.show();
            }
        });
        rv.setAdapter(adapter);
    }

    private void confirmarEliminacion(Producto p) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Producto")
                .setMessage("¿Estás seguro de que deseas eliminar el producto " + p.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    eliminarProducto(p.getId());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarProducto(int id) {
        dao.open();
        dao.eliminarProducto(id);
        dao.close();
        refrescarLista();
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
    }

    private void mostrarDialogoProducto(final Producto exist) {
        View v = getLayoutInflater().inflate(R.layout.dialog_producto, null);
        EditText etNom = v.findViewById(R.id.etNombreProd);
        EditText etPre = v.findViewById(R.id.etPrecioProd);
        EditText etCan = v.findViewById(R.id.etCantidadProd);

        if (exist != null) {
            etNom.setText(exist.getNombre());
            etPre.setText(String.valueOf(exist.getPrecio()));
            etCan.setText(String.valueOf(exist.getCantidad()));
        }

        new AlertDialog.Builder(this)
                .setTitle(exist == null ? "Nuevo Producto" : "Editar Producto")
                .setView(v)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nom = etNom.getText().toString().trim();
                    String preStr = etPre.getText().toString().trim();
                    String canStr = etCan.getText().toString().trim();

                    if (!nom.isEmpty() && !preStr.isEmpty() && !canStr.isEmpty()) {
                        try {
                            double precio = Double.parseDouble(preStr);
                            int cantidad = Integer.parseInt(canStr);

                            Producto p = (exist == null) ? new Producto() : exist;
                            p.setNombre(nom);
                            p.setPrecio(precio);
                            p.setCantidad(cantidad);
                            p.setIdCategoria(idCategoria);

                            dao.open();
                            if (exist == null) dao.insertarProducto(p);
                            else dao.actualizar(p);
                            dao.close();

                            refrescarLista();
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Error en los números", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}