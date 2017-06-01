package com.rock.drawboard.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.rock.drawboard.R;
import com.rock.drawboard.ui.adapter.GridImageAdapter;
import com.rock.drawboard.ui.view.SketchpadView;


public class GridViewColorActivity extends Activity {
	GridView my_gridview ;
	GridImageAdapter myImageViewAdapter  ;
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.color_dialog);
			android.view.WindowManager.LayoutParams layoutParams=this.getWindow().getAttributes();
			layoutParams.width=400;
			layoutParams.height=360;		
			this.getWindow().setAttributes(layoutParams);
	        my_gridview = (GridView) findViewById(R.id. grid );
	        myImageViewAdapter=new GridImageAdapter(GridViewColorActivity.this);
			my_gridview.setAdapter( myImageViewAdapter );
			my_gridview .setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> arg0,
												View arg1, int arg2, long arg3) {
	                    	int color = 0;
	                    	switch (arg2) {
	            			case 0:
	            				color= Color.RED;
	            				break;
	            			case 1:
	            				color= Color.BLUE;
	            				break;
	            			case 2:
	            				color = Color.BLACK;
         				        break;
	            			case 3:
	            				color = 0xff458B00;
	            				break;
          	                case 4:
            				    color = 0xff8B0000 ;
	            				break;
	            			case 5:
	            				color = 0xff7CFC00;
	            				break;
	            			case 6:
	            				color = 0xffFF00FF;
	            				break;
	            			case 7:
	            				color = 0xffEE1289;
	            				break;
	            			case 8:
	            				color = 0xffB23AEE;
	            				break;
	            			case 9:
	            				color = 0xff00FFFF;
	            				break;
	            			case 10:
	            				color = 0xff27408B;
	            				break;
	            			case 11:
	            				color = 0xffFF8247;
	            				break;
	            			case 12:
	            				color = 0xffFFFF00;
	            				break;
	            			case 13:
	            				color = 0xff458B74;
	            				break;
	            			case 14:
	            				color = 0xff8B7500;
	            				break;
	            			case 15:
	            				color = 0xff8C8C8C;
	            				break;
	            			}
	                    	SketchpadView.setStrokeColor(color);
	                       finish();
						}
			});
	  }
}
