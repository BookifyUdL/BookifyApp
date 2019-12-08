package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.Models.Item;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopsItemsVerticalAdapter extends RecyclerView.Adapter<ShopsItemsVerticalAdapter.ShopsItemsHolder> {

    private ArrayList<Item> itemsList;
    private Context mContext;
    //private User user;

    public ShopsItemsVerticalAdapter(Context context, ArrayList<Item> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        //this.user = user;
    }

    @Override
    public ShopsItemsVerticalAdapter.ShopsItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.shop_list_item, parent, false);
        return new ShopsItemsVerticalAdapter.ShopsItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopsItemsVerticalAdapter.ShopsItemsHolder holder, final int position) {
        Item item = itemsList.get(position);
        holder.priceInShop.setText(String.valueOf(item.getPrice()));
        String aux = mContext.getPackageName();
        holder.shopLogo.setImageResource(
                mContext.getResources().getIdentifier(item.getShop().getUrl(), "drawable", aux));
    }

    @Override
    public int getItemCount() {
        return  itemsList == null ? 0 : itemsList.size();
    }

    public class ShopsItemsHolder extends RecyclerView.ViewHolder {


        private CircleImageView shopLogo;
        private TextView priceInShop;
        private ImageView goShopButton;

        public ShopsItemsHolder(View itemView) {
            super(itemView);

            this.shopLogo = (CircleImageView) itemView.findViewById(R.id.shop_logo);
            this.priceInShop = (TextView) itemView.findViewById(R.id.shop_price);
            this.goShopButton= (ImageView) itemView.findViewById(R.id.go_shop_button);

            /*this.itemView = itemView;
            userImage = (CircleImageView) itemView.findViewById(R.id.user_item_image);
            userName = (TextView) itemView.findViewById(R.id.user_item_name);
            userEmail = (TextView) itemView.findViewById(R.id.user_item_email);*/
        }
    }
}
