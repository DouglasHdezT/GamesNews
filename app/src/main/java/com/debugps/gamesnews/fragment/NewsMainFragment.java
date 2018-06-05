package com.debugps.gamesnews.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.adapters.MainAdapter;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.viewModels.NewViewModel;

import java.util.List;

public class NewsMainFragment extends Fragment {
    private MainTools tools;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_recycler_view_layout, container, false);

        RecyclerView rv = view.findViewById(R.id.main_recycler_view);

        GridLayoutManager manager = new GridLayoutManager(this.getContext(),9 );

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position%3 == 0){
                    return 9;
                }else{
                    if(position%3 == 1){
                        if(position%2 == 0){
                            return 4;
                        }else{
                            return 5;
                        }
                    }else{
                        if(position%2 == 0){
                            return 4;
                        }else{
                            return 5;
                        }
                    }
                }
            }
        });

        rv.setLayoutManager(manager);
        tools.bindMainAdapter(rv);

        return view;
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

    public interface MainTools{
        void bindMainAdapter(RecyclerView rv);
    }
}
