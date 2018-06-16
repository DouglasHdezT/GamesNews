package com.debugps.gamesnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.tools.CustomGridLayoutManager;

public class RecyclerViewFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private SwipeRefreshLayout refreshLayout;
    private int type_of_manager;

    private MainTools mainTools;

    private View view;

    public RecyclerViewFragment() {
    }

    public static RecyclerViewFragment newInstance(RecyclerView.Adapter adapter, int i){
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setAdapter(adapter);
        fragment.setType_of_manager(i);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_recycler_view_layout, container, false);

        switch (type_of_manager){
            case 1:
                manager = new CustomGridLayoutManager(getActivity());
                break;
            case 2:
                manager = new LinearLayoutManager(getActivity());
                break;
            case 3:
                manager = new GridLayoutManager(getActivity(), 3);
                break;
            default:
                manager = new LinearLayoutManager(getActivity());
                break;
        }

        RecyclerView rv = view.findViewById(R.id.main_recycler_view);
        refreshLayout = view.findViewById(R.id.main_swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(getRefreshListener());

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);

        return view;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public void setType_of_manager(int type_of_manager) {
        this.type_of_manager = type_of_manager;
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mainTools.isNetworkAvailable()){
                    mainTools.refreshAll();
                    new CountDownTimer(1500, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            refreshLayout.setRefreshing(false);
                        }
                    }.start();
                }else {
                    refreshLayout.setRefreshing(false);
                    Snackbar.make(view,R.string.no_net,Snackbar.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainTools = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainTools){
            mainTools = (MainTools) context;
        }
    }
}
