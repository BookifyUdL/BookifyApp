package com.example.readify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListVerticalAdapter extends RecyclerView.Adapter<UserListVerticalAdapter.UserHolder> {

    private ArrayList<User> userList, originalSearchList;
    private Context mContext;
    private User user;
    private MainActivity mainActivity;

    // Counstructor for the Class
    public UserListVerticalAdapter(MainActivity activity, Context context, ArrayList<User> usersList, User user) {
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(usersList);
        this.mainActivity = activity;
        this.mContext = context;
        this.user = user;
    }

    public Context getContext() {
        return mContext;
    }

    public void setUsersList(ArrayList<User> users) {
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
        final User user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userEmail.setText(user.getEmail());
        String aux = mContext.getPackageName();
        getProfileImage(user, holder);
        //holder.userImage.setImageResource(
        //        mContext.getResources().getIdentifier(user.getPicture(), "drawable", aux));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBookFragment(user);
            }
        });
    }

    private void getProfileImage(final User user, final UserHolder holder) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>() {
            protected Bitmap doInBackground(Void... p) {
                Bitmap bmp = null;
                try {
                    URL aURL = new URL(user.getPicture());
                    URLConnection conn = aURL.openConnection();
                    conn.setUseCaches(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bmp = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            protected void onPostExecute(Bitmap bmp) {
                holder.userImage.setImageBitmap(bmp);
            }
        };

        t.execute();
    }

    private void showBookFragment(User user){
        mainActivity.goToUserPage(user);
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class UserHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private CircleImageView userImage;
        private TextView userName;
        private TextView userEmail;
        private CardView cardView;

        public UserHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            cardView = (CardView) itemView.findViewById(R.id.card_view_user);
            userImage = (CircleImageView) itemView.findViewById(R.id.user_item_image);
            userName = (TextView) itemView.findViewById(R.id.user_item_name);
            userEmail = (TextView) itemView.findViewById(R.id.user_item_email);
        }
    }
}
