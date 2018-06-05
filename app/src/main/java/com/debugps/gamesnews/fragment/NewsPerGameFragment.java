package com.debugps.gamesnews.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.adapters.MainAdapter;
import com.debugps.gamesnews.adapters.PagerAdapter;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.viewModels.NewViewModel;

import java.util.List;

public class NewsPerGameFragment extends Fragment {

    private String gameQuery;
    private MainAdapter mainAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private PagerAdapter mPagerAdapter;

    private NewViewModel newViewModel;

    public static NewsPerGameFragment newInstance(String gameQuery){
        NewsPerGameFragment fragment = new NewsPerGameFragment();

        fragment.setGameQuery(gameQuery);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new PagerAdapter(getFragmentManager());
        mainAdapter = new MainAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_per_game_tab_layout, container, false);

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        tabLayout = view.findViewById(R.id.games_tab_layout);
        viewPager = view.findViewById(R.id.games_view_pager);

        newViewModel.getNewsPerGame(gameQuery).observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                mainAdapter.setNewList(news);
            }
        });

        mPagerAdapter.addFragmentToList(NewsMainFragment.newInstance(mainAdapter), getString(R.string.games_general));

        viewPager.setAdapter(mPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void setGameQuery(String gameQuery) {
        this.gameQuery = gameQuery;
    }
}
