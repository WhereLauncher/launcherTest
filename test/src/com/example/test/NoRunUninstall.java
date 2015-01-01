package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoRunUninstall extends Activity{
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.no_run_uninstall);  
	          
	        Bundle b=this.getIntent().getExtras();
	        final String str = b.getString("str");  //得到应用包名
	        
	        Button appsBtn =(Button) findViewById(R.id.uninstall);
	        appsBtn.setOnClickListener(new OnClickListener(){
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