package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.just.model.BookInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private String[] mData;
//    private LayoutInflater mInflater;
    private MyRecyclerViewClickListener mListener;
    private List<BookInfo> BookInfoList;
    private Context mContext;
    Bitmap bitmap;

    public interface MyRecyclerViewClickListener{
        //item 선택 클릭시
        void onItemClicked(int position);
    }

    public void setOnClickListener(MyRecyclerViewClickListener listener){
        mListener = listener;
    }

    //각각의 아이템 레퍼런스를 저장할 뷰 홀더 클래스 *리사이클러뷰홀더를 반드시 상속!
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView my_book_img;
        //TextView my_book_name;

        MyViewHolder(View v){
            super(v);
            my_book_img = v.findViewById(R.id.myBookList);
//            my_book_name = v.findViewById(R.id.myBookList);
        }
    }

//    MyAdapter(ArrayList<BookInfo> BookInfoList){
//        this.BookInfoList = BookInfoList;
//    }

    MyAdapter(Context mContext, List<BookInfo> BookInfoList){
        this.mContext = mContext;
        this.BookInfoList = BookInfoList;
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
//        myViewHolder.my_book_name.setText(BookInfoArrayList.get(position).id);
        //TODO::책 img 로 변경 필요.
        //myViewHolder.my_book_name.setText(BookInfoList.get(position).book_name);
        setImageSrc(myViewHolder.my_book_img, position);


        final String book_name = BookInfoList.get(position).getBook_name();
        final String book_author = BookInfoList.get(position).getAuthor();
        final String book_content = BookInfoList.get(position).getContents();
        final String book_thumbnail = BookInfoList.get(position).getThumbnail();
        final String book_country = BookInfoList.get(position).getCountry();
        final String book_tags = BookInfoList.get(position).getTags();
        final String isbn = BookInfoList.get(position).getIsbn();


        //클릭 이벤트
        if(mListener != null){
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                    Context context = view.getContext();
                    Intent intent = new Intent(view.getContext(),BookDetailActivity.class);

                    intent.putExtra("isbn",isbn);
                    intent.putExtra("book_thumbnail",book_thumbnail);
                    intent.putExtra("book_name",book_name);
                    intent.putExtra("book_author",book_author);
                    intent.putExtra("book_content",book_content);
                    intent.putExtra("book_country",book_country);
                    intent.putExtra("book_tags",book_tags);

                    mContext.startActivity(intent);

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return BookInfoList.size();
    }

    @Override
    public int getItemViewType(int position){
        return super.getItemViewType(position);
    }


    public void setImageSrc(ImageView imageView, final int position) {
        //ImageView url 설정
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(BookInfoList.get(position).getThumbnail());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try {
            mThread.join();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
