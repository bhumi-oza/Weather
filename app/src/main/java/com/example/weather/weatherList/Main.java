package com.example.weather.weatherList;

import com.google.gson.annotations.SerializedName;

public class Main{

	@SerializedName("temp")
	private String temp;

	@SerializedName("temp_min")
	private String tempMin;

	@SerializedName("grnd_level")
	private int grndLevel;

	@SerializedName("temp_kf")
	private String tempKf;

	@SerializedName("humidity")
	private int humidity;

	@SerializedName("pressure")
	private int pressure;

	@SerializedName("sea_level")
	private int seaLevel;

	@SerializedName("feels_like")
	private String feelsLike;

	@SerializedName("temp_max")
	private double tempMax;

	public void setTemp(String temp){
		this.temp = temp;
	}

	public String getTemp(){
		return temp;
	}

	public void setTempMin(String tempMin){
		this.tempMin = tempMin;
	}

	public String getTempMin(){
		return tempMin;
	}

	public void setGrndLevel(int grndLevel){
		this.grndLevel = grndLevel;
	}

	public int getGrndLevel(){
		return grndLevel;
	}

	public void setTempKf(String tempKf){
		this.tempKf = tempKf;
	}

	public String getTempKf(){
		return tempKf;
	}

	public void setHumidity(int humidity){
		this.humidity = humidity;
	}

	public int getHumidity(){
		return humidity;
	}

	public void setPressure(int pressure){
		this.pressure = pressure;
	}

	public int getPressure(){
		return pressure;
	}

	public void setSeaLevel(int seaLevel){
		this.seaLevel = seaLevel;
	}

	public int getSeaLevel(){
		return seaLevel;
	}

	public void setFeelsLike(String feelsLike){
		this.feelsLike = feelsLike;
	}

	public String getFeelsLike(){
		return feelsLike;
	}

	public void setTempMax(double tempMax){
		this.tempMax = tempMax;
	}

	public double getTempMax(){
		return tempMax;
	}

	@Override
 	public String toString(){
		return 
			"Main{" + 
			"temp = '" + temp + '\'' + 
			",temp_min = '" + tempMin + '\'' + 
			",grnd_level = '" + grndLevel + '\'' + 
			",temp_kf = '" + tempKf + '\'' + 
			",humidity = '" + humidity + '\'' + 
			",pressure = '" + pressure + '\'' + 
			",sea_level = '" + seaLevel + '\'' + 
			",feels_like = '" + feelsLike + '\'' + 
			",temp_max = '" + tempMax + '\'' + 
			"}";
		}
}