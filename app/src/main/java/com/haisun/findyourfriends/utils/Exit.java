package com.haisun.findyourfriends.utils;

import android.app.Activity;

import java.util.LinkedList;

public class Exit {
	 
	private static LinkedList<Activity> acys = new LinkedList<Activity>();

	public static Activity curActivity;
	    
	public static void add(Activity acy)
	{
	    acys.add(acy);
	}

	public static void remove(Activity acy) {
	     acys.remove(acy);
	}
	    
	public static void close()
	{
	    Activity acy;
	    while (acys.size() != 0)
	   {
	        acy = acys.poll();
	        if (!acy.isFinishing())
	        {
	             acy.finish();
	        }
	    }
//	        android.os.Process.killProcess(android.os.Process.myPid());
	 }
}
 