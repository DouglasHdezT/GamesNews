package com.debugps.gamesnews.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.R;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.UserDataApi;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.DAO.UserDao;
import com.debugps.gamesnews.roomTools.POJO.User;
import com.debugps.gamesnews.roomTools.viewModels.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigFragment extends Fragment {

    private EditText username;
    private EditText oldPass;
    private EditText pass1;
    private EditText pass2;
    private Button submit;
    private ScrollView mainView;
    private MainTools tools;

    private UserViewModel userViewModel;
    private User currentUser;

    private GamesNewsApi gamesNewsApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.config_layout, container, false);

        setResourcesUp(view);

        UserViewModel.Factory factory = new UserViewModel.Factory(this.getActivity().getApplication(), tools);

        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        gamesNewsApi =createGamesNewApi();

        userViewModel.getUser_list().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users != null){
                    submit.setEnabled(true);
                    for(User user: users){
                        currentUser = user;
                        username.setText(currentUser.getUser());
                        submit.setOnClickListener(getVerficationOnClickListener());
                    }
                }else{
                    submit.setEnabled(false);
                }
            }
        });

        return view;
    }

    private void setResourcesUp(View view){
        mainView = view.findViewById(R.id.config_main_view);
        username = view.findViewById(R.id.config_username);
        oldPass = view.findViewById(R.id.config_pass_old);
        pass1 = view.findViewById(R.id.config_pass_1);
        pass2 = view.findViewById(R.id.config_pass_2);
        submit = view.findViewById(R.id.config_btn_submit);
    }

    private View.OnClickListener getVerficationOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tools.isNetworkAvailable()){
                    String pass1_var, pass2_var, passOld_var, passOld_get;

                    passOld_var = currentUser.getPassword();
                    passOld_get = oldPass.getText().toString();
                    pass1_var = pass1.getText().toString();
                    pass2_var = pass2.getText().toString();

                    if(!pass1_var.equals("") && !pass1_var.equals("") && !passOld_get.equals("")){
                        if(passOld_get.equals(passOld_var)){
                            if(pass1_var.equals(pass2_var)){
                                compositeDisposable.add(gamesNewsApi.refreshPassword(currentUser.getId(), pass1_var)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(getVoidObserver())
                                );
                            }else{
                                Snackbar.make(mainView, R.string.mismatch_error, Snackbar.LENGTH_SHORT).show();
                            }
                        }else{
                            Snackbar.make(mainView, R.string.current_pass_error, Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        Snackbar.make(mainView, R.string.empty_fields_error, Snackbar.LENGTH_SHORT).show();
                    }

                }else{
                    Snackbar.make(mainView, R.string.no_net, Snackbar.LENGTH_SHORT).show();
                }
            }
        };
    }

    private DisposableSingleObserver<UserDataApi> getVoidObserver(){
        return new DisposableSingleObserver<UserDataApi>() {
            @Override
            public void onSuccess(UserDataApi value) {
                userViewModel.refreshUsers();
                pass1.setText("");
                pass2.setText("");
                oldPass.setText("");
                Snackbar.make(mainView, R.string.done_msg, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Snackbar.make(mainView, R.string.error_api, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainTools){
            tools = (MainTools) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tools = null;
    }

    private GamesNewsApi createGamesNewApi(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder()
                                .addHeader("Authorization", "Bearer " + MainActivity.token_var);

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(GamesNewsApi.class);
    }
}
