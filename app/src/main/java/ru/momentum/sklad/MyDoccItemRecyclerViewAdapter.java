package ru.momentum.sklad;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.momentum.sklad.Doc1cItemFragment.OnListFragmentInteractionListener;
import ru.momentum.sklad.dto.BarcodeDTO;
import ru.momentum.sklad.dto.Doc1c;
import ru.momentum.sklad.dummy.DummyContent.DummyItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDoccItemRecyclerViewAdapter extends RecyclerView.Adapter<MyDoccItemRecyclerViewAdapter.ViewHolder> {

    //private List<DummyItem> mValues;
    private List<Doc1c.Doc1cGoods> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyDoccItemRecyclerViewAdapter(List<Doc1c.Doc1cGoods> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setmValues(List<Doc1c.Doc1cGoods> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    public void sendUpdateToServer(Doc1c.Doc1cGoods item) {
        BarcodeDTO bdto = new BarcodeDTO(item.getBarcode(), item.getCode());
            App.getApi().AddCode(bdto).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("sklad", "response code: " + response.code());
                    if(response.code() == 200) {
                        Log.i("sklad", "update result: " + response.body());
                    } else if(response.code() == 409) {
                        try {
                            String messageText = response.errorBody().string();
                            Log.i("sklad", "update failure: " + response.message() + ": " + messageText);
                            mListener.onListFragmentAlert(response.message(), messageText);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("sklad", "update failure: " + t.getMessage());
                }
            });
        Log.i("sklad", "sending updates to server: " + item.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doccitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Doc1c.Doc1cGoods value = mValues.get(position);
        holder.mItem = value;
        //holder.mIdView.setText(value.getCode());
        holder.mIdView.setText(value.getKolvo());
        String title = value.getNomen();
        if (value.getBarcode() != "") {
            title += " (штрихкод = " + value.getBarcode() + ")";
        }
        holder.mContentView.setText(title);

        if (value != null && value.getBarcode() != "") {
            holder.mContentView.setBackgroundColor(Color.parseColor("#00f0b5"));
        } else {
            holder.mContentView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Doc1c.Doc1cGoods mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
