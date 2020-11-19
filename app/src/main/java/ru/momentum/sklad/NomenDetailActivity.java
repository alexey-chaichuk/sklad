package ru.momentum.sklad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.momentum.sklad.dto.NomenklaturaDTO;

public class NomenDetailActivity extends AppCompatActivity {

    TextView nomenCode;
    NomenklaturaDTO nomen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomen_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            nomen = (NomenklaturaDTO) savedInstanceState.getSerializable("nomen");
        } else {
            nomen = (NomenklaturaDTO) getIntent().getSerializableExtra("nomenclature");
        }

        TextView nomenTitle = (TextView) findViewById(R.id.nomenTitle);
        nomenCode = (TextView) findViewById(R.id.nomenCode);
        TextView nomenArticle = (TextView) findViewById(R.id.nomenArticle);

        if(nomen != null) {
            nomenTitle.setText(nomen.getTitle());
            nomenCode.setText(nomen.getCode());
            nomenArticle.setText(nomen.getArticle());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button addBarcode = (Button) findViewById(R.id.addBarcode);
        addBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddBarcode = new Intent(view.getContext(), AddBarcodeActivity.class);
                intentAddBarcode.putExtra("nomenCode", nomenCode.getText());
                view.getContext().startActivity(intentAddBarcode);
            }
        });

        App.getApi().getPhoto().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("sklad", String.valueOf(response.body()));
                Log.i("sklad", String.valueOf(response.errorBody()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("nomen", nomen);
    }
}
