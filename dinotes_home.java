package mobprog.uts.AdindaRiskaSafitri;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class dinotes_home extends AppCompatActivity {
    //untuk mendeklarasi id edittext dan button yang digunakan pada xml dinotes_home
    private EditText editTextTitle;
    private EditText editTextDetail;
    private Button buttonAddNote;
    private Button buttonViewNotes;

    @SuppressLint("MissingInflatedId")
    @Override
    //untuk menandakan metode yang dipanggil ketika aktivitas dibuat
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinotes_home);

        //untuk memanggil edittext dan button dari xml dinotes_home
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDetail = findViewById(R.id.editTextDetail);
        buttonAddNote = findViewById(R.id.buttonAddNote);
        buttonViewNotes = findViewById(R.id.buttonViewNotes);

        //untuk navigasi button tambah catatan pentingmu
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahCatatan();
            }
        });

        //untuk navigasi button lihat catatan pentingmu
        buttonViewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lihatCatatan();
            }
        });
    }

    //untuk fungsi tambah catatan pentingmu
    private void tambahCatatan() {
        String judul = editTextTitle.getText().toString();
        String detail = editTextDetail.getText().toString();

        if (judul.isEmpty() || detail.isEmpty()) {
            Toast.makeText(this, "Judul dan detail tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat JSON Object untuk request API
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req", "tambah_catatan");
            jsonObject.put("nim", "0102522003");
            jsonObject.put("judul_catatan", judul);
            jsonObject.put("detail_catatan", detail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new TambahCatatanTask().execute(jsonObject.toString());
    }

    //untuk fungsi lihat catatan pentingmu
    private void lihatCatatan() {
        Intent intent = new Intent(dinotes_home.this, dinotes_view.class);
        startActivity(intent);
    }

    //untuk fungsi tambah catatan pentingmu agar tersinkronisasi ke request API method POST
    private class TambahCatatanTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String jsonString = params[0];
            String response = "";

            try {
                URL url = new URL("http://103.178.153.230/uts/indexapipost.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(jsonString.getBytes("UTF-8"));
                os.close();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    response = "Catatan berhasil ditambahkan";
                } else {
                    response = "Gagal menambahkan catatan";
                }
                connection.disconnect();
            } catch (Exception e) {
                response = "Error: " + e.getMessage();
            }
            return response;
        }

        //untuk button tambah catatan pentingmu
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(dinotes_home.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
