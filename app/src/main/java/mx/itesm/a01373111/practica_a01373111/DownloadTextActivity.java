package mx.itesm.a01373111.practica_a01373111;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_text);

        AndroidNetworking.initialize(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidNetworking.get("https://www.googleapis.com/books/v1/volumes?q=isbn:9781449362188")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arrItems = response.getJSONArray("items");
                            JSONObject item = arrItems.getJSONObject(0);
                            JSONObject info = item.getJSONObject("volumeInfo");
                            String titulo = info.getString("title");

                            // Imagen
                            JSONObject imagenes = info.getJSONObject("imageLinks");
                            String urlImagen = imagenes.getString("smallThumbnail");

                            TextView tvTexto = findViewById(R.id.tvTexto);
                            tvTexto.setText(titulo + "\n\n" + urlImagen);

                            descargarImagenLibro(urlImagen);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

                            /*getAsString(new StringRequestListener() {
                             @Override
                             public void onResponse(String response) {
                                 TextView tvTexto = findViewById(R.id.tvTexto);
                                 tvTexto.setText(response);
                             }

                             @Override
                             public void onError(ANError anError) {
                                 TextView tvTexto = findViewById(R.id.tvTexto);
                                 tvTexto.setText(anError.toString());
                             }
                         });*/
    }

    private void descargarImagenLibro(String urlImagen) {
        urlImagen.replace("http","https");
        AndroidNetworking.get(urlImagen)
                .setPriority(Priority.MEDIUM)
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // poner el bitmap en un imageview
                        ImageView ivView = findViewById(R.id.ivLibro);
                        ivView.setImageBitmap(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("Error ***************\n", anError.toString());
                    }
                });
    }
}
