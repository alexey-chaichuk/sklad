package ru.momentum.sklad;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.momentum.sklad.dto.Doc1c;
import ru.momentum.sklad.dummy.DummyContent;
import ru.momentum.sklad.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Doc1cItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyDoccItemRecyclerViewAdapter adapter;
    private ProgressDialog mProgressDialog;
    private List<Doc1c.Doc1cGoods> listGoods = new ArrayList<>();

//    private final static String SCAN_ACTION = "urovo.rcv.message";
//    private ScanManager mScanManager;
//    private boolean isScaning = false;
//    private String barcodeStr;
//    private boolean needToSetBarcode = false;
//    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // TODO Auto-generated method stub
//            isScaning = false;
//            //soundpool.play(soundid, 1, 1, 0, 0, 1);
//            //showScanResult.setText("");
//            //mVibrator.vibrate(100);
//
//            byte[] barcode = intent.getByteArrayExtra("barcode");
//            int barcodelen = intent.getIntExtra("length", 0);
//            byte barcodeType = intent.getByteExtra("barcodeType", (byte) 0);
//            Log.i("barcode_sklad", "----codetype-- " + barcodeType);
//            barcodeStr = new String(barcode, 0, barcodelen);
//            Log.i("barcode_sklad", barcodeStr);
//
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Неизвестный штрихкод")
//                    .setMessage("Выберите номенклатуру из документа для штрихкода: " + barcodeStr)
//                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
//
//            needToSetBarcode = true;
//        }
//    };
//
//
//    private void initScan() {
//        mScanManager = new ScanManager();
//        mScanManager.openScanner();
//
//        mScanManager.switchOutputMode( 0);
//        //soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
//        //soundid = soundpool.load("/etc/Scan_new.ogg", 1);
//    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Doc1cItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Doc1cItemFragment newInstance(int columnCount) {
        Doc1cItemFragment fragment = new Doc1cItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

//    @Override
//    public void onResume() {
//        Log.i("sklad_info", "onResume fragment doc1c");
//        super.onResume();
//        initScan();
//        //showScanResult.setText("");
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(SCAN_ACTION);
//        getActivity().registerReceiver(mScanReceiver, filter);
//    }
//
//    @Override
//    public void onPause() {
//        Log.i("sklad_info", "onPause fragment doc1c");
//        super.onPause();
//        if(mScanManager != null) {
//            mScanManager.stopDecode();
//            isScaning = false;
//            getActivity().unregisterReceiver(mScanReceiver);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doccitem_list, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Идет загрузка из 1с ...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        App.getApi().findPTU().enqueue(new Callback<Doc1c>() {
            @Override
            public void onResponse(Call<Doc1c> call, Response<Doc1c> response) {
                mProgressDialog.cancel();
                if(response.isSuccessful()) {
                    Log.i("got doc1c ", response.body().toString());

                    Doc1c doc = response.body();
                    List<DummyItem> list = new ArrayList<DummyItem>();
                    listGoods = doc.getGoods();
                    int cnt = 0;
                    for (Doc1c.Doc1cGoods item : doc.getGoods()) {
                        list.add(new DummyItem(String.valueOf(++cnt), item.toString(), "desc"));
                    }
                    Log.i("got doc1c - list - ", list.toString());
                    adapter.setmValues(listGoods);

                } else {
                    Log.d("error", "some error");
                }
            }

            @Override
            public void onFailure(Call<Doc1c> call, Throwable t) {
                mProgressDialog.cancel();
                if(t.getMessage() != null) {
                    Log.d("error", t.getMessage());
                } else {
                    Log.d("error", t.toString());
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("Ошибка загрузки данных")
                        .setMessage("Что-то пошло не так: " + t.getClass() + " -> " + t.getMessage())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //recyclerView.setAdapter(new MyDoccItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));

            adapter = new MyDoccItemRecyclerViewAdapter(listGoods, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    public MyDoccItemRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Doc1c.Doc1cGoods item);
        void onListFragmentAlert(String caption, String message);
    }
}
