package org.techtown.just;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.techtown.just.model.Post;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Post> postArrayList = new ArrayList<>();
    private OnClickListener clickListener;

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void addComments(List<Post> posts) {
        postArrayList.addAll(posts);
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

        final Post post = postArrayList.get(position);

        ((RowCell) holder).str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.str:
                        clickListener.onPostClick(post);
                        break;
                }
            }
        });

        ((RowCell) holder).str.setText(postArrayList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        //카운터
        return postArrayList.size();
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
