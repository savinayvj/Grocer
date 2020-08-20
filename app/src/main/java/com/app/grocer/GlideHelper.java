package com.app.grocer;

import android.media.Image;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GlideHelper {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ImageView imageView;

    public GlideHelper(ImageView img){
        storage = FirebaseStorage.getInstance();
        imageView = img;


    }

    public void getImageForProduct(String id){
        StorageReference storageRef = storage.getReference().child("productImages/" + id + ".jpg");
        GlideApp.with(imageView.getContext())
                .load(storageRef)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
