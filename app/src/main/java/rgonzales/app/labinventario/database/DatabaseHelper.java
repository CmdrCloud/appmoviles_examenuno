package rgonzales.app.labinventario.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventario.db";
    private static final int VERSION = 2;
    private static final String CATEGORIAS = "create table categorias(" +
            "id integer primary key autoincrement," +
            "nombre text not null," +
            "fecha_creacion datetime default CURRENT_TIMESTAMP);";
    private static final String PRODUCTOS = "create table productos(" +
            "id integer primary key autoincrement," +
            "nombre text not null," +
            "precio real not null," +
            "cantidad integer not null," +
            "id_categoria integer not null," +
            "fecha_creacion datetime default CURRENT_TIMESTAMP," +
            "foreign key(id_categoria) references categorias(id) ON DELETE CASCADE" +
            ");";

    private static final String ADMINISTRADOR = "create table administrador(" +
            "id integer primary key autoincrement," +
            "username text not null," +
            "password text not null);";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly())
        {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CATEGORIAS);
        db.execSQL(PRODUCTOS);
        db.execSQL(ADMINISTRADOR);
        // Insert default administrator
        db.execSQL("INSERT INTO administrador (username, password) VALUES ('admin', 'cl4v3')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists productos");
        db.execSQL("drop table if exists categorias");
        db.execSQL("drop table if exists administrador");
        onCreate(db);
    }
}
