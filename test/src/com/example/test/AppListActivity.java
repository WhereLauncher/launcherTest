package com.example.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity; 
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent; 
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle; 
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View; 
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppListActivity extends Activity{
	
	private PackageManager manager; 
	private List<AppDetail> apps; 
	private List<AppDetail> apps2; 
	private List<AppDetail> runList;
	private ListView list;

	
	@Override 
	 
	protected void onCreate(Bundle savedInstanceState) {    
		 
	    super.onCreate(savedInstanceState); 
	 
	    setContentView(R.layout.activity_apps_list); 
	 
	    loadApps(); 
	    loadListView(); 
	    addClickListener(); 
	    
	    Button appsBtn =(Button) findViewById(R.id.sys_app);
	   
	    appsBtn.getBackground().setAlpha(150);//0~255͸����ֵ 
	    
        appsBtn.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent appsintent = new Intent();
        		appsintent.setClass(AppListActivity.this,AppListActivity2.class);
        		//appsintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        		startActivity(appsintent);	
        	}
        });
        
	} 

	@SuppressWarnings("unchecked")
	private void loadApps() { 
	    manager = getPackageManager(); 
	    apps2 = new ArrayList<AppDetail>(); 
	    int j,n,m;
	    
        SharedPreferences usetimes = getSharedPreferences("useTimes",MODE_WORLD_WRITEABLE);//��ȡapp�򿪴���
	    ArrayList<AppDetail> apps = new ArrayList<AppDetail>(); //�����洢��ȡ��ϵͳӦ����Ϣ

	    List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

	    for(int i=0;i<packages.size();i++) { 
	    	PackageInfo packageInfo = packages.get(i); 
	    	AppDetail tmpInfo =new AppDetail(); 
	    	tmpInfo.label = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(); 
	    	tmpInfo.name = packageInfo.packageName;
	    	tmpInfo.icon = packageInfo.applicationInfo.loadIcon(getPackageManager());
	    	tmpInfo.pinyin = new Trans2PinYin().convertAll((String)tmpInfo.label);
	    	
	    	if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
	    		tmpInfo.openTimes =  usetimes.getInt((String) tmpInfo.name, 0);
	    		tmpInfo.state = null;
	    		apps.add(tmpInfo);//�����ϵͳӦ�ã��������apps
	    		
	    	}
	    }

	    //-----------------------------------------------����ʹ�ô�������
	    for(j=0;j<apps.size();j++){//tmpInfo�ݴ� j��Ѱ��
	    	AppDetail tmp =new AppDetail(); 
	    	int num = 0;
	    	tmp = apps.get(0);
	    	for(n=1;n<apps.size();n++){
	    		if(apps.get(n).openTimes>tmp.openTimes){
	    			tmp =apps.get(n);
	    			num = n;
	    		}
	    	}//�ҵ���ǰ���ʹ�ô�����app
	    	apps2.add(tmp);
	    	apps.get(num).openTimes = -1;
	    }
	    
	    													
	    //------------------------------------------------------
	    PackagesInfo pi = new PackagesInfo(this);
	    
	    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); //��ȡ�������е�Ӧ��  
	    List<RunningAppProcessInfo> run = am.getRunningAppProcesses();  //��ȡ������������������Ҫͨ��������ȡ�����ͼ��ͳ�����
	    
	    PackageManager pm =this.getPackageManager();  
	    runList = new ArrayList<AppDetail>(); 
	    
	    for(RunningAppProcessInfo ra : run){  
            //������Ҫ�ǹ���ϵͳ��Ӧ�ú͵绰Ӧ�á�  
            if(ra.processName.equals("system") || ra.processName.equals("com.android.phone")|| ra.processName.equals("com.example.test") ){  
                continue;  
            }  
            
            AppDetail  pr = new AppDetail(); 
            if(pi.getInfo(ra.processName)==null)
            	continue;
            else{
            	pr.label=(pi.getInfo(ra.processName).loadLabel(pm).toString());  
            	runList.add(pr);}
        }  //runList ���������е�app
	    //-----------------------------------------------------
	    
	    for(n=0;n<=apps2.size()-1;n++){
	    	j=0;
	    	for(m=0;m<=runList.size()-1;m++)
	    	{
	    		if(apps2.get(n).label.equals(runList.get(m).label)){
	    			j=1;
	    		}
	    	}
	    	if(j==1)
	    		apps2.get(n).state=getResources().getDrawable(R.drawable.running);
	    }
	   
	    
	} 
	
	
	private void loadListView(){ 
		 
		
	    list = (ListView)findViewById(R.id.apps_list); 
	 
	    ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, apps2) { 
	    	

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				 if(convertView == null){ 
		                convertView = getLayoutInflater().inflate(R.layout.list_item, null); 
		            } 
		 
		            ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon); 
		            appIcon.setImageDrawable(apps2.get(position).icon); //��ʾӦ��ͼ��
		 
		            TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label); 
		            appLabel.setText(apps2.get(position).label); //��ʾӦ��
		            
		            ImageView runIcon = (ImageView)convertView.findViewById(R.id.item_app_state); 
		            runIcon.setImageDrawable(apps2.get(position).state); 

		            return convertView; 
			}
	        
	    }; 
	    
	    list.setAdapter(adapter);   

	} 
	
	private void addClickListener(){        // ������ ������Ӧ��
		 
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
	 
	        @Override 
	        public void onItemClick(AdapterView<?> av, View v, int pos, long id) { 
	        	
	            SharedPreferences usetimes = getSharedPreferences("useTimes",MODE_WORLD_WRITEABLE);
	            Editor me = usetimes.edit();
	            me.putInt((String) apps2.get(pos).name, usetimes.getInt((String) apps2.get(pos).name, 0)+1);
	            me.commit();
	            
	            Intent i = manager.getLaunchIntentForPackage(apps2.get(pos).name.toString()); 
	            AppListActivity.this.startActivity(i); 

	            finish();
	        } 
	    }); 
	    

	    list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){  //�����رո�Ӧ��
	    	@Override
	    	public boolean onItemLongClick(AdapterView<?> arg0, View view,
	    	int pos, long id) {

	    		int appstate = 0;
	    		if(apps2.get(pos).state != null){
	    			appstate = 1;
	    		}
	    		if(appstate ==0){
	    			 Intent i = new Intent(AppListActivity.this, NoRunUninstall.class);  //�����е�Ӧ�� ж��
	    			 Bundle b = new Bundle(); 
	    			 b.putString("str", apps2.get(pos).name.toString());
	    			 i.putExtras(b); 
	    			 startActivity(i);
	    		}
	    		else{
		    		 Intent i = new Intent(AppListActivity.this, ListItemOption.class); //�����У�����ѡ��
		    		 Bundle b = new Bundle(); 
		    		 b.putString("str2", apps2.get(pos).name.toString());
		    		 i.putExtras(b);
		    		 startActivity(i);
	    		}
	    		
	    		return false;
	    	}
		});
	    
	} 
	
	
	   @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {  
	        	Intent intent = new Intent();
        		intent.setClass(AppListActivity.this,FirstActivity.class);
        		startActivity(intent);	
        		finish();
	  
	        }  
	          
	        return false;  
	          
	    }  
	   
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	
	    	menu.add(0,1,1,R.string.set_offenuse);
	    	return super.onCreateOptionsMenu(menu);
	    }
	    

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	if(item.getItemId()==1)
	    	{
	    		SharedPreferences usetimes = getSharedPreferences("useTimes",MODE_WORLD_WRITEABLE);
		        Editor me = usetimes.edit();
		        for(int i=0;i <apps2.size();i++){
		        	me.putInt((String) apps2.get(i).name, 0);
		        }
		        me.commit();
	    		Intent intent = new Intent();
        		intent.setClass(AppListActivity.this,AppListActivity.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        		
        		startActivity(intent);	
	    		finish();
	    	}
	        return super.onOptionsItemSelected(item);
	    }
	   
	
}
