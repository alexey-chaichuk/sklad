package ru.momentum.sklad;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.momentum.sklad.dto.BarcodeDTO;
import ru.momentum.sklad.dto.BarcodeSearchRespDTO;

public class AddBarcodeActivity extends AppCompatActivity {

    TextView tvBarcode;
    TextView tvBarcodeType;
    TextView tvNomenCode;
    Button addThisBarcode;

    private ProgressDialog mProgressDialog;

    private final static String SCAN_ACTION = "urovo.rcv.message";
    private ScanManager mScanManager;
    private boolean isScaning = false;
    private static String barcodeStr = "---";
    private static byte barcodeType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barcode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvBarcode = (TextView) findViewById(R.id.tvBarcode);
        tvBarcodeType = (TextView) findViewById(R.id.tvBarcodeType);
        tvNomenCode = (TextView) findViewById(R.id.tvNomenCode);

        final String nomenCode = getIntent().getStringExtra("nomenCode");
        tvNomenCode.setText(nomenCode);

        tvBarcode.setText(barcodeStr);
        tvBarcodeType.setText("type: " + barcodeType);

        addThisBarcode = (Button) findViewById(R.id.addThisBarcode);
        addThisBarcode.setEnabled(barcodeType == 0 ? false : true);
        addThisBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mProgressDialog = new ProgressDialog(view.getContext());
                mProgressDialog.setMessage("Идет загрузка в 1с ...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                BarcodeDTO barcodeDTO = new BarcodeDTO();
                barcodeDTO.setBarcode(barcodeStr);
                barcodeDTO.setOwnerCode(nomenCode);

                App.getApi().AddCode(barcodeDTO).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        mProgressDialog.cancel();
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Данные загружены")
                                .setMessage("Получен ответ: "
                                    + response.toString() + " -> "
                                    + response.body())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        mProgressDialog.cancel();
                        if(t.getMessage() != null) {
                            Log.d("error", t.getMessage());
                        } else {
                            Log.d("error", t.toString());
                        }
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Ошибка загрузки данных")
                                .setMessage("Что-то пошло не так: " + t.getClass() + " -> " + t.getMessage())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                });
            }
        });


        initScan();
    }


    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
            //soundpool.play(soundid, 1, 1, 0, 0, 1);
            //showScanResult.setText("");
            //mVibrator.vibrate(100);

            byte[] barcode = intent.getByteArrayExtra("barcode");
            int barcodelen = intent.getIntExtra("length", 0);
            barcodeType = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("barcode", "----codetype-- " + barcodeType);
            barcodeStr = new String(barcode, 0, barcodelen);
            Log.i("barcode", barcodeStr);

            tvBarcode.setText(barcodeStr);
            tvBarcodeType.setText("type: " + barcodeType);

            addThisBarcode.setEnabled(true);

//            Intent intentBarcodeSearchResp = new Intent(context, BarcodeSearchRespActivity.class);
//            intentBarcodeSearchResp.putExtra("barcode", barcodeStr);
//            intentBarcodeSearchResp.putExtra("barcodeType", barcodeType);
//            context.startActivity(intentBarcodeSearchResp);
        }
    };

    private void initScan() {
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode( 0);
        //soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        //soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initScan();
        //showScanResult.setText("");
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
            unregisterReceiver(mScanReceiver);
        }
    }


//    private class AddBarcodeTask extends AsyncTask<String, Void, BarcodeDTO> {
//
//        @Override
//        protected BarcodeDTO doInBackground(String... params) {
//
//            String barcode = params[0];
//
//            // Populate the HTTP Basic Authentitcation header with the username and password
//            HttpAuthentication authHeader = new HttpBasicAuthentication("obmen", "Obmen1234");
//            HttpHeaders requestHeaders = new HttpHeaders();
//            requestHeaders.setAuthorization(authHeader);
//            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//            // Create a new RestTemplate instance
//            RestTemplate template = new RestTemplate();
//            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//
//            try {
//                // Make the network request
//                ResponseEntity<BarcodeSearchRespDTO[]> response = template.exchange(Constants.URL.GET_BY_BARCODE + barcode,
//                        HttpMethod.GET, new HttpEntity<Object>(requestHeaders), BarcodeSearchRespDTO[].class);
//                return response.getBody();
//            } catch (HttpClientErrorException e) {
//                Log.e("RemindMe", e.getLocalizedMessage(), e);
//                return new BarcodeSearchRespDTO[]{new BarcodeSearchRespDTO("HttpClientErrorException")};
//            } catch (ResourceAccessException e) {
//                Log.e("RemindMe", e.getLocalizedMessage(), e);
//                return new BarcodeSearchRespDTO[]{new BarcodeSearchRespDTO("ResourceAccessException")};
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(BarcodeSearchRespDTO[] DTOs) {
//            progressBar.setVisibility(View.GONE);
//
//            for (BarcodeSearchRespDTO dto : DTOs) {
//                Log.i("barcode", dto.getOwnerName());
//                ownerName.setText(dto.getOwnerName());
//                ownerCode.setText(dto.getOwner());
//                serial.setText(dto.getSerial());
//            }
//
//            ownerName.setVisibility(View.VISIBLE);
//            ownerCode.setVisibility(View.VISIBLE);
//            serial.setVisibility(View.VISIBLE);
//
//        }
//    }

}
