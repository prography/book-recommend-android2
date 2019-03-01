package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.techtown.just.model.BookInfo;
import org.techtown.just.model.TagNames;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private String[] mData;
//    private LayoutInflater mInflater;
    private MyRecyclerViewClickListener mListener;
    private List<BookInfo> bookInfoList;
    private Context mContext;
    Bitmap bitmap;
    TagNames tagNames;

    public interface MyRecyclerViewClickListener{
        //item 선택 클릭시
        void onItemClicked(int position);
    }

    public void setOnClickListener(MyRecyclerViewClickListener listener){
        mListener = listener;
    }

    //각각의 아이템 레퍼런스를 저장할 뷰 홀더 클래스 *리사이클러뷰홀더를 반드시 상속!
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public final View mView;
        ImageView my_book_img;
        //TextView my_book_name;

        MyViewHolder(View v){
            super(v);
            mView = v;
            my_book_img = v.findViewById(R.id.myBookList);
//            my_book_name = v.findViewById(R.id.myBookList);
        }
    }

//    MyAdapter(ArrayList<BookInfo> bookInfoList){
//        this.bookInfoList = bookInfoList;
//    }

    MyAdapter(Context mContext, List<BookInfo> BookInfoList, TagNames tagNames){
        this.mContext = mContext;
        this.bookInfoList = BookInfoList;
        this.tagNames = tagNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcview_mybook , parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        //id값에 따라 image/name 설정
        //myViewHolder.my_book_img.setImageResource(BookInfoArrayList.get(position).id);
//        myViewHolder.my_book_name.setText(BookInfoArrayList.get(position).id);
        //TODO::책 img 로 변경 필요.
        //myViewHolder.my_book_name.setText(bookInfoList.get(position).book_name);
        setImageSrc(myViewHolder.my_book_img, position);


        final String book_name = bookInfoList.get(position).getBook_name();
        final String book_author = bookInfoList.get(position).getAuthor();
        final String book_content = bookInfoList.get(position).getContents();
        final String book_thumbnail = bookInfoList.get(position).getThumbnail();
        final String book_country = bookInfoList.get(position).getCountry();
        final String book_tags = bookInfoList.get(position).getTags();
        final String isbn = bookInfoList.get(position).getIsbn();

        myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                intent.putExtra("tagNames", tagNames);

                mContext.startActivity(intent);

                Toast.makeText(context, position + "번째 아이템 클릭", Toast.LENGTH_LONG).show();
            }
        });

        //recyclerview item clicklistener
        myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                //like, read버튼 클릭시 이벤트 처리 here

                Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                //              intent.putExtra("bookInfo", (Parcelable) bookInfoList.get(position));

                intent.putExtra("isbn", bookInfoList.get(position).getIsbn());
                intent.putExtra("tagNames", tagNames);

                mContext.startActivity(intent);

                Toast.makeText(context, bookInfoList.get(position).getBook_name() + " 클릭", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookInfoList.size();
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
                    URL url = new URL(bookInfoList.get(position).getThumbnail());

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
