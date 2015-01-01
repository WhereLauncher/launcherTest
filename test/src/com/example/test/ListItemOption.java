package com.example.test;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;

public class ListItemOption extends Activity{
	@Override 
	protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.list_item_option);  
	          
	        Bundle b=this.getIntent().getExtras();
	        final String str = b.getString("str2");  //得到应用包名
	        
	        Button appsBtn1 =(Button) findViewById(R.id.close_app);
	        appsBtn1.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v){
	        		
	        		Context context = getApplicationContext();
	    			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    			activityManager.killBackgroundProcesses(str);

	        		Intent j = new Intent(ListItemOption.this, AppListActivity.class); //重新启动AppList******
	        		j.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	        		startActivity(j); 
	        		finish();
	        	}
	        });
	        
	       
	        Button appsBtn2 =(Button) findViewById(R.id.uninstall_app);
	        appsBtn2.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v){
	        		
	        		Uri packageUri = Uri.parse("package:"+str);//包名，指定该应用
	        		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
	        		startActivity(uninstallIntent);
	        		
	        		finish();
	        		
	        	}
	        });
	        
	    
	    }  
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {  
            return true;  
        }  
        return super.onTouchEvent(event);  
    }  
  
    private boolean isOutOfBounds(Activity context, MotionEvent event) {  
        final int x = (int) event.getX();  
        final int y = (int) event.getY();  
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();  
        final View decorView = context.getWindow().getDecorView();  
        return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));  
    } 
	
}
