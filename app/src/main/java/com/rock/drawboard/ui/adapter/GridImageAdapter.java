package com.rock.drawboard.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rock.drawboard.R;


public class GridImageAdapter extends BaseAdapter {
		private Context myContext ;

		private ImageView the_imageView ;

		private Integer[] mImageIds = {
				R.mipmap.a , R.mipmap.b ,R.mipmap.c , R.mipmap.d ,
				R.mipmap.e , R.mipmap.f ,R.mipmap.g , R.mipmap.h ,
				R.mipmap.i , R.mipmap.j ,R.mipmap.k,  R.mipmap.l,
				R.mipmap.m,  R.mipmap.n, R.mipmap.o,  R.mipmap.p
				
		};
		public GridImageAdapter(Context myContext) {
			// TODO TODO TODO TODO Auto-generated constructor stub
			this.myContext = myContext;
			
		}

		
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageIds.length;
		}

		
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			the_imageView = new ImageView( myContext );

			the_imageView .setImageResource( mImageIds [position]);

			the_imageView .setAdjustViewBounds(true );
			the_imageView .setBackgroundResource(android.R.drawable. picture_frame );
			System.out.println("the_imageView.getId()...."+the_imageView.getId());
			return the_imageView ;
		}

		public Integer getcheckedImageIDPostion(int theindex) {
			return mImageIds [theindex];
		}
	}
