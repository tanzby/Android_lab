package pub.tanzby.lab9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.tanzby.lab9.factory.ServiceFactory;
import pub.tanzby.lab9.factory.StatusBarFactory;
import pub.tanzby.lab9.model.Repos;
import retrofit2.Retrofit;

public class ReposActivity extends Activity {

    ListView lv_repos_list;
    Observer<List<Repos>>  repos_obs;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        init();
        Intent intent = getIntent();
        String s = intent.getStringExtra("username");
        getrepos(s);
    }

    private void init()
    {
        StatusBarFactory.immerseStatusBar(this);

        lv_repos_list = findViewById(R.id.lv_repos_list);
        loading       = findViewById(R.id.progressBar_load_repos);
        loading.setVisibility(View.INVISIBLE);

        repos_obs = new Observer<List<Repos>>() {
            @Override
            public void onSubscribe(Disposable s) {
                Log.i("repos_obs","Subscribe");
                loading.setVisibility(View.VISIBLE);
                lv_repos_list.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(List<Repos> repos) {
                Log.i("repos_obs","onNext size = "+repos.size());
                List<HashMap<String,String>> data = new ArrayList<>(repos.size());
                for (Repos r: repos)
                {
                    HashMap<String,String> m = new HashMap<>();
                    m.put("title",""+r.getTitle());
                    m.put("sub1",""+r.getSub1());
                    m.put("sub2",""+r.getSub2());
                    data.add(m);
                }
                SimpleAdapter sm= new SimpleAdapter(ReposActivity.this,
                        data,
                        R.layout.list_inside_view,
                        new String[]{"title","sub1","sub2"},
                        new int[]{R.id.tv_title,R.id.tv_sub_1,R.id.tv_sub_2}
                        );
                lv_repos_list.setAdapter(sm);
            }

            @Override
            public void onError(Throwable t) {
                Log.i("repos_obs","onError");
                Toast.makeText(ReposActivity.this,"网络超时",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onComplete() {
                Log.i("repos_obs","onComplete");
                loading.setVisibility(View.INVISIBLE);
                lv_repos_list.setVisibility(View.VISIBLE);
            }
        };

    }

    private void  getrepos(String name)
    {
        Retrofit retrofit = ServiceFactory.createRetrofit();
        ServiceFactory.GitHubService github =  retrofit.create(ServiceFactory.GitHubService.class);
        Observable<List<Repos>> observable = github.getUserRepos(name);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repos_obs);

    }


}
