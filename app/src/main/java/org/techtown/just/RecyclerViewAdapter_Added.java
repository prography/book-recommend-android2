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

import org.techtown.just.model.BookFlag;
import org.techtown.just.model.BookInfo_Added;
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

public class RecyclerViewAdapter_Added extends RecyclerView.Adapter<RecyclerViewAdapter_Added.ViewHolder> {

    private List<BookInfo_Added> bookInfoAddedList;
    private Context mContext;
    private TagNames tagNames;
    private BookListListener listener;

    public interface BookListListener{
        void saveFlag(int position, BookInfo_Added bookInfoAdded, int like, int read);
    }
    public void updateBookInfo(int position, BookInfo_Added bookInfoAdded){
        bookInfoAddedList.set(position, bookInfoAdded);
        notifyItemChanged(position);
    }
    public void setBookListListener(BookListListener listener){
        this.listener = listener;
    }


    public RecyclerViewAdapter_Added(Context mContext, List<BookInfo_Added> bookInfoAddedList, TagNames tagNames) {
        this.mContext = mContext;
        //this.bookInfoAddedList = bookInfoAddedList;

        this.bookInfoAddedList = new ArrayList<>();
        this.bookInfoAddedList.addAll(bookInfoAddedList);
        this.tagNames = tagNames;
        //this.itemLayout = itemLayout;
    }





    //책 정보가 추가되면 (flag)
    public void addBookInfo(BookInfo_Added bookInfoAdded){
        bookInfoAddedList.add(bookInfoAdded);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_added, parent, false);

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

//        ListViewItemData item = listViewItems.get(position);

        viewHolder.ITEM_BOOKNAME.setText(bookInfoAddedList.get(position).getBook_name());
        viewHolder.ITEM_AUTHOR.setText(bookInfoAddedList.get(position).getAuthor());
        viewHolder.ITEM_COUNTRY.setText(bookInfoAddedList.get(position).getCountry());

        String s = getTagNames(bookInfoAddedList.get(position).getTags(), tagNames);
        viewHolder.ITEM_TAG.setText(s);
        setImageSrc(viewHolder.ITEM_IMG, position);

        setBookFlags(viewHolder, position);

