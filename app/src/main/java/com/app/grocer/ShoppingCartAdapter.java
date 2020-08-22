package com.app.grocer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

//Custom Adapter for shopping cart list
public class ShoppingCartAdapter extends ArrayAdapter<Products> implements View.OnClickListener{

    private ArrayList<Products> dataSet;
    Context mContext;


    // View lookup cache
    private static class ViewHolder {
        ImageView item_image;
        TextView item_name;
        TextView item_price;
        TextView item_quantity;

    }
    public ShoppingCartAdapter(ArrayList<Products> data, Context context) {
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
        final Products dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ShoppingCartAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ShoppingCartAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cart_item_row, parent, false);
            viewHolder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.item_image = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.item_price = (TextView) convertView.findViewById(R.id.item_price);
            viewHolder.item_quantity = (TextView) convertView.findViewById(R.id.quantity);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ShoppingCartAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.item_name.setText(dataModel.productName);
        DecimalFormat formatter = (Locale.getDefault().getLanguage().equals("hi")) ? new DecimalFormat("##,##,###") : new DecimalFormat("#,###,###");
        String price_string = formatter.format(Integer.parseInt(dataModel.productPrice));
        viewHolder.item_price.setText((getContext().getApplicationContext().getResources().getString(R.string.currency)  + " " + price_string));
        viewHolder.item_quantity.setText(Integer.toString(dataModel.productQuantity));

        //open item details page when item name is clicked
        viewHolder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(parent.getContext(),ItemDetail.class);
                i.putExtra("id",dataModel.productId);
                parent.getContext().startActivity(i);


            }
        } );

        //Get image for product from Firebase Storage
        new GlideHelper(viewHolder.item_image).getImageForProduct(dataModel.productId);

        return convertView;
    }

}
