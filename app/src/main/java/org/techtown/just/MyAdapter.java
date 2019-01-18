package org.techtown.just;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private String[] mData;
//    private LayoutInflater mInflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageButton my_book_img;
        TextView my_book_name;

        MyViewHolder(View v){
            super(v);
            //my_book_img = v.findViewById(R.id.myList_img);
            my_book_name = v.findViewById(R.id.myList_text);
        }
    }
    private ArrayList<BookInfo> BookInfoArrayList;

    MyAdapter(ArrayList<BookInfo> BookInfoArrayList){
        this.BookInfoArrayList = BookInfoArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcview_mybook , parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        //id값에 따라 image/name 설정
        //myViewHolder.my_book_img.setImageResource(BookInfoArrayList.get(position).id);
        //myViewHolder.my_book_name.setText(BookInfoArrayList.get(position).book_name);
        myViewHolder.my_book_name.setText(BookInfoArrayList.get(position).book_name);
    }

    @Override
    public int getItemCount() {
        return BookInfoArrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        return super.getItemViewType(position);
    }

}
