package rgonzales.app.labinventario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rgonzales.app.labinventario.database.DatabaseHelper;
import rgonzales.app.labinventario.model.CartItem;
import rgonzales.app.labinventario.model.Producto;

public class CarritoDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CarritoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void agregarOIncrementar(int idProducto) {
        Cursor cursor = db.rawQuery("SELECT cantidad FROM carrito WHERE id_producto = ?", new String[]{String.valueOf(idProducto)});
        if (cursor.moveToFirst()) {
            int cantidadActual = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("cantidad", cantidadActual + 1);
            db.update("carrito", values, "id_producto = ?", new String[]{String.valueOf(idProducto)});
        } else {
            ContentValues values = new ContentValues();
            values.put("id_producto", idProducto);
            values.put("cantidad", 1);
            db.insert("carrito", null, values);
        }
        cursor.close();
    }

    public void actualizarCantidad(int idProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminar(idProducto);
        } else {
            ContentValues values = new ContentValues();
            values.put("cantidad", nuevaCantidad);
            db.update("carrito", values, "id_producto = ?", new String[]{String.valueOf(idProducto)});
        }
    }

    public void eliminar(int idProducto) {
        db.delete("carrito", "id_producto = ?", new String[]{String.valueOf(idProducto)});
    }

    public void vaciarCarrito() {
        db.delete("carrito", null, null);
    }

    public List<CartItem> listarCarrito() {
        List<CartItem> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.precio, p.cantidad, p.id_categoria, c.cantidad " +
                     "FROM carrito c JOIN productos p ON c.id_producto = p.id";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Producto p = new Producto();
                p.setId(cursor.getInt(0));
                p.setNombre(cursor.getString(1));
                p.setPrecio(cursor.getDouble(2));
                p.setCantidad(cursor.getInt(3));
                p.setIdCategoria(cursor.getInt(4));

                int cantidadEnCarrito = cursor.getInt(5);
                lista.add(new CartItem(p, cantidadEnCarrito));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}
