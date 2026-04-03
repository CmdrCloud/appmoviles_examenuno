package rgonzales.app.labinventario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rgonzales.app.labinventario.database.DatabaseHelper;
import rgonzales.app.labinventario.model.Producto;

public class ProductoDAO {
    private SQLiteDatabase db;
    // mi clase propia DatabaseHelper
    private DatabaseHelper dbHelper;
    public ProductoDAO(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }
    public void open()
    {
        db = dbHelper.getWritableDatabase();
    }
    public void close()
    {
        dbHelper.close();
    }
    public long insertarProducto(Producto p)
    {
        ContentValues valores = new ContentValues();
        valores.put("nombre",p.getNombre());
        valores.put("precio",p.getPrecio());
        valores.put("cantidad",p.getCantidad());
        valores.put("id_categoria",p.getIdCategoria());
        return db.insert("productos",null,valores);
    }
    public List<Producto> obtenerProductosPorCategoria(int idCat)
    {
        List<Producto> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM productos " +
                        "WHERE id_categoria =? ORDER BY nombre asc",
            new String[]{String.valueOf(idCat)});
        if(cursor.moveToFirst())
        {
            do{
                Producto p = new Producto();
                p.setId(cursor.getInt(0));
                p.setNombre(cursor.getString(1));
                p.setPrecio(cursor.getDouble(2));
                p.setCantidad(cursor.getInt(3));
                p.setIdCategoria(cursor.getInt(4));
                lista.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public void eliminarProducto(int id)
    {
        db.delete("productos","id=?",
                new String[]{String.valueOf(id)});
    }
    public List<Producto> listarPorCategoria(int idCat) {
        List<Producto> lista = new ArrayList<>();

        // Usamos query() que es más seguro o rawQuery
        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, precio, cantidad, id_categoria FROM productos WHERE id_categoria = ? ORDER BY id DESC",
                new String[]{String.valueOf(idCat)}
        );

        if (cursor.moveToFirst()) {
            do {
                Producto p = new Producto();
                p.setId(cursor.getInt(0));
                p.setNombre(cursor.getString(1));
                p.setPrecio(cursor.getDouble(2));
                p.setCantidad(cursor.getInt(3));
                p.setIdCategoria(cursor.getInt(4));
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public int actualizar(Producto p) {
        ContentValues values = new ContentValues();
        values.put("nombre", p.getNombre());
        values.put("precio", p.getPrecio());
        values.put("cantidad", p.getCantidad());

        return db.update("productos", values, "id = ?",
                new String[]{String.valueOf(p.getId())});
    }

    public List<Producto> buscarPorNombre(String criterio) {

        List<Producto> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, precio, cantidad, id_categoria FROM productos " +
                        "WHERE nombre LIKE ? ORDER BY nombre",
                new String[]{"%" + criterio + "%"}
        );

        if(cursor.moveToFirst()){
            do{
                Producto p = new Producto();
                p.setId(cursor.getInt(0));
                p.setNombre(cursor.getString(1));
                p.setPrecio(cursor.getDouble(2));
                p.setCantidad(cursor.getInt(3));
                p.setIdCategoria(cursor.getInt(4));
                lista.add(p);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }
}
