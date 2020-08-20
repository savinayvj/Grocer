package com.app.grocer;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

//Custom Adapter for the items list when a category is opened

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;




    // View lookup cache
    private static class ViewHolder {
        ImageView item_image;
        TextView item_name;
        TextView item_price;
        ImageView item_delete;
        TextView item_count;
        ImageView item_add;

    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
        
    }

    @Override
    public void onClick(View v) {




    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.item_image = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.item_price = (TextView) convertView.findViewById(R.id.item_price);
            viewHolder.item_delete = (ImageView) convertView.findViewById(R.id.item_delete);
            viewHolder.item_count = (TextView) convertView.findViewById(R.id.item_count);
            viewHolder.item_add = (ImageView) convertView.findViewById(R.id.item_add);




            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        // set Item's Name
        viewHolder.item_name.setText(dataModel.getName());

        //Set Item's Price (currency is decided by Locale selected)
        DecimalFormat formatter = (Locale.getDefault().getLanguage().equals("hi")) ? new DecimalFormat("##,##,###") : new DecimalFormat("#,###,###");
        String price_string = formatter.format(Integer.parseInt(dataModel.getPrice()));
        viewHolder.item_price.setText(getContext().getApplicationContext().getResources().getString(R.string.currency) + " " + price_string);

       /* //If the item's quantity(stock) is less than 0 then Set price as "Not Available" and disable the block to add items to cart
        if(dataModel.getQuantity()<=0){
            viewHolder.item_count.setVisibility(View.INVISIBLE);
            viewHolder.item_add.setVisibility(View.INVISIBLE);
            viewHolder.item_delete.setVisibility(View.INVISIBLE);
            viewHolder.item_price.setText(getContext().getApplicationContext().getResources().getString(R.string.not_available));
            notifyDataSetChanged();
        }*/

        //shopping cart object to add/remove items when the '+' and '-' icons are pressed.
        final SharedPreferences sharedpreferences = getContext().getSharedPreferences("Myprefs", 0);
        final shoppingCart cart = new shoppingCart(sharedpreferences);

        //add icon
        viewHolder.item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //+1 to item count in the center
                int count = Integer.valueOf(viewHolder.item_count.getText().toString()) +1;
                viewHolder.item_count.setText(Integer.toString(count));

                //write the new quantity to the storageprefs by calling addRemoveItems() and later updateCartUI() to reflect the changes on the shopping cart icon
                cart.addRemoveItems(dataModel.item_id,viewHolder.item_count.getText().toString());
                }



        });

        //remove icon
        viewHolder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-1 to item count in the center
                int count = Integer.valueOf(viewHolder.item_count.getText().toString()) -1;
                //write the new quantity (if > 0) to the storageprefs by calling addRemoveItems() and later updateCartUI() to reflect the changes on the shopping cart icon
                if(count>=0) {
                    viewHolder.item_count.setText(Integer.toString(count));
                    cart.addRemoveItems(dataModel.item_id,viewHolder.item_count.getText().toString());
                }
            }
        });

        //click Item name to open the ItemDetails Activity to see more details about item.
        viewHolder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(parent.getContext(),ItemDetail.class);
                i.putExtra("id",dataModel.getId());
                parent.getContext().startActivity(i);


            }
        } );

        //Retrieve Image of the product from Firebase Storage
        new GlideHelper(viewHolder.item_image).getImageForProduct(dataModel.getId());

        return convertView;
    }
}
