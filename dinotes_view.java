package mobprog.uts.AdindaRiskaSafitri;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class dinotes_view extends AppCompatActivity {

    //untuk mendeklarasi textview dan button yang digunakan pada dinotes_view xml
    private TextView textViewTitle;
    private TextView textViewDetail;
    private TextView textViewCreateDate;
    private Button buttonPrevious;
    private Button buttonNext;
    private Button buttonBackToHome;

    private JSONArray notesArray;
    private int currentNoteIndex = 0;

    //untuk menandakan metode yang dipanggil ketika aktivitas dibuat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinotes_view);

        //untuk memanggil setiap id dari textview dan button yang ada pada dinotes_view xml
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDetail = findViewById(R.id.textViewDetail);
        textViewCreateDate = findViewById(R.id.textViewCreateDate);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBackToHome = findViewById(R.id.buttonBackToHome);

        //untuk klik button catatanmu sebelumnya
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousNote();
            }
        });

        //untuk klik button catatanmu berikutnya
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextNote();
            }
        });

        //untuk klik button kembali
        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dinotes_view.this, dinotes_home.class);
                startActivity(intent);
                finish();
            }
        });

        // Fetch the notes data from the server
        new FetchNotesTask().execute();
    }

    //untuk fungsi menunjukan catatanmu sebelumnya
    private void showPreviousNote() {
        if (currentNoteIndex > 0) {
            currentNoteIndex--;
            displayNote();
        } else {
            Toast.makeText(this, "Ini catatan pertama", Toast.LENGTH_SHORT).show();
        }
    }

    //untuk fungsi menunjukan catatanmu berikutnya
    private void showNextNote() {
        if (notesArray != null && currentNoteIndex < notesArray.length() - 1) {
            currentNoteIndex++;
            displayNote();
        } else {
            Toast.makeText(this, "Ini catatan terakhir", Toast.LENGTH_SHORT).show();
        }
    }

    //untuk melihat isi catatanmu
    private void displayNote() {
        try {
            JSONObject note = notesArray.getJSONObject(currentNoteIndex);
            String title = note.getString("judul_catatan");
            String detail = note.getString("detail_catatan");
            String createDate = note.getString("create_date");

            textViewTitle.setText(title);
            textViewDetail.setText(detail);
            textViewCreateDate.setText(createDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //untuk fungsi lihat catatanmu yang sudah tersinkronisasi ke request API method GET
    private class FetchNotesTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                URL url = new URL("http://103.178.153.230/uts/indexapi.php?req=get_catatan&nim=0102522003");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                connection.disconnect();

                response = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error: " + e.getMessage();
            }
            return response;
        }

        //untuk menampilkan catatan pentingmu
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                boolean success = jsonResponse.getBoolean("success");

                if (success) {
                    notesArray = jsonResponse.getJSONArray("data_catatan");
                    if (notesArray.length() > 0) {
                        displayNote();
                    } else {
                        Toast.makeText(dinotes_view.this, "Belum ada catatan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(dinotes_view.this, jsonResponse.getString("pesan"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(dinotes_view.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

