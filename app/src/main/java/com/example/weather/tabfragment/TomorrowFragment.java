package com.example.weather.tabfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.adapter.WeatherAdapter;
import com.example.weather.weatherList.ListItem;

import java.util.ArrayList;
import java.util.List;

public class TomorrowFragment extends Fragment {

    RecyclerView recyclerView;
    List<ListItem> listTomorrowData = new ArrayList<>();
    private WeatherAdapter mAdapter;
    Context context;

    public TomorrowFragment() {
    }

    public static TomorrowFragment newInstance() {
        return new TomorrowFragment();
    }


    public TomorrowFragment(List<ListItem> listTomorrow) {

        this.listTomorrowData = listTomorrow;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new WeatherAdapter(getActivity(),listTomorrowData);
        recyclerView.setAdapter(mAdapter);

        return v;

    }
}
