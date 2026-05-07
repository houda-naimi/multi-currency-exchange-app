package com.example.evaluation_app;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FetchRatesTask extends AsyncTask<String, Void, Map<String, Double>> {

    private Context context;
    private CurrencyService.CurrencyServiceCallback callback;

    public FetchRatesTask(Context context, CurrencyService.CurrencyServiceCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Map<String, Double> doInBackground(String... urls) {
        Map<String, Double> rates = new HashMap<>();
        try {
            // Créer une connexion HTTP pour envoyer la requête
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Lire la réponse de l'API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse le JSON de la réponse
            JSONObject responseObject = new JSONObject(response.toString());
            JSONObject ratesObject = responseObject.getJSONObject("conversion_rates");
            Iterator<String> keys = ratesObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                rates.put(key, ratesObject.getDouble(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return rates;
    }

    @Override
    protected void onPostExecute(Map<String, Double> rates) {
        if (rates != null) {
            callback.onSuccess(rates);
        } else {
            Toast.makeText(context, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            callback.onFailure(new Exception("Erreur de récupération des taux"));
        }
    }
}
