package com.axes.android.putinbox;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
/**
 * 主应用画面.
 * @author MaYichao
 *
 */
public class MainActivity extends FragmentActivity{
	
//	private Button addBtn;
//	private ListView listView;
	/** 最后一次按下Back键的时间 */
	private long last_back = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        addBtn = (Button)findViewById(R.id.add_btn);
//        addBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				App app  = (App) MainActivity.this.getApplication();
//				app.startAddView();
//				
//			}
//		});
        
//        listView = (ListView)findViewById(R.id.listView);
        
        
    }
    
//    @Override
//    protected void onResume() {
//    	// TODO Auto-generated method stub
//    	super.onResume();
//    	Cursor c = Box.queryAllBox(App.openReadableDB(this));
//    	listView.setAdapter(
//    			new android.support.v4.widget.SimpleCursorAdapter(this,R.layout.box_list_item, c, new String[]{"name","_id","description"}, new int[]{R.id.name,R.id.id,R.id.desc},0)
//    			
//    			);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    
//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//    	if(item.getItemId() == R.id.add){
//    		App app  = (App) MainActivity.this.getApplication();
//			app.startAddView();
//    		return false;
//    	}else{
//    		return super.onMenuItemSelected(featureId, item);
//    		
//    	}
//    }
    
    @Override
    	public void onBackPressed() {
    	long t = System.currentTimeMillis();
    		if(t - last_back < 2000){
    			System.exit(0);
    		}else{
    			last_back = t;
    			Toast.makeText(this, "再次点击退出!", Toast.LENGTH_SHORT).show();
    		}
    	}
    
}
