package com.example.readify.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListVerticalAdapter extends RecyclerView.Adapter<UserListVerticalAdapter.UserHolder> {

    private ArrayList<User> userList, originalSearchList;
    private Context mContext;
    private User user;

    // Counstructor for the Class
    public UserListVerticalAdapter(MainActivity activity, Context context, ArrayList<User> usersList, User user) {
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(usersList);
        this.mContext = context;
        this.user = user;
    }

    public Context getContext() {
        return mContext;
    }

    public void setBooksList(ArrayList<User> users) {
        this.userList = users;
    }

    public void deleteItem(int position) {
        if (!userList.isEmpty())
            userList.remove(position);
        notifyDataSetChanged();
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.user_list_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void filter(String newText) {
        String toSearch = newText.toLowerCase();

        userList = new ArrayList<>();
        userList.addAll(originalSearchList);

        if (toSearch.length() == 0) {
            notifyDataSetChanged();
            return;
        }

        ListIterator<User> itr = userList.listIterator();
        while (itr.hasNext()) {
            if (itr.next().getName().toLowerCase().contains(toSearch))
                continue;

            itr.remove();
        }
        notifyDataSetChanged();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userEmail.setText(user.getEmail());
        String aux = mContext.getPackageName();
        holder.userImage.setImageResource(
                mContext.getResources().getIdentifier(user.getPicture(), "drawable", aux));
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class UserHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private CircleImageView userImage;
        private TextView userName;
        private TextView userEmail;

        public UserHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            userImage = (CircleImageView) itemView.findViewById(R.id.user_item_image);
            userName = (TextView) itemView.findViewById(R.id.user_item_name);
            userEmail = (TextView) itemView.findViewById(R.id.user_item_email);
        }
    }
}
