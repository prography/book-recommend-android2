package org.techtown.just;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Data> dataArrayList = new ArrayList<>();
//    public MyRecyclerViewAdapter() {
//
//    }
    Retrofit retrofit;
    ApiService apiService;
    Data data;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //XML 가져오는 부분
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).build();
        apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> comment = apiService.getComment(1);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.v("Test", result); //받아온 데이터
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        data = new Data();
                        for (int i = 0 ; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            data.postId = jsonObject.getInt("postId");
                            data.id = jsonObject.getInt("id");
                            data.name = jsonObject.getString("name");
                            data.mail = jsonObject.getString("mail");
                            data.body = jsonObject.getString("body");
                            dataArrayList.add(data);
                            Log.v("Test", jsonObject.toString());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //데이터를 넣어주는 부분. 바인딩하는 부분


        ((RowCell) holder).str.setText(dataArrayList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        //카운터
        return dataArrayList.size();
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
