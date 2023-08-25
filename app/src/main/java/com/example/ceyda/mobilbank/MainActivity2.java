package com.example.ceyda.mobilbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {

    private EditText adEditText, soyadEditText, tcEditText, mailEditText;
    private TextView devamEtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        adEditText = findViewById(R.id.adEditText);
        soyadEditText = findViewById(R.id.soyadEditText);
        tcEditText = findViewById(R.id.tcEditText);
        mailEditText = findViewById(R.id.mailEditText);

        devamEtBtn = findViewById(R.id.devamEtBtn);
        devamEtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ad = adEditText.getText().toString();
                String soyad = soyadEditText.getText().toString();
                String tc = tcEditText.getText().toString();
                String mail = mailEditText.getText().toString();

                // Alanların boş olup olmadığını kontrol et
                if (ad.isEmpty() || soyad.isEmpty() || tc.isEmpty() || mail.isEmpty()) {

                    /*
                    musteriOlBtn.setClickable(false);
                    musteriOlBtn.setEnabled(false);

                     */
                    // Eksik alanlar var, kullanıcıya uyarı ver veya işlemi tamamlayama
                    // Örnek olarak Toast mesajı gösterebilirsiniz.
                    Toast.makeText(MainActivity2.this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
                    return; // İşlemi sonlandır
                }


                // Verileri JSON formatında oluştur
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("name", ad);
                    jsonParams.put("surname", soyad);
                    jsonParams.put("tc", Integer.parseInt(tc));
                    //jsonParams.put("email", mail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                // Web servis URL'si
                String url = "http://192.168.1.37:5000/api/v1/predictions"; // Gerçek URL'yi buraya ekleyin

                // JSON isteği oluştur
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // İstek başarılı
                                try {
                                    String prediction = response.getString("prediction");
                                    // Tahmin sonucunu al ve istediğiniz gibi kullan
                                    Log.d("WebService", "Başarılı İstek: " + jsonParams.toString());
                                    Log.d("WebService", "Cevap: " + response.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Hata durumu
                                error.printStackTrace();
                                Log.e("WebService", "Hata İstek: " + jsonParams.toString());
                                Log.e("WebService", "Hata: " + error.toString()); // Hata ayrıntılarını logla
                            }
                        });

                // İsteği kuyruğa ekle
                Volley.newRequestQueue(MainActivity2.this).add(request);




                // MainActivity3'ü aç
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                //intent.putExtra("jsonParams", jsonParams.toString());
                startActivity(intent);

            }
        });


    }
}