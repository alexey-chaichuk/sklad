package ru.momentum.sklad;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.momentum.sklad.adapter.NomenRecyclerAdapter;
import ru.momentum.sklad.api.NomenApi;
import ru.momentum.sklad.api.ServiceGenerator;
import ru.momentum.sklad.dto.Doc1c;
import ru.momentum.sklad.dto.NomenklaturaDTO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NomenRecyclerAdapter adapter;
    private ArrayList<NomenklaturaDTO> list;
    private ProgressBar progressBar;
    private ProgressDialog mProgressDialog;

    private final static String SCAN_ACTION = "urovo.rcv.message";
    private ScanManager mScanManager;
    private boolean isScaning = false;
    private String barcodeStr;
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
            byte barcodeType = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("barcode", "----codetype-- " + barcodeType);
            barcodeStr = new String(barcode, 0, barcodelen);
            Log.i("barcode", barcodeStr);

            barcodeStr = "2100000000111";

            Intent intentBarcodeSearchResp = new Intent(context, BarcodeSearchRespActivity.class);
            intentBarcodeSearchResp.putExtra("barcode", barcodeStr);
            intentBarcodeSearchResp.putExtra("barcodeType", barcodeType);
            context.startActivity(intentBarcodeSearchResp);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", list);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList("list");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        if(list == null) {
            list = new ArrayList<NomenklaturaDTO>();
            list.add(new NomenklaturaDTO("test1"));
            list.add(new NomenklaturaDTO("test2"));
            list.add(new NomenklaturaDTO("test3"));
        }
        adapter = new NomenRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        initScan();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            Intent intent = new Intent(this, TabActivity.class);
//            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, Tab2Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
//            new GetNomenklaturaTask().execute();
            progressBar.setVisibility(View.VISIBLE);
            //NomenApi api = ServiceGenerator.createService(NomenApi.class, "obmen", "Obmen1234");

            if (isWifiConnected()) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Идет загрузка из 1с ...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                App.getApi().getData("omron").enqueue(new Callback<List<NomenklaturaDTO>>() {
                    @Override
                    public void onResponse(Call<List<NomenklaturaDTO>> call, Response<List<NomenklaturaDTO>> response) {
                        progressBar.setVisibility(View.GONE);
                        mProgressDialog.cancel();
                        if(response.isSuccessful()) {
                            list.clear();
                            for (NomenklaturaDTO dto : response.body()) {
                                Log.i("got nomenclature^ ", dto.getTitle());
                                list.add(dto);
                            }

                            adapter.setNomenList(list);
                        } else {
                            Log.d("error", "some error");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NomenklaturaDTO>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        mProgressDialog.cancel();
                        if(t.getMessage() != null) {
                            Log.d("error", t.getMessage());
                        } else {
                            Log.d("error", t.toString());
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Ошибка загрузки данных")
                                .setMessage("Что-то пошло не так: " + t.getClass() + " -> " + t.getMessage())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                });

            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Устройство не подключено к сети")
                        .setMessage("Подключитесь к сети WiFi и попробуйте еще раз")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }



//            App.getApi().getData("omron").enqueue(new Callback<List<NomenklaturaDTO>>() {
//                @Override
//                public void onResponse(Call<List<NomenklaturaDTO>> call, Response<List<NomenklaturaDTO>> response) {
//                    progressBar.setVisibility(View.GONE);
//                    if(response.isSuccessful()) {
//                        list.clear();
//                        for (NomenklaturaDTO dto : response.body()) {
//                            Log.i("got nomenclature^ ", dto.getTitle());
//                            list.add(dto);
//                        }
//
//                        adapter.setNomenList(list);
//                    } else {
//                        Log.d("error", "some error");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<NomenklaturaDTO>> call, Throwable t) {
//                    if(t.getMessage() != null) {
//                        Log.d("error", t.getMessage());
//                    }
//                }
//            });

        } else if (id == R.id.nav_manage) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage("Идет загрузка из 1с ...");
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();
//
//            App.getApi().findPTU().enqueue(new Callback<Doc1c>() {
//                @Override
//                public void onResponse(Call<Doc1c> call, Response<Doc1c> response) {
//                    mProgressDialog.cancel();
//                    if(response.isSuccessful()) {
//                        Log.i("got doc1c ", response.body().toString());
//
//                    } else {
//                        Log.d("error", "some error");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Doc1c> call, Throwable t) {
//                    mProgressDialog.cancel();
//                    if(t.getMessage() != null) {
//                        Log.d("error", t.getMessage());
//                    } else {
//                        Log.d("error", t.toString());
//                    }
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle("Ошибка загрузки данных")
//                            .setMessage("Что-то пошло не так: " + t.getClass() + " -> " + t.getMessage())
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            }).setIcon(android.R.drawable.ic_dialog_alert).show();
//                }
//            });

            Intent intent = new Intent(this, Doc1cActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    private class GetNomenklaturaTask extends AsyncTask<Void, Void, NomenklaturaDTO[]> {
//
//        @Override
//        protected NomenklaturaDTO[] doInBackground(Void... params) {
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
//                ResponseEntity<NomenklaturaDTO[]> response = template.exchange(Constants.URL.GET_REMIND, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), NomenklaturaDTO[].class);
//                return response.getBody();
//            } catch (HttpClientErrorException e) {
//                Log.e("RemindMe", e.getLocalizedMessage(), e);
//                return new NomenklaturaDTO[]{new NomenklaturaDTO("HttpClientErrorException")};
//            } catch (ResourceAccessException e) {
//                Log.e("RemindMe", e.getLocalizedMessage(), e);
//                return new NomenklaturaDTO[]{new NomenklaturaDTO("ResourceAccessException")};
//            }
//
//
//            //return template.getForObject(Constants.URL.GET_REMIND, RemindDTO.class);
//            //return null;
//        }
//
//        @Override
//        protected void onPostExecute(NomenklaturaDTO[] remindDTOs) {
//            progressBar.setVisibility(View.GONE);
//            list.clear();
//            for (NomenklaturaDTO dto : remindDTOs) {
//                Log.i("RemindMe", dto.getTitle());
//                list.add(dto);
//            }
//
//            adapter.setNomenList(list);
//        }
//    }
}
