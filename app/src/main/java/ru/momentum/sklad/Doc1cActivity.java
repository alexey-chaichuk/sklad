package ru.momentum.sklad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ru.momentum.sklad.dto.Doc1c;

public class Doc1cActivity extends AppCompatActivity implements Doc1cItemFragment.OnListFragmentInteractionListener {

    ConstraintLayout doc1cmain_layout;

    private final static String SCAN_ACTION = "urovo.rcv.message";
    private ScanManager mScanManager;
    private boolean isScaning = false;
    private String barcodeStr;
    private boolean needToSetBarcode = false;
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
            Log.i("barcode_sklad", "----codetype-- " + barcodeType);
            barcodeStr = new String(barcode, 0, barcodelen);
            Log.i("barcode_sklad", barcodeStr);

            new AlertDialog.Builder(context)
                    .setTitle("Неизвестный штрихкод")
                    .setMessage("Выберите номенклатуру из документа для штрихкода: " + barcodeStr)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

            needToSetBarcode = true;
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
    public void onResume() {
        Log.i("sklad_info", "onResume fragment doc1c");
        super.onResume();
        initScan();
        //showScanResult.setText("");
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }

    @Override
    public void onPause() {
        Log.i("sklad_info", "onPause fragment doc1c");
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
        setContentView(R.layout.activity_doc1c);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        doc1cmain_layout = findViewById(R.id.doc1cmain_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Doc1c.Doc1cGoods item) {
        if(needToSetBarcode) {
            item.setBarcode(barcodeStr);
            Doc1cItemFragment frag = (Doc1cItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1cdoc);
            if(frag != null && frag.isAdded()) {
                frag.getAdapter().notifyDataSetChanged();
                frag.getAdapter().sendUpdateToServer(item);
            }
            needToSetBarcode = false;
        } else {
            Snackbar.make(doc1cmain_layout, "Item selected " + item.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onListFragmentAlert(String caption, String message) {
        new AlertDialog.Builder(this)
                .setTitle(caption)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
