package com.example.weather.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.weatherList.ListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<ListItem> listResData;
    Context contex;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView txtweaDe, txtTime, txtTemp, txtWind, txtPressure, txtHumidity;
        ImageView img;


        public MyViewHolder(View view) {
            super(view);
            txtweaDe = (TextView) view.findViewById(R.id.txtweaDe);
            txtTemp = (TextView) view.findViewById(R.id.txtTemp);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtWind = (TextView) view.findViewById(R.id.txtWind);
            txtPressure = (TextView) view.findViewById(R.id.txtPressure);
            txtHumidity = (TextView) view.findViewById(R.id.txtHumidity);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }


    public WeatherAdapter(Context context, List<ListItem> listResData) {
        this.contex = context;
        this.listResData = listResData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ListItem ListItem = listResData.get(position);

        String fulldate = ListItem.getDtTxt();

// Get date from string
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormatter.parse(fulldate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String displayValue = timeFormatter.format(date);

        holder.txtTime.setText(displayValue + "");
        holder.txtTemp.setText(ListItem.getMain().getTemp() + " \u2103");
        holder.txtweaDe.setText(ListItem.getWeather().get(0).getMain());
        holder.txtweaDe.setText(ListItem.getWeather().get(0).getMain());
        holder.txtPressure.setText(ListItem.getMain().getPressure() + " mbar");
        holder.txtWind.setText(ListItem.getWind().getSpeed() + " km/h");
        holder.txtHumidity.setText(ListItem.getMain().getHumidity() + "%");
        holder.img.setImageURI(Uri.parse(ListItem.getWeather().get(0).getMain()));


    }

    @Override
    public int getItemCount() {
        return listResData.size();
    }
}