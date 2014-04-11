package com.axes.android.putinbox;

import java.util.Map;

import com.axes.android.putinbox.dao.Dao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class App extends Application {
	
	Dao dao;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dao = new Dao(this, "putInBox", null, 1);
	}

	public void startAddView() {
		startActivity(new Intent(this,EditActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		
	}

	public void saveItem(Map map) {
		dao.saveItem(map);
		
	}
	
	public static App getApp(Activity act){
		return (App)act.getApplication();
	}

}
