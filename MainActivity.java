package mobprog.uts.AdindaRiskaSafitri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    //untuk menandakan metode yang dipanggil ketika aktivitas dibuat
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPindah = findViewById(R.id.pindah);
        buttonPindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke activity dinotes_home
                Intent intent = new Intent(MainActivity.this, dinotes_home.class);
                startActivity(intent);
            }
        });
    }
}