        // like clicklistener
        viewHolder.ITEM_LIKE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                if(viewHolder.ITEM_LIKE.isSelected()) {
                    //좋아요 취소
                    Toast.makeText(context, "\"" + bookInfoAddedList.get(position).getBook_name() + "\"" + " 좋아요 취소", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_LIKE.setSelected(false);
                    viewHolder.ITEM_LIKE.setImageResource(R.drawable.ic_like_empty);
                    like = 0;

                }else {
                    //좋아요
                    Toast.makeText(context, "\"" + bookInfoAddedList.get(position).getBook_name() + "\"" + " 좋다!", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_LIKE.setSelected(true);
                    viewHolder.ITEM_LIKE.setImageResource(R.drawable.ic_like_full);
                    like = 1;
                }

                //read 가져오고 서버에 저장
                if (viewHolder.ITEM_READ.isSelected() == true)
                    read = 1;
                else
                    read = 0;

                //서버에 저장
                saveBookStatus(viewHolder, position, like, read);
            }
        });

        // read clicklistener
        viewHolder.ITEM_READ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                if(viewHolder.ITEM_READ.isSelected()) {
                    //읽음 취소
                    Toast.makeText(context, "아맞다 \"" + bookInfoAddedList.get(position).getBook_name() + "\"" + "안읽었지...ㅎ", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_READ.setSelected(false);
                    viewHolder.ITEM_READ.setImageResource(R.drawable.ic_check);
                    read = 0;
                }else{
                    Toast.makeText(context, "\"" + bookInfoAddedList.get(position).getBook_name() + "\"" + "을(를) 읽었다!", Toast.LENGTH_LONG).show();
                    viewHolder.ITEM_READ.setSelected(true);
                    viewHolder.ITEM_READ.setImageResource(R.drawable.ic_checked);
                    read = 1;
                }

                //like 가져오고 서버에 저장
                if (viewHolder.ITEM_LIKE.isSelected() == true)
                    like = 1;
                else
                    like = 0;

                //서버에 저장
                saveBookStatus(viewHolder, position, like, read);


            }
        });



        //recyclerview item clicklistener
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                //like, read버튼 클릭시 이벤트 처리 here

                Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                //              intent.putExtra("bookInfoAdded", (Parcelable) bookInfoAddedList.get(position));

                intent.putExtra("isbn", bookInfoAddedList.get(position).getIsbn());
                intent.putExtra("tagNames", tagNames);

                mContext.startActivity(intent);

                Toast.makeText(context, bookInfoAddedList.get(position).getBook_name() + " 클릭", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBookStatus(final ViewHolder viewHolder, final int position, int like, int read) {
        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);
        String userId = getLocalStore().getStringValue(LocalStore.UserId);

        String isbn = bookInfoAddedList.get(position).getIsbn();

        Call<ResponseBody> call = getNetworkManager().getBookApi().putBookFlags(isbn, userId, like, read, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(mContext, "put success", Toast.LENGTH_SHORT).show();
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(mContext, "catch", Toast.LENGTH_SHORT).show();
                }
//                List<BookFlag> status = response.body();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBookFlags(final ViewHolder viewHolder, final int position) {
        //bookFlag 가져오기
        String userId = getLocalStore().getStringValue(LocalStore.UserId);
        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);

        final String isbn = bookInfoAddedList.get(position).getIsbn();

        Call<List<BookFlag>> call = getNetworkManager().getBookApi().getBookFlags(isbn, userId, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<List<BookFlag>>() {
            @Override
            public void onResponse(Call<List<BookFlag>> call, Response<List<BookFlag>> response) {
                try {
                    List<BookFlag> bookFlags = response.body();

                    int like = bookFlags.get(0).getBe_interested();
                    if (like == 1) {
                        viewHolder.ITEM_LIKE.setSelected(true);
                        viewHolder.ITEM_LIKE.setImageResource(R.drawable.ic_like_full);
                    }
                    else {
                        viewHolder.ITEM_LIKE.setSelected(false);
                        viewHolder.ITEM_LIKE.setImageResource(R.drawable.ic_like_empty);
                    }

                    int read = bookFlags.get(0).getHad_read();
                    if (read == 1) {
                        viewHolder.ITEM_READ.setSelected(true);
                        viewHolder.ITEM_READ.setImageResource(R.drawable.ic_checked);
                    }
                    else {
                        viewHolder.ITEM_READ.setSelected(false);
                        viewHolder.ITEM_READ.setImageResource(R.drawable.ic_check);
                    }
                } catch (IndexOutOfBoundsException e) {
                    //post안된경우 post로 0 0 설정해주면 됨~
                    postBookFlags(isbn);
                    setBookFlags(viewHolder, position);
                }


            }

            @Override
            public void onFailure(Call<List<BookFlag>> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postBookFlags(String isbn) {
        String userId = getLocalStore().getStringValue(LocalStore.UserId);
        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);

        Call<ResponseBody> call = getNetworkManager().getBookApi().postBookFlags(isbn, userId, 0, 0, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
                    URL url = new URL(bookInfoAddedList.get(position).getThumbnail());

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
        TextView ITEM_BOOKNAME, ITEM_AUTHOR, ITEM_TAG, ITEM_COUNTRY;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ITEM_IMG = itemView.findViewById(R.id.item_img);
            ITEM_LIKE = itemView.findViewById(R.id.item_like);
            ITEM_READ = itemView.findViewById(R.id.item_read);
            ITEM_BOOKNAME = itemView.findViewById(R.id.item_bookname);
            ITEM_AUTHOR = itemView.findViewById(R.id.item_author);
            ITEM_TAG = itemView.findViewById(R.id.item_tag);
            ITEM_COUNTRY = itemView.findViewById(R.id.item_country);

            // 레이아웃 객체화 findViewById

        }

    }

    @Override // 데이터 수 반환
    public int getItemCount() {
        return bookInfoAddedList.size();
    }

}