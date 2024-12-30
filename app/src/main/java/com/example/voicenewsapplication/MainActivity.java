package com.example.voicenewsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//3509c8e321dc45fcadde95a212a23af9
public class MainActivity extends AppCompatActivity implements CategoryRVAdaptor.CategoryClickInterface{

    private RecyclerView newsRV, categoryRV;
    private ProgressBar loadingPB;

    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private NewsRVAdaptor newsRVAdaptor;
    private CategoryRVAdaptor categoryRVAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdaptor = new NewsRVAdaptor(articlesArrayList, this);
        categoryRVAdaptor = new CategoryRVAdaptor(categoryRVModelArrayList, this, this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdaptor);
        categoryRV.setAdapter(categoryRVAdaptor);
        getCategories();
        getNews("All");
        newsRVAdaptor.notifyDataSetChanged();
    }

    private void getCategories() {
        categoryRVModelArrayList.add(new CategoryRVModel("All","https://images.unsplash.com/photo-1475721027785-f74eccf877e2?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://plus.unsplash.com/premium_photo-1661963212517-830bbb7d76fc?q=80&w=2886&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science","https://images.unsplash.com/photo-1564325724739-bae0bd08762c?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports","https://images.unsplash.com/photo-1552674605-db6ffd4facb5?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("General","https://images.unsplash.com/photo-1451187580459-43490279c0fa?q=80&w=2944&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business","https://plus.unsplash.com/premium_photo-1663100420388-6d2e29a256b8?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment","https://images.unsplash.com/photo-1499364615650-ec38552f4f34?q=80&w=2804&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health","https://images.unsplash.com/photo-1447452001602-7090c7ab2db3?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVAdaptor.notifyDataSetChanged();
    }

    private void getNews(String category) {

        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
//        StringBuilder catergoryURL = new StringBuilder("https://newsapi.org/v2/top-headlines?country=in&cateogry=");
//        catergoryURL.append(category);
//        catergoryURL.append("&apikey=3509c8e321dc45fcadde95a212a23af9");
//        catergoryURL = catergoryURL.toString();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apikey=3509c8e321dc45fcadde95a212a23af9";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=published&language=en&apikey=3509c8e321dc45fcadde95a212a23af9";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if(category.equals("All")) {
            call = retrofitAPI.getAllNews(url);
        }
        else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for(int i=0; i<articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()));
                }
                newsRVAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to get News", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();
        getNews(category);
    }
}