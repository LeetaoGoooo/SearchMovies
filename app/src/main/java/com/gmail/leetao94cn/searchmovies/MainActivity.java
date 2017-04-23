package com.gmail.leetao94cn.searchmovies;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gmail.leetao94cn.searchmovies.MovieSearch.PianYuan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;

    @BindView(R.id.movie_name)
    EditText mMovieName;
    @BindView(R.id.search_btn)
    ButtonRectangle mSearchBtn;
    @BindView(R.id.movies_list)
    ListView mMoviesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.search_btn)
    public void onClick() {
        Log.i("click", "onclick");
        String moviename = mMovieName.getText().toString();
        new SearchAsynctask().execute(moviename);
    }

    public void BindListData(final ArrayList arrayList) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.item, new String[]{"downLoadImage", "movieUrl"}, new int[]{R.id.downLoadImage, R.id.movieUrl});
        mMoviesList = new ListView(this);
        mMoviesList.setAdapter(simpleAdapter);
        setContentView(mMoviesList);
        mMoviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                Log.i("item", "click " + i + " item");
                HashMap<String, Object> map = new HashMap<String, Object>();
                map = (HashMap) arrayList.get(i);
                String movieUrl = map.get("movieUrl").toString();
                Uri uri = Uri.parse(movieUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    class SearchAsynctask extends AsyncTask<String, Void, List> {


        private PianYuan mPianYuan;

        @Override
        protected List doInBackground(String... strings) {
            mPianYuan = new PianYuan();
            Log.i("movie", strings[0]);
            return mPianYuan.getMovieSoureList(strings[0]);
        }

        protected void onPostExecute(List urlList) {
            super.onPostExecute(urlList);
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < urlList.size(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("downLoadImage", R.drawable.ic_down);
                map.put("movieUrl", urlList.get(i));
                listItem.add(map);
            }
            BindListData(listItem);
        }
    }

}
