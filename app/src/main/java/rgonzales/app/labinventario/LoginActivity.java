package rgonzales.app.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import rgonzales.app.labinventario.dao.AdminDAO;
import rgonzales.app.labinventario.login.SessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText etPin, etConfirmPin, etAdminUser, etAdminPass;
    Button btnAction, btnAdmin;
    ImageButton btnReset;
    TextView tvTitle;
    LinearLayout layoutPin, layoutAdmin;
    SessionManager session;
    AdminDAO adminDao;
    boolean isFirstStart = true;
    boolean isAdminMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etPin = findViewById(R.id.etPin);
        etConfirmPin = findViewById(R.id.etConfirmPin);
        etAdminUser = findViewById(R.id.etAdminUser);
        etAdminPass = findViewById(R.id.etAdminPass);
        btnAction = findViewById(R.id.btnAccion);
        tvTitle = findViewById(R.id.tvTitulo);
        session = new SessionManager(this);
        adminDao = new AdminDAO(this);
        isFirstStart = !session.isPinConfigured();
        btnReset = findViewById(R.id.btnReset);
        btnAdmin = findViewById(R.id.btnAdmin);
        layoutPin = findViewById(R.id.layoutPin);
        layoutAdmin = findViewById(R.id.layoutAdmin);

        updateUI();

        btnAdmin.setOnClickListener(v -> {
            isAdminMode = !isAdminMode;
            updateUI();
        });

        btnAction.setOnClickListener(v -> {
            if (isAdminMode) {
                handleAdminLogin();
            } else {
                handlePinLogin();
            }
        });

        btnReset.setOnClickListener(v -> {
            session.deletePin();
            Toast.makeText(this, "Pin cleared", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateUI() {
        if (isAdminMode) {
            tvTitle.setText("Admin Login");
            layoutPin.setVisibility(View.GONE);
            layoutAdmin.setVisibility(View.VISIBLE);
            btnAdmin.setText("Ingresar con PIN");
        } else {
            layoutAdmin.setVisibility(View.GONE);
            layoutPin.setVisibility(View.VISIBLE);
            btnAdmin.setText("Ingresar como Administrador");
            if (isFirstStart) {
                tvTitle.setText("Create access pin");
                etConfirmPin.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setText("Enter pin");
                etConfirmPin.setVisibility(View.GONE);
            }
        }
    }

    private void handlePinLogin() {
        String pin = etPin.getText().toString().trim();
        if (pin.isEmpty()) {
            Toast.makeText(this, "Please enter pin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isFirstStart) {
            String confirmPin = etConfirmPin.getText().toString().trim();
            if (!pin.equals(confirmPin)) {
                Toast.makeText(this, "Pins are not the same", Toast.LENGTH_SHORT).show();
                return;
            }

            session.savePin(pin);
            Toast.makeText(this, "Pin saved", Toast.LENGTH_SHORT).show();
            showClientPage();
        } else {
            if (session.validatePin(pin)) {
                Toast.makeText(this, "Pin is correct", Toast.LENGTH_SHORT).show();
                showClientPage();
            } else {
                Toast.makeText(this, "Pin is incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleAdminLogin() {
        String user = etAdminUser.getText().toString().trim();
        String pass = etAdminPass.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        adminDao.open();
        boolean isValid = adminDao.validarAdmin(user, pass);
        adminDao.close();

        if (isValid) {
            Toast.makeText(this, "Admin login success", Toast.LENGTH_SHORT).show();
            showAdminPage();
        } else {
            Toast.makeText(this, "Usuario o clave incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAdminPage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void showClientPage() {
        Intent intent = new Intent(LoginActivity.this, MenuProductosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
