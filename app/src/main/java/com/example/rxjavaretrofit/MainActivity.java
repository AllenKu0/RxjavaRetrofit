package com.example.rxjavaretrofit;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private TextView tx1;
    private MyAPIService myAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx1 = (TextView)findViewById(R.id.tx1);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())          //Gson轉換資料
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   //用RxJava
                .build();
        myAPIService = retrofit.create(MyAPIService.class);
        //getPost();
        createPost();
    }
    public void getPost(){
        Observable<Post> post = myAPIService.getPosts();
        post.subscribeOn(Schedulers.io())                    //工作的執行緒，rx的background thread，適合用來處理網路連線、資料庫存取這些不使用CPU運算的作業
                .observeOn(AndroidSchedulers.mainThread())   //發射結果的執行緒，rx的ui thread
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Post post) {

                        String content = "";
                        content+= "ID: "+post.getId()+"\n";
                        content+= "User ID: "+post.getUserId()+"\n";
                        content+= "Title: "+post.getTitle()+"\n";
                        content+= "Text: "+post.getText()+"\n\n";

                        tx1.setText(content);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "请求失敗");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "请求成功");
                    }
                });

    }
    public void createPost(){
        Post post = new Post(23,"New Title","New Text");
        Observable<Response<Post>> observable = myAPIService.createPost(post);
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<Response<Post>>() {
                      @Override
                      public void onSubscribe(@NonNull Disposable d) {

                      }

                      @Override
                      public void onNext(@NonNull Response<Post> postResponse) {
                          Post result = postResponse.body();
                          String content = "";
                          content +="Code: " + postResponse.code() + "\n";
                          content +="ID: " + result.getId()+ "\n";
                          content +="User ID: " + result.getUserId()+ "\n";
                          content +="Title: " + result.getTitle()+ "\n";
                          content +="Text: " + result.getText()+ "\n";

                          tx1.setText(content);
                      }

                      @Override
                      public void onError(@NonNull Throwable e) {

                      }

                      @Override
                      public void onComplete() {
                          //完成時進入
                      }
                  });
    }
}