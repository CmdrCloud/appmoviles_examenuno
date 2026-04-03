package rgonzales.app.labinventario.login;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "mispref";
    private static final String KEY_PIN = "pin";
    private static final String KEY_IS_CONFIGURED = "configurado";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void savePin(String pin) {
        editor.putString(KEY_PIN, pin);
        editor.putBoolean(KEY_IS_CONFIGURED, true);
        editor.apply();
    }

    public boolean isPinConfigured() {
        return pref.getBoolean(KEY_IS_CONFIGURED, false);
    }

    public boolean validatePin(String pin) {
        return pref.getString(KEY_PIN, "").equals(pin);
    }

    public void deletePin() {
        editor.clear();
        editor.apply();
    }
}
