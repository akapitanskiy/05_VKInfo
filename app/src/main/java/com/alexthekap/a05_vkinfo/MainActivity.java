package com.alexthekap.a05_vkinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.alexthekap.a05_vkinfo.utils.NetworkUtils.generateURL;
import static com.alexthekap.a05_vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchBtn;
    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchBtn = findViewById(R.id.b_search_vk);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL generatedUrl = generateURL(searchField.getText().toString());

                new VKQueryTask().execute(generatedUrl);
            }
        };
        searchBtn.setOnClickListener(onClickListener);
    }

    private void showResultTextView() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView() {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    class VKQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
            result.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String firstName = null;
            String lastName = null;

            if(response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    JSONObject userInfo = jsonArray.getJSONObject(0);
                    firstName = userInfo.getString("first_name");
                    lastName = userInfo.getString("last_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String resultString = "Имя: " + firstName + "\n" + "Фамилия: " + lastName;
                result.setText(resultString);
                showResultTextView();
            } else {
                showErrorTextView();
            }
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
