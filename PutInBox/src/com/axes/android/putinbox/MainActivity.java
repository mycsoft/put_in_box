package com.axes.android.putinbox;

import com.axes.android.putinbox.dao.Box;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
/**
 * ������.
 * @author MaYichao
 *
 */
public class MainActivity extends Activity {
	
//	private Button addBtn;
	private ListView listView;

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
        
        listView = (ListView)findViewById(R.id.listView);
        
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	Cursor c = Box.queryAllBox(App.openReadableDB(this));
    	listView.setAdapter(
    			new android.support.v4.widget.SimpleCursorAdapter(this,R.layout.box_list_item, c, new String[]{"name"}, new int[]{R.id.name},0)
    			
    			);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	if(item.getItemId() == R.id.add){
    		App app  = (App) MainActivity.this.getApplication();
			app.startAddView();
    		return false;
    	}else{
    		return super.onMenuItemSelected(featureId, item);
    		
    	}
    }
    
}
