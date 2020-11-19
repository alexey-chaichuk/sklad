package ru.momentum.sklad;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import ru.momentum.sklad.dto.BarcodeSearchRespDTO;
import ru.momentum.sklad.dto.NomenklaturaDTO;

public class BarcodeSearchRespActivity extends AppCompatActivity {

    TextView ownerName;
    TextView ownerCode;
    TextView serial;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_search_resp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //NomenklaturaDTO nomen = (NomenklaturaDTO) getIntent().getSerializableExtra("nomenclature");
        ownerName = (TextView) findViewById(R.id.ownerName);
        ownerCode = (TextView) findViewById(R.id.ownerCode);
        serial = (TextView) findViewById(R.id.serial);
        progressBar = (ProgressBar) findViewById(R.id.progressBarcodeResp);

        ownerName.setVisibility(View.GONE);
        ownerCode.setVisibility(View.GONE);
        serial.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String barcode = getIntent().getStringExtra("barcode");
        new GetNomenklaturaByBarcodeTask().execute(barcode);
    }


    private class GetNomenklaturaByBarcodeTask extends AsyncTask<String, Void, BarcodeSearchRespDTO[]> {

        @Override
        protected BarcodeSearchRespDTO[] doInBackground(String... params) {

            String barcode = params[0];

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication("obmen", "Obmen1234");
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try {
                // Make the network request
                ResponseEntity<BarcodeSearchRespDTO[]> response = template.exchange(Constants.URL.GET_BY_BARCODE + barcode,
                        HttpMethod.GET, new HttpEntity<Object>(requestHeaders), BarcodeSearchRespDTO[].class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e("RemindMe", e.getLocalizedMessage(), e);
                return new BarcodeSearchRespDTO[]{new BarcodeSearchRespDTO("HttpClientErrorException")};
            } catch (ResourceAccessException e) {
                Log.e("RemindMe", e.getLocalizedMessage(), e);
                return new BarcodeSearchRespDTO[]{new BarcodeSearchRespDTO("ResourceAccessException")};
            }

        }

        @Override
        protected void onPostExecute(BarcodeSearchRespDTO[] DTOs) {
            progressBar.setVisibility(View.GONE);

            for (BarcodeSearchRespDTO dto : DTOs) {
                Log.i("barcode", dto.getOwnerName());
                ownerName.setText(dto.getOwnerName());
                ownerCode.setText(dto.getOwner());
                serial.setText(dto.getSerial());
            }

            ownerName.setVisibility(View.VISIBLE);
            ownerCode.setVisibility(View.VISIBLE);
            serial.setVisibility(View.VISIBLE);

        }
    }
}
