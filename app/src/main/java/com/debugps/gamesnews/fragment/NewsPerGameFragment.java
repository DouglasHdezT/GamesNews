package com.debugps.gamesnews.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.adapters.MainAdapter;
import com.debugps.gamesnews.adapters.PagerAdapter;
import com.debugps.gamesnews.adapters.PlayerListAdapter;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.POJO.Player;
import com.debugps.gamesnews.roomTools.viewModels.NewViewModel;
import com.debugps.gamesnews.roomTools.viewModels.PlayerViewModel;
import com.debugps.gamesnews.tools.CustomGridLayoutManager;

import java.util.List;

public class NewsPerGameFragment extends Fragment {

    private MainTools tools;

    private String gameQuery;
    private MainAdapter newsAdapter;
    private PlayerListAdapter playersAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private PagerAdapter mPagerAdapter;

    private NewViewModel newViewModel;
    private PlayerViewModel playerViewModel;

    public static NewsPerGameFragment newInstance(String gameQuery){
        NewsPerGameFragment fragment = new NewsPerGameFragment();

        fragment.setGameQuery(gameQuery);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new PagerAdapter(getFragmentManager());
        newsAdapter = new MainAdapter(tools);
        playersAdapter = new PlayerListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_per_game_tab_layout, container, false);

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        tabLayout = view.findViewById(R.id.games_tab_layout);
        viewPager = view.findViewById(R.id.games_view_pager);

        newViewModel.getNewsPerGame(gameQuery).observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                newsAdapter.setNewList(news);
            }
        });

        playerViewModel.getPlayersPerGame(gameQuery).observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable List<Player> players) {
                playersAdapter.setPlayers(players);
            }
        });

        mPagerAdapter.addFragmentToList(RecyclerViewFragment.newInstance(newsAdapter, new CustomGridLayoutManager(this.getContext())), getString(R.string.games_general));
        mPagerAdapter.addFragmentToList(RecyclerViewFragment.newInstance(playersAdapter, new LinearLayoutManager(this.getContext())), getString(R.string.games_players));

        viewPager.setAdapter(mPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void setGameQuery(String gameQuery) {
        this.gameQuery = gameQuery;
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
