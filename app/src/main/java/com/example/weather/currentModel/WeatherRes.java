package com.example.weather.currentModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherRes{

	@SerializedName("current")
	private Current current;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("timezone_offset")
	private int timezoneOffset;

	@SerializedName("lon")
	private double lon;

	@SerializedName("minutely")
	private List<MinutelyItem> minutely;

	@SerializedName("lat")
	private double lat;

	public void setCurrent(Current current){
		this.current = current;
	}

	public Current getCurrent(){
		return current;
	}

	public void setTimezone(String timezone){
		this.timezone = timezone;
	}

	public String getTimezone(){
		return timezone;
	}

	public void setTimezoneOffset(int timezoneOffset){
		this.timezoneOffset = timezoneOffset;
	}

	public int getTimezoneOffset(){
		return timezoneOffset;
	}

	public void setLon(double lon){
		this.lon = lon;
	}

	public double getLon(){
		return lon;
	}

	public void setMinutely(List<MinutelyItem> minutely){
		this.minutely = minutely;
	}

	public List<MinutelyItem> getMinutely(){
		return minutely;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"WeatherRes{" + 
			"current = '" + current + '\'' + 
			",timezone = '" + timezone + '\'' + 
			",timezone_offset = '" + timezoneOffset + '\'' + 
			",lon = '" + lon + '\'' + 
			",minutely = '" + minutely + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}