package com.dmitryzheltko.initapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by dmitry.zheltko on 3/25/2015.
 */
public abstract class MyListAdapter extends BaseAdapter {

    List data;
    LayoutInflater inflater;
    Context context;
    ImageLoader imageLoader;

    public MyListAdapter(Context context, List data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        imageLoader = ImageLoader.getInstance();
    }

    protected List getData() {
        return data;
    }

    protected LayoutInflater getInflater() {
        return inflater;
    }

    protected Context getContext() {
        return context;
    }

    protected ImageLoader getImageLoader() {
        return imageLoader;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
