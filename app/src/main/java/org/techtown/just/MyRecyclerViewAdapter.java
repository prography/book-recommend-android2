package org.techtown.just;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Comment> commentArrayList = new ArrayList<>();


    public void addComments(List<Comment> comments) {
        commentArrayList.addAll(comments);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //XML 가져오는 부분
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);



        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //데이터를 넣어주는 부분. 바인딩하는 부분


        ((RowCell) holder).str.setText(commentArrayList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        //카운터
        return commentArrayList.size();
    }

    //소스코드 절약해주는 부분 static 넣으면 더 좋음
    private static class RowCell extends RecyclerView.ViewHolder {
        TextView str;
        public RowCell(View view) {
            super(view);
            str = (TextView)view.findViewById(R.id.str);
        }
    }
}
