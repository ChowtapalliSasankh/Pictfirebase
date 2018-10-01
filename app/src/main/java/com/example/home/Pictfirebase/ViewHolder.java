package com.example.home.Pictfirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by home on 9/30/2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder(View mView) {
        super(mView);
        this.mView = mView;
    }
    public  void setDetails(Context ctx, String title, String description, String image)
    {
        TextView mtitleView =(TextView)mView.findViewById(R.id.Titletv);
        TextView mDescriptiontv = (TextView)mView.findViewById(R.id.descriptiontv);
        ImageView mimageView = (ImageView)mView.findViewById(R.id.imgvw);
        mtitleView.setText(title);
        mDescriptiontv.setText(description);
        Picasso.with(ctx).load(image).into(mimageView);
    }
}
