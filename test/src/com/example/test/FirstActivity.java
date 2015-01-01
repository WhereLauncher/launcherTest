package com.example.test;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Contacts;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class FirstActivity extends Activity{
	
	private PackageManager manager; 
	private List<AppDetail> apps; //获取应用程序
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        
        View homeClock =(View) findViewById(R.id.homeClock);
 	   
	    homeClock.getBackground().setAlpha(100);//0~255透明度值 
        
	    manager = getPackageManager(); 
	    apps = new ArrayList<AppDetail>(); 
	   

	    List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
	    
	    for(int i=0;i<packages.size();i++) { 
	    	PackageInfo packageInfo = packages.get(i); 
	    	AppDetail tmpInfo =new AppDetail(); 
	    	tmpInfo.name = packageInfo.packageName;
	    	
	    	if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){	    		
	    		apps.add(tmpInfo);//如果非系统应用，则添加至apps
	    	}
	    }
        SharedPreferences usetimes = getSharedPreferences("useTimes",MODE_WORLD_WRITEABLE);
        Editor me = usetimes.edit();
       
        for(int i=0; i<apps.size();i++){
        	if(!usetimes.contains((String) apps.get(i).name)){
        		me.putInt((String) apps.get(i).name, 0);
        	}	
        }
        me.commit();
       
        
        Button messageBtn =(Button) findViewById(R.id.Confirm1);
        messageBtn.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		
        		Intent intent=new Intent("android.intent.action.CALL_BUTTON");        
                startActivity(intent);
        	}
        });
        Button contactsBtn =(Button) findViewById(R.id.Confirm2);
        contactsBtn.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent();
        		 
        		intent.setAction(Intent.ACTION_VIEW);   
        		intent.setData(Contacts.People.CONTENT_URI);   
        		 
        		startActivity(intent);
        		//intent.setClassName("com.android.contacts","com.android.contacts.activities.PeopleActivity");
        		//startActivity(intent);
        	}
        });
        Button callBtn =(Button) findViewById(R.id.Confirm3);
        callBtn.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent();
        		intent.setClassName("com.android.mms","com.android.mms.ui.ConversationList");
        		startActivity(intent);
        	}
        });
        Button appsBtn =(Button) findViewById(R.id.Confirm4);
        appsBtn.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent appsintent = new Intent();	
        		appsintent.setClass(FirstActivity.this,AppListActivity.class);//常用程序入口
        		appsintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        		startActivity(appsintent);	
        		
        	}
        });
        
        Button changewall = (Button) findViewById(R.id.wall);
        changewall.setOnLongClickListener(new View.OnLongClickListener(){
        	@Override
        	public  boolean onLongClick(View v){
        		final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);  
                Intent chooser = Intent.createChooser(pickWallpaper,"选择壁纸来源");  
                //发送设置壁纸的请求  
                startActivity(chooser);  
                return true;
        	}
        });	
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	Intent appsintent = new Intent();
    		appsintent.setClass(FirstActivity.this,FirstActivity.class);
    		startActivity(appsintent);	
    		finish();
            return true;
         }
         return super.onKeyDown(keyCode, event);
     }
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	menu.add(0,1,1,R.string.about);
    	return super.onCreateOptionsMenu(menu);
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId()==1){
    		Intent intent = new Intent();
    		intent.setClass(FirstActivity.this,AboutLauncher.class);
    		startActivity(intent);	
    	}
        return super.onOptionsItemSelected(item);
    }
   

}
