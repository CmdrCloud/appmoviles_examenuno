package rgonzales.app.labinventario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rgonzales.app.labinventario.database.DatabaseHelper;
import rgonzales.app.labinventario.model.Categoria;

public class CategoriaDAO
{
    private SQLiteDatabase db;
    // mi clase propia DatabaseHelper
    private DatabaseHelper dbHelper;
    public CategoriaDAO(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }
    public void open(){
        db = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }
    public long insertar(String nom){
        ContentValues valores = new ContentValues();
        valores.put("nombre",nom);
        return db.insert("categorias",null,valores);
    }
    public int actualizar(int id,String nom)
    {
        ContentValues valores = new ContentValues();
        valores.put("nombre",nom);
        return db.update("categorias",valores,
                "id=?",
                new String[]{String.valueOf(id)});
    }
    public void eliminar(int id)
    {
        db.delete("categorias","id=?",
                new String[]{String.valueOf(id)});
    }
    public List<Categoria> listarConConteo(){
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT c.id,c.nombre,COUNT(p.id) as total "+
                "FROM categorias c LEFT JOIN productos p "+
                "ON c.id=p.id_categoria "+
                "GROUP BY c.id,c.nombre "+
                "ORDER BY c.nombre asc";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do{
                Categoria cat = new Categoria();
                cat.setId(cursor.getInt(0));
                cat.setNombre(cursor.getString(1));
                cat.setTotalProductos(cursor.getInt(2));
                lista.add(cat);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}
