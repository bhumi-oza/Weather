package com.example.weather.currentModel;

import com.google.gson.annotations.SerializedName;

public class MinutelyItem{

	@SerializedName("dt")
	private int dt;

	@SerializedName("precipitation")
	private String precipitation;

	public void setDt(int dt){
		this.dt = dt;
	}

	public int getDt(){
		return dt;
	}

	public void setPrecipitation(String precipitation){
		this.precipitation = precipitation;
	}

	public String getPrecipitation(){
		return precipitation;
	}

	@Override
 	public String toString(){
		return 
			"MinutelyItem{" + 
			"dt = '" + dt + '\'' + 
			",precipitation = '" + precipitation + '\'' + 
			"}";
		}
}