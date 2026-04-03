package rgonzales.app.labinventario.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import rgonzales.app.labinventario.database.DatabaseHelper;

public class AdminDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public AdminDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean validarAdmin(String user, String pass) {
        String sql = "SELECT * FROM administrador WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{user, pass});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
