package rgonzales.app.labinventario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rgonzales.app.labinventario.database.DatabaseHelper;
import rgonzales.app.labinventario.model.CartItem;

public class VentaDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public VentaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long registrarVenta(double subtotal, double tarifa, double total, List<CartItem> items) {
        db.beginTransaction();
        try {
            ContentValues valuesVenta = new ContentValues();
            valuesVenta.put("subtotal", subtotal);
            valuesVenta.put("tarifa_servicio", tarifa);
            valuesVenta.put("total", total);
            long idVenta = db.insert("ventas", null, valuesVenta);

            for (CartItem item : items) {
                ContentValues valuesDetalle = new ContentValues();
                valuesDetalle.put("id_venta", idVenta);
                valuesDetalle.put("id_producto", item.getProducto().getId());
                valuesDetalle.put("nombre_producto", item.getProducto().getNombre());
                valuesDetalle.put("cantidad", item.getCantidad());
                valuesDetalle.put("precio_unitario", item.getProducto().getPrecio());
                db.insert("detalle_ventas", null, valuesDetalle);
                
                // Opcional: Descontar stock del producto
                db.execSQL("UPDATE productos SET cantidad = cantidad - ? WHERE id = ?", 
                        new Object[]{item.getCantidad(), item.getProducto().getId()});
            }

            db.setTransactionSuccessful();
            return idVenta;
        } finally {
            db.endTransaction();
        }
    }

    public List<VentaInfo> listarVentas() {
        List<VentaInfo> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id, fecha, total FROM ventas ORDER BY fecha DESC", null);
        if (cursor.moveToFirst()) {
            do {
                VentaInfo v = new VentaInfo();
                v.id = cursor.getInt(0);
                v.fecha = cursor.getString(1);
                v.total = cursor.getDouble(2);
                lista.add(v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public static class VentaInfo {
        public int id;
        public String fecha;
        public double total;
    }
}
