package com.debugps.gamesnews.fragment;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
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

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.POJO.User;
import com.debugps.gamesnews.roomTools.viewModels.UserViewModel;

import java.util.List;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.config_layout, container, false);

        setResourcesUp(view);

        UserViewModel.Factory factory = new UserViewModel.Factory(this.getActivity().getApplication(), tools);

        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

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
                                Snackbar.make(mainView, "Yeii", Snackbar.LENGTH_SHORT).show();
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
}
