package ru.momentum.sklad.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.momentum.sklad.R;
import ru.momentum.sklad.dto.Doc1c;

/**
 * Created by chaichukau on 13.03.18.
 */

public class Doc1cRecyclerAdapter extends RecyclerView.Adapter<Doc1cRecyclerAdapter.ViewHolder> {

    private static Doc1c doc1c;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.doc1c_cardview, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.tv_doc1c.setText(doc1c.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView tv_doc1c;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_doc1c = (TextView) itemView.findViewById(R.id.tv_doc1c);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
