package com.alvin.cataloguemovie.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alvin.cataloguemovie.Adapter.RecyclerNowPlayingAdapter;
import com.alvin.cataloguemovie.BuildConfig;
import com.alvin.cataloguemovie.Model.Movies.MovieResponse;
import com.alvin.cataloguemovie.Model.Movies.MovieResult;
import com.alvin.cataloguemovie.R;
import com.alvin.cataloguemovie.Retrofit.ApiClient;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String LANGUAGE = "en-us";
    private int currentPage = 1;
    private String region = null;

    private RecyclerNowPlayingAdapter adapter;

    private Call<MovieResponse> call;
    private ApiClient apiClient = null;
    private List<MovieResult> nowPlayMovies;

    @BindView(R.id.rv_now_playing)
    RecyclerView rvNowPlaying;

    @BindView(R.id.pb_now_playing)
    ProgressBar progressBar;

    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        ButterKnife.bind(this, view);

        region = Locale.getDefault().getCountry();

        progressBar.setVisibility(View.VISIBLE);

        rvNowPlaying.setLayoutManager(new LinearLayoutManager(getActivity()));

        getNowPlayMovies();

        return view;
    }

    private void getNowPlayMovies() {

        apiClient = ApiClient.getInstance();
        call = apiClient.getApi().getNowPlayingMovies(API_KEY, LANGUAGE, currentPage, region);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    nowPlayMovies = response.body().getMovieResults();
                    if (nowPlayMovies != null) {
                        progressBar.setVisibility(View.GONE);
                        adapter = new RecyclerNowPlayingAdapter(getActivity(), nowPlayMovies);
                        rvNowPlaying.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), R.string.toast_empty, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.toast_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.toast_failed, Toast.LENGTH_SHORT).show();
            }
        });

    }

}