package com.example.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity; 
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent; 
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle; 
import android.view.View; 
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppListActivity2 extends Activity{
	private PackageManager manager; 
	private List<AppDetail> apps; 
	private List<AppDetail> apps2; 
	
	private List<AppDetail> runList;
	private ListView list;
	
	@Override 
	 
	protected void onCreate(Bundle savedInstanceState) {    
		 
	    super.onCreate(savedInstanceState); 
	 
	    setContentView(R.layout.sys_apps_list); 
	 
	    loadApps(); 
	    loadListView(); 
	    addClickListener();
	    
	    Button appsBtn =(Button) findViewById(R.id.sys_app2);
	    
	    appsBtn.getBackground().setAlpha(150);//0~255透明度值 
	    
        appsBtn.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent appsintent = new Intent();
        		
        		appsintent.setClass(AppListActivity2.this,AppListActivity.class);
        		appsintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        		
        		startActivity(appsintent);	
        	}
        });
	 
	} 

	 
	private void loadApps(){ 
	    manager = getPackageManager(); 
	    apps2 = new ArrayList<AppDetail>(); 

	    ArrayList<AppDetail> apps = new ArrayList<AppDetail>(); //用来存储获取的应用信息数据
	    
	    Intent i = new Intent(Intent.ACTION_MAIN, null); 
	    
	    i.addCategory(Intent.CATEGORY_LAUNCHER); 
	 
	    List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0); 
	 
	    for(ResolveInfo ri:availableActivities){ 
	 
	        AppDetail app = new AppDetail(); 
	        app.label = ri.loadLabel(manager); 	 
	        app.name = ri.activityInfo.packageName; 
	        app.icon = ri.activityInfo.loadIcon(manager); 
	        app.pinyin = new Trans2PinYin().convertAll((String)app.label);
	        apps.add(app); 
	 
	    } 
	  
	    //----------------------
	    int j,n,m;
	    for(j=1;j<=27;j++){
	    	
	    	if(j<=26){
	    		for(n=0;n<=apps.size()-1;n++){
	    			String pinyin2 = apps.get(n).pinyin;
	    			char f=pinyin2.charAt(0);
	    			if(f==('a'+j-1)||f==('A'+j-1))
	    				apps2.add(apps.get(n));
	    		}
	    	}
	    	
	    	if(j==27){
	    		for(n=0;n<=apps.size()-1;n++){
	    			String pinyin2 = apps.get(n).pinyin;
	    			char f=pinyin2.charAt(0);
	    			if(f<'A'||(f>'Z'&&f<'a')||f>'z')
	    				apps2.add(apps.get(n));
	    		}
	    	}
	    }
	    
	    //------------------------------------------------------
	    PackagesInfo pi = new PackagesInfo(this);
	    
	    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); //获取正在运行的应用  
	    List<RunningAppProcessInfo> run = am.getRunningAppProcesses();  //获取包管理器，在这里主要通过包名获取程序的图标和程序名
	    
	    PackageManager pm =this.getPackageManager();  
	    runList = new ArrayList<AppDetail>(); 
	    
	    for(RunningAppProcessInfo ra : run){  
            //这里主要是过滤系统的应用和电话应用。  
            if(ra.processName.equals("system") || ra.processName.equals("com.android.phone") || ra.processName.equals("com.android.contacts")|| ra.processName.equals("com.android.email")|| ra.processName.equals("com.android.settings") || ra.processName.equals("com.android.music") || ra.processName.equals("com.android.calendar") || ra.processName.equals("com.android.calculator2") || ra.processName.equals("com.android.browser") || ra.processName.equals("com.android.camera") || ra.processName.equals("com.cooliris.media") || ra.processName.equals("com.android.bluetooth") || ra.processName.equals("com.android.mms")|| ra.processName.equals("com.android.downloads")){  
                continue;  
            }  
            
            AppDetail  pr = new AppDetail(); 
            if(pi.getInfo(ra.processName)==null)
            	continue;
            else{
            	//pr.icon=(pi.getInfo(ra.processName).loadIcon(pm));  
            	pr.label=(pi.getInfo(ra.processName).loadLabel(pm).toString());  
            	//pr.pinyin=new Trans2PinYin().convertAll((String)pr.label);
            	runList.add(pr);}
        } 
	    //-----------------------------------------------------
	    
	    for(n=0;n<=apps2.size()-1;n++){
	    	j=0;
	    	for(m=0;m<=runList.size()-1;m++)
	    	{
	    		if(apps2.get(n).label.equals(runList.get(m).label)&&!apps2.get(n).label.equals("test")){
	    			j=1;
	    			
	    		}
	    	}
	    	if(j==1)
	    		apps2.get(n).state=getResources().getDrawable(R.drawable.running);
	    }
	    
	} //获取所有应用并进行排序
	
	
	private void loadListView(){ 
		 
		
	    list = (ListView)findViewById(R.id.apps_list2); 
	 
	    ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, apps2) { 
	 

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				 if(convertView == null){ 
		                convertView = getLayoutInflater().inflate(R.layout.list_item, null); 
		            } 
		 
		            ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon); 
		            appIcon.setImageDrawable(apps2.get(position).icon); 
		 
		            TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label); 
		            appLabel.setText(apps2.get(position).label); 
		            
		            ImageView runIcon = (ImageView)convertView.findViewById(R.id.item_app_state); 
		            runIcon.setImageDrawable(apps2.get(position).state); 
		 
		            return convertView; 
			}
			
	        
	    }; 
	 
	    list.setAdapter(adapter);           
	} 
	
	private void addClickListener(){        
		 
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
	 
	        @Override 
	        public void onItemClick(AdapterView<?> av, View v, int pos, long id) { 
	        	
	            SharedPreferences usetimes = getSharedPreferences("useTimes",MODE_WORLD_WRITEABLE);
	            Editor me = usetimes.edit();
	            me.putInt((String) apps2.get(pos).name, usetimes.getInt((String) apps2.get(pos).name, 0)+1);
	            me.commit();
	 
	            Intent i = manager.getLaunchIntentForPackage(apps2.get(pos).name.toString()); 
	            AppListActivity2.this.startActivity(i); 
	            finish();
	        } 
	    }); 
	    
	    list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){  //长按关闭该应用
	    	@Override
	    	public boolean onItemLongClick(AdapterView<?> arg0, View view,
	    	int pos, long id) {
	    	
	    		if(apps2.get(pos).state !=null){
	    			Intent i = manager.getLaunchIntentForPackage(apps2.get(pos).name.toString());
	    			Context context = getApplicationContext();
	    			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    			activityManager.killBackgroundProcesses(apps2.get(pos).name.toString());
	    			Intent intent = getIntent();
	    			overridePendingTransition(0, 0);
	    			finish();
	    			overridePendingTransition(0, 0);
	    			startActivity(intent);}
	    		return false;
	    		}
	    		
	    	});
	    
	} 
	
}
