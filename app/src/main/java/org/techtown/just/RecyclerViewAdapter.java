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
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.model.BookInfo;
import org.techtown.just.model.Tag;
import org.techtown.just.model.TagNames;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BookInfo> bookInfoList;
    private Context mContext;
    private TagNames tagNames;
    private BookListListener listener;

    public interface BookListListener{
        void saveFlag(int position, BookInfo bookInfo, int like, int read);
    }
    public void updateBookInfo(int position, BookInfo bookInfo){
        bookInfoList.set(position, bookInfo);
        notifyItemChanged(position);
    }
    public void setBookListListener(BookListListener listener){
        this.listener = listener;
    }


    public RecyclerViewAdapter(Context mContext, List<BookInfo> BookInfoList, TagNames tagNames) {
        this.mContext = mContext;
        //this.bookInfoList = BookInfoList;

        this.bookInfoList = new ArrayList<>();
        this.bookInfoList.addAll(BookInfoList);
        this.tagNames = tagNames;
        //this.itemLayout = itemLayout;
    }
    //책 정보가 추가되면 (flag)
    public void addBookInfo(BookInfo bookInfo){
        bookInfoList.add(bookInfo);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcview_recommenditem, parent, false);

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

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

//        ListViewItemData item = listViewItems.get(position);

        viewHolder.ITEM_BOOKNAME.setText(bookInfoList.get(position).getBook_name());
        viewHolder.ITEM_AUTHOR.setText(bookInfoList.get(position).getAuthor());

        String s = getTagNames(bookInfoList.get(position).getTags(), tagNames);
        viewHolder.ITEM_TAG.setText(s);
        setImageSrc(viewHolder.ITEM_IMG, position);
//1.25 am 05:18

        // 값 설정 ( set )
        int like= 0;//bookInfoList.get(position).getFlag().getBe_interested();
        int read= 0;//bookInfoList.get(position).getFlag().getHad_read();

        if(like==1)
            viewHolder.ITEM_LIKE.setSelected(true);
        else
            viewHolder.ITEM_LIKE.setSelected(false);

        if(read==1)
            viewHolder.ITEM_LIKE.setSelected(true);
        else
            viewHolder.ITEM_LIKE.setSelected(false);

        //Here it is simply write onItemClick listener here
        viewHolder.ITEM_LIKE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                if(viewHolder.ITEM_LIKE.isSelected()) {
                    //좋아요 취소
                    Toast.makeText(context, position + "좋아요 취소..", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_LIKE.setSelected(false);
                    viewHolder.ITEM_LIKE.setImageResource(R.drawable.ic_like_empty);
                    //listener.saveFlag(position, bookInfoList.get(position), 0, bookInfoList.get(position).getFlag().getBe_interested());
                }else {
                    //좋아요
                    Toast.makeText(context, position + "좋아요!", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_LIKE.setSelected(true);
                    viewHolder.ITEM_LIKE.setImageResource(R.drawable.ic_like_full);
                    //listener.saveFlag(position, bookInfoList.get(position), 1, bookInfoList.get(position).getFlag().getBe_interested() );
                }

            }
        });
        viewHolder.ITEM_READ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                if(viewHolder.ITEM_READ.isSelected()) {
                    //읽음 취소
                    Toast.makeText(context, position + "안읽은 책ㅠㅠ", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_READ.setSelected(false);
                    viewHolder.ITEM_READ.setImageResource(R.drawable.ic_check);
                    //listener.saveFlag(position, bookInfoList.get(position), bookInfoList.get(position).getFlag().getBe_interested(), 0 );
                }else{
                    Toast.makeText(context, position + "읽었어요!", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_READ.setSelected(true);
                    viewHolder.ITEM_READ.setImageResource(R.drawable.ic_checked);
                }
                    //listener.saveFlag(position, bookInfoList.get(position), bookInfoList.get(position).getFlag().getBe_interested(),1);
            }
        });

                //Here it is simply write onItemClick listener here
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                //like, read버튼 클릭시 이벤트 처리 here


                Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                //              intent.putExtra("bookInfo", (Parcelable) bookInfoList.get(position));

                intent.putExtra("isbn", bookInfoList.get(position).getIsbn());
                intent.putExtra("tagNames", tagNames);

//                intent.putExtra("booklike",like);
//                intent.putExtra("bookread",read);

//                intent.putExtra("booklike",bookInfoList.get(position).getFlag().getBe_interested());
//                intent.putExtra("bookread",bookInfoList.get(position).getFlag().getHad_read());

//                intent.putExtra("book_thumbnail", bookInfoList.get(position).getThumbnail());
//                intent.putExtra("book_name", bookInfoList.get(position).getBook_name());
//                intent.putExtra("book_author", bookInfoList.get(position).getAuthor());
//                intent.putExtra("book_content", bookInfoList.get(position).getContents());
//                intent.putExtra("book_country", bookInfoList.get(position).getCountry());
//                intent.putExtra("book_tags", bookInfoList.get(position).getAllTags());

                mContext.startActivity(intent);

                Toast.makeText(context, position + "번째 아이템 클릭", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getTagNames(String s, TagNames tagNames) {
        String booksTags[] = s.split(";");
        String str = "";
        String s1 = "", s2 = "";
        List<Tag> selectedTags = tagNames.getSelectedTags();

        for (int i = 0; i < booksTags.length; i++) {
            try {
                int toInt = Integer.parseInt(booksTags[i]);
                int flag = 0; //selectedTag와 일치한다(1) 안한다(0), default : 0
                //selectedTag와 일치하면 s1에, 아니면 s2에 넣기
                for (int j = 0; j < selectedTags.size(); j++)
                    if (toInt == selectedTags.get(j).getTag_id()) { //선택된 태그가 책정보 태그에 있으면
                        flag = 1;
                        break;
                    }

                if (flag == 1) //toInt가 selectedTag에 있으면
                    s1 += tagNames.getAllTags().get(toInt - 1).getTag_name() + " · ";
                else
                    s2 += tagNames.getAllTags().get(toInt - 1).getTag_name() + " · ";

            } catch (NumberFormatException e) {

            }
        }
        return s1 + s2;
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
        return bookInfoList.size();
    }

}