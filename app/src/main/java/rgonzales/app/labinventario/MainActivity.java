package rgonzales.app.labinventario;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import rgonzales.app.labinventario.adapter.CategoriaAdapter;
import rgonzales.app.labinventario.dao.CategoriaDAO;
import rgonzales.app.labinventario.login.SessionManager;
import rgonzales.app.labinventario.model.Categoria;

public class MainActivity extends AppCompatActivity {
    SessionManager session;
    private CategoriaDAO dao;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private Button btnVerVentas, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dao = new CategoriaDAO(this);
        rv = findViewById(R.id.rvCategorias);
        fab = findViewById(R.id.fabAddCategoria);
        btnVerVentas = findViewById(R.id.btnVerVentasDirecto);
        btnLogout = findViewById(R.id.btnLogoutAdmin);
        
        rv.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(v -> mostrarDialogoCategoria(null));
        
        btnVerVentas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VerPagosActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        
        session = new SessionManager(this);
    }

    private void mostrarDialogoCategoria(final Categoria exist) {
        View view = getLayoutInflater().inflate(R.layout.dialog_categoria,null);
        final EditText etNombre = view.findViewById(R.id.etNombreCategoria);
        if(exist != null)
        {
            etNombre.setText(exist.getNombre());
        }
        new AlertDialog.Builder(this)
                .setTitle(exist == null ? "Nueva categoria" : "Editar categoria")
                .setView(view)
                .setPositiveButton("Guardar",(dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    if(!nombre.isEmpty()){
                        dao.open();
                        if(exist == null) dao.insertar(nombre);
                        else dao.actualizar(exist.getId(),nombre);
                        dao.close();
                        refrescarLista();
                    }
                })
                .setNegativeButton("Cancelar",null)
                .show();
    }
    public void refrescarLista()
    {
        dao.open();
        List<Categoria> lista = dao.listarConConteo();
        dao.close();
        CategoriaAdapter adapter = new CategoriaAdapter(lista, new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria c) {
                Intent i = new Intent(MainActivity.this,ProductosActivity.class);
                i.putExtra("id_categoria",c.getId());
                i.putExtra("nombre_categoria",c.getNombre());
                startActivity(i);
            }

            @Override
            public void onItemLongClick(Categoria c, View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this,v);
                popup.getMenu().add("Editar");
                popup.getMenu().add("Eliminar");
                popup.setOnMenuItemClickListener(item -> {
                    if(item.getTitle().equals("Editar"))
                    {
                        mostrarDialogoCategoria(c);
                    }else if(item.getTitle().equals("Eliminar"))
                    {
                        eliminarCategoria(c.getId());
                    }
                    return true;
                });
                popup.show();
            }
        });
        rv.setAdapter(adapter);
    }
    void eliminarCategoria(int id){
        dao.open();
        dao.eliminar(id);
        dao.close();
        refrescarLista();
        Toast.makeText(this,"Categoria eliminada",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refrescarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_option1) {
            finishAffinity();
            return true;
        }

        if (id == R.id.menu_option2) {
            session.deletePin();
            Toast.makeText(this, "PIN Deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        if (id == R.id.menu_option3) {
            Intent i = new Intent(MainActivity.this, FindProduct.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.menu_ventas) {
            Intent i = new Intent(MainActivity.this, VerPagosActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
