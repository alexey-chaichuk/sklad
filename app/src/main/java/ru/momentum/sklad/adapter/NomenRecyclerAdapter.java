package ru.momentum.sklad.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import ru.momentum.sklad.NomenDetailActivity;
import ru.momentum.sklad.R;
import ru.momentum.sklad.Tab2Activity;
import ru.momentum.sklad.dto.NomenklaturaDTO;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by chaichukau on 25.01.18.
 */

public class NomenRecyclerAdapter extends RecyclerView.Adapter<NomenRecyclerAdapter.ViewHolder> {

    private static List<NomenklaturaDTO> nomenList;

    public NomenRecyclerAdapter(List<NomenklaturaDTO> nomenList) {
        this.nomenList = nomenList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.nomen_cardview,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(nomenList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return nomenList.size();
    }

    public void setNomenList(List<NomenklaturaDTO> nomenList) {
        this.nomenList = nomenList;
        notifyDataSetChanged();
    }

    public List<NomenklaturaDTO> getNomenList() {
        return nomenList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textView;
        //CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.textView2);
            //cv = (CardView) itemView.findViewById(R.id.cv);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick " + getLayoutPosition() + ": " + textView.getText().toString());
            Context ctx = view.getContext();
            Intent intent = new Intent(ctx, NomenDetailActivity.class);
            intent.putExtra("nomenclature", (Parcelable) nomenList.get(getLayoutPosition()));
            ctx.startActivity(intent);
        }
    }

}
