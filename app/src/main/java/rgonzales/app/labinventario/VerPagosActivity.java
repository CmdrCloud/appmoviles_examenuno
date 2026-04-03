package rgonzales.app.labinventario;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import rgonzales.app.labinventario.adapter.VentaAdapter;
import rgonzales.app.labinventario.dao.VentaDAO;

public class VerPagosActivity extends AppCompatActivity {

    private RecyclerView rv;
    private VentaDAO ventaDao;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pagos);

        rv = findViewById(R.id.rvVerPagos);
        btnVolver = findViewById(R.id.btnVolverVerPagos);
        ventaDao = new VentaDAO(this);

        rv.setLayoutManager(new LinearLayoutManager(this));

        btnVolver.setOnClickListener(v -> finish());

        cargarVentas();
    }

    private void cargarVentas() {
        ventaDao.open();
        List<VentaDAO.VentaInfo> lista = ventaDao.listarVentas();
        ventaDao.close();

        VentaAdapter adapter = new VentaAdapter(lista);
        rv.setAdapter(adapter);
    }
}
