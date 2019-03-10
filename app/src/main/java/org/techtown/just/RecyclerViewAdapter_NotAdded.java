package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.model.BookFlag;
import org.techtown.just.model.BookInfo_Added;
import org.techtown.just.model.BookInfo_NotAdded;
import org.techtown.just.model.LocalStore;
import org.techtown.just.model.Tag;
import org.techtown.just.model.TagNames;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.just.base.BaseApplication.getLocalStore;
import static org.techtown.just.base.BaseApplication.getNetworkManager;

public class RecyclerViewAdapter_NotAdded extends RecyclerView.Adapter<RecyclerViewAdapter_NotAdded.ViewHolder> {

    private List<BookInfo_NotAdded> bookInfoNotAddedList;
    private Context mContext;
    private TagNames tagNames;
    private BookListListener listener;

    public interface BookListListener{
        void saveFlag(int position, BookInfo_NotAdded bookInfoNotAdded, int like, int read);
    }

    //이건 뭐지
    public void updateBookInfo(int position, BookInfo_NotAdded bookInfoNotAdded){
        bookInfoNotAddedList.set(position, bookInfoNotAdded);
        notifyItemChanged(position);
    }
    public void setBookListListener(BookListListener listener){
        this.listener = listener;
    }


    public RecyclerViewAdapter_NotAdded(Context mContext, List<BookInfo_NotAdded> bookInfoNotAddedList, TagNames tagNames) {
        this.mContext = mContext;
        //this.bookInfoAddedList = bookInfoAddedList;

        this.bookInfoNotAddedList = new ArrayList<>();
        this.bookInfoNotAddedList.addAll(bookInfoNotAddedList);
        this.tagNames = tagNames;
        //this.itemLayout = itemLayout;
    }

    //책 정보가 추가되면 (flag)
    public void addBookInfo(BookInfo_NotAdded bookInfoNotAdded){
        bookInfoNotAddedList.add(bookInfoNotAdded);
        notifyDataSetChanged();
    }
//
//    /**
//     * 레이아웃을 만들어서 Holer에 저장
//     *  뷰 홀더를 생성하고 뷰를 붙여주는 부분
//     * @param viewGroup
//     * @param viewType
//     * @return
//     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_notadded, parent, false);

        return new ViewHolder(v);
    }

    /**
     * 넘겨 받은 데이터를 화면에 출력하는 역할
     * 재활용 되는 뷰가 호출하여 실행되는 메소드
     * 뷰 홀더를 전달하고 어댑터는 position 의 데이터를 결합
     *
     * @param viewHolder
     * @param position
     */

    Bitmap bitmap;

    //서버에 올리기 위한 like, read flag 초기 설정
    int like = 0, read = 0;

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.ITEM_BOOKNAME.setText(bookInfoNotAddedList.get(position).getBook_name());
        viewHolder.ITEM_AUTHOR.setText(bookInfoNotAddedList.get(position).getFullAuthor());
        setImageSrc(viewHolder.ITEM_IMG, position);

        //추가하기 버튼 누르면
        viewHolder.btnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), AddBookInfoActivity.class);
                intent.putExtra("bookInfoNotAdded", bookInfoNotAddedList.get(position));
                mContext.startActivity(intent);
            }
        });

        viewHolder.btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), AddBookInfoActivity.class);
                intent.putExtra("bookInfoNotAdded", bookInfoNotAddedList.get(position));
                mContext.startActivity(intent);
            }
        });




    }


    public void setImageSrc(ImageView imageView, final int position) {
        //ImageView url 설정
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(bookInfoNotAddedList.get(position).getThumbnail());

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


    class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        ImageView ITEM_IMG;
        TextView ITEM_BOOKNAME, ITEM_AUTHOR;
        Button btnCountry, btnTag;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ITEM_IMG = itemView.findViewById(R.id.item_img);
            ITEM_BOOKNAME = itemView.findViewById(R.id.item_bookname);
            ITEM_AUTHOR = itemView.findViewById(R.id.item_author);
            btnCountry = itemView.findViewById(R.id.btn_country);
            btnTag = itemView.findViewById(R.id.btn_tag);
        }

    }

    @Override // 데이터 수 반환
    public int getItemCount() {
        return bookInfoNotAddedList.size();
    }

}