package com.example.ceyda.mobilbank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
public class MainActivity5 extends AppCompatActivity {

    TextView devamEtBtn;
    TextView kameraAcBTn;
    ImageView tcKimlikArkaYuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        devamEtBtn = findViewById(R.id.devamEtBtn);
        kameraAcBTn = findViewById(R.id.kameraAcBtn);
        tcKimlikArkaYuz = findViewById(R.id.tcKimlikArkaYuz);

        devamEtBtn.setClickable(false);
        devamEtBtn.setEnabled(false);

        kameraAcBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        devamEtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = ((BitmapDrawable) tcKimlikArkaYuz.getDrawable()).getBitmap();
                String base64Image = bitmapToBase64(bitmap);

                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("image", base64Image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = "http://192.168.1.37:5000/api/v1/predictions"; // Gerçek URL'yi buraya ekleyin

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // İstek başarılı
                                try {
                                    String prediction = response.getString("prediction");
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
                                Log.e("WebService", "Hata: " + error.toString());
                            }
                        });

                Volley.newRequestQueue(MainActivity5.this).add(request);
                // MainActivity6'yi aç
                Intent intent = new Intent(MainActivity5.this, MainActivity6.class);
                startActivity(intent);
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap originalBitmap = (Bitmap) data.getExtras().get("data");
            if (originalBitmap != null) {
                // Hedef boyutu dp cinsinden belirle
                int newWidthDp = 300;
                int newHeightDp = 390;

                // DP değerlerini piksel cinsine çevir
                float scale = getResources().getDisplayMetrics().density;
                int newWidthPx = (int) (newWidthDp * scale + 0.5f);
                int newHeightPx = (int) (newHeightDp * scale + 0.5f);

                // Çekilen fotoğrafın oranlarını koruyarak hedef boyuta getir
                float aspectRatio = (float) originalBitmap.getWidth() / (float) originalBitmap.getHeight();
                int targetWidth, targetHeight;
                if (aspectRatio > 1) {
                    targetWidth = newWidthPx;
                    targetHeight = (int) (newWidthPx / aspectRatio);
                } else {
                    targetWidth = (int) (newHeightPx * aspectRatio);
                    targetHeight = newHeightPx;
                }

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);

                // ImageView'a ayarla
                tcKimlikArkaYuz.setImageBitmap(scaledBitmap);

                // Base64 kodlaması ve diğer işlemler
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                //textView.setText(base64Image);

                // Fotoğraf çekildiğinde devamEtBtn'i tıklanabilir yap
                devamEtBtn.setBackgroundResource(R.drawable.regular_button_background);
                devamEtBtn.setEnabled(true);
                devamEtBtn.setClickable(true);
            }
        }
    }
}