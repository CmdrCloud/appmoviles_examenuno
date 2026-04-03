package rgonzales.app.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import rgonzales.app.labinventario.login.SessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText etPin;
    EditText etConfirmPin;
    Button btnAction;
    ImageButton btnReset;
    TextView tvTitle;
    SessionManager session;
    boolean isFirstStart = true;
    String temporaryPin = "";

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
        btnAction = findViewById(R.id.btnAccion);
        tvTitle = findViewById(R.id.tvTitulo);
        session = new SessionManager(this);
        isFirstStart = !session.isPinConfigured();
        btnReset = findViewById(R.id.btnReset);

        if (isFirstStart) {
            tvTitle.setText("Create access pin");
            etConfirmPin.setVisibility(View.VISIBLE);
        }

        else {
            tvTitle.setText("Enter pin");
            etConfirmPin.setVisibility(View.GONE);
        }

        btnAction.setOnClickListener(v -> {
            String pin = etPin.getText().toString().trim();
            if (pin.isEmpty()) {
                Toast.makeText(this, "Please enter a new pin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isFirstStart) {
                String confirmPin = etConfirmPin.getText().toString().trim();
                if (!pin.equals(confirmPin)) {
                    Toast.makeText(this, "Pins are not the same", Toast.LENGTH_SHORT);
                    return;
                }

                session.savePin(pin);
                Toast.makeText(this, "Pin saved", Toast.LENGTH_SHORT);
                showMainPage();
            }

            else {
                if (session.validatePin(pin)) {
                    Toast.makeText(this, "Pin is correct", Toast.LENGTH_SHORT).show();
                    showMainPage();
                }
                else {
                    Toast.makeText(this, "Pin is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReset.setOnClickListener(v -> {
            session.deletePin();
            Toast.makeText(this, "Pin cleared", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public void showMainPage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // finish();
    }

}