package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.model.BookInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<BookInfo> BookInfoList;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<BookInfo> BookInfoList)
    {
        this.mContext = mContext;
        this.BookInfoList = BookInfoList;
        //this.itemLayout = itemLayout;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcview_recommenditem , parent,false);

        return new ViewHolder(v);
    }

    /**
     * 넘겨 받은 데이터를 화면에 출력하는 역할
     * 재활용 되는 뷰가 호출하여 실행되는 메소드
     * 뷰 홀더를 전달하고 어댑터는 position 의 데이터를 결합
     * @param viewHolder
     * @param position
     */

    Bitmap bitmap;

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

//        ListViewItemData item = listViewItems.get(position);

        viewHolder.ITEM_BOOKNAME.setText(BookInfoList.get(position).getBook_name());
//        viewHolder.ITEM_IMG.setImageBitmap(BookInfoList.get(position).getThumbnail());
        //viewHolder.ITEM_IMG.setImageBitmap();
//        viewHolder.ITEM_IMG.setImageURI(Uri.parse("https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F521345%3Ftimestamp%3D20190123155507"));
        viewHolder.ITEM_AUTHOR.setText(BookInfoList.get(position).getAuthor());
        viewHolder.ITEM_TAG.setText(BookInfoList.get(position).getTags());


        setImageSrc(viewHolder.ITEM_IMG, position);
//1.25 am 05:18


        // 값 설정 ( set )
        //Here it is simply write onItemClick listener here
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                //like, read버튼 클릭시 이벤트 처리 here


                Intent intent = new Intent(v.getContext(),BookDetailActivity.class);

                intent.putExtra("isbn",BookInfoList.get(position).getIsbn());
                intent.putExtra("book_thumbnail",BookInfoList.get(position).getThumbnail());
                intent.putExtra("book_name",BookInfoList.get(position).getBook_name());
                intent.putExtra("book_author",BookInfoList.get(position).getAuthor());
                intent.putExtra("book_content",BookInfoList.get(position).getContents());
                intent.putExtra("book_country",BookInfoList.get(position).getCountry());
                intent.putExtra("book_tags",BookInfoList.get(position).getTags());

                mContext.startActivity(intent);

                Toast.makeText(context, position +"번째 아이템 클릭", Toast.LENGTH_LONG).show();
            }
        });
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


    class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        ImageView ITEM_IMG, ITEM_LIKE, ITEM_READ;
        TextView ITEM_BOOKNAME, ITEM_AUTHOR, ITEM_TAG;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ITEM_IMG = itemView.findViewById(R.id.item_img);
            ITEM_LIKE = itemView.findViewById(R.id.item_like);
            ITEM_READ = itemView.findViewById(R.id.item_read);
            ITEM_BOOKNAME = itemView.findViewById(R.id.item_bookname);
            ITEM_AUTHOR = itemView.findViewById(R.id.item_author);
            ITEM_TAG = itemView.findViewById(R.id.item_tag);

            // 레이아웃 객체화 findViewById

        }

    }

    @Override // 데이터 수 반환
    public int getItemCount() {
        return BookInfoList.size();
    }

}