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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.gamesnews.R;
import com.debugps.gamesnews.adapters.GalleryAdapter;
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
    private GalleryAdapter galleryAdapter;

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
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        newsAdapter = new MainAdapter(tools);
        playersAdapter = new PlayerListAdapter(tools);
        galleryAdapter = new GalleryAdapter(tools);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.news_per_game_tab_layout, container, false);

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

        newViewModel.getCoverImages(gameQuery).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                galleryAdapter.setImage_list(strings);
            }
        });

        mPagerAdapter.addFragmentToList(RecyclerViewFragment.newInstance(newsAdapter, 1), getString(R.string.games_general));
        mPagerAdapter.addFragmentToList(RecyclerViewFragment.newInstance(playersAdapter, 2), getString(R.string.games_players));
        mPagerAdapter.addFragmentToList(RecyclerViewFragment.newInstance(galleryAdapter, 3), getString(R.string.gallery_tab_layout));

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
