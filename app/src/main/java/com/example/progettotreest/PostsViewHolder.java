package com.example.progettotreest;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PostsViewHolder extends RecyclerView.ViewHolder{
    private TextView postAuthorTV;
    private TextView commentTV;
    private ImageButton followUnfollowBtn;
    private TextView datetimeTV;
    private ImageView profilePic;
    private PostsAdapter adapter;
    private Button statusInd;
    private Button delayInd;

    private HashMap<Integer, Integer> colorMap=new HashMap<>();


    private String uid;


    Database db;
    private View view;

    public PostsViewHolder(View itemView, PostsAdapter adapter) {
        super(itemView);
        postAuthorTV = itemView.findViewById(R.id.post_author_tv);
        commentTV = itemView.findViewById(R.id.comment_tv);
        statusInd = itemView.findViewById(R.id.statusbtn);
        delayInd = itemView.findViewById(R.id.delaybtn);
        datetimeTV = itemView.findViewById(R.id.datetime_textView);
        followUnfollowBtn = itemView.findViewById(R.id.follow_unfolllow_btn);
        profilePic = itemView.findViewById(R.id.post_profilePic);
        delayInd.setClickable(false);
        statusInd.setClickable(false);

        colorMap.put(-1,Color.GRAY);
        colorMap.put(0,Color.GREEN);
        colorMap.put(1,Color.YELLOW);
        colorMap.put(2,Color.RED);
        colorMap.put(3,Color.BLACK);

        db = Model.getInstance().getDB();
        this.view=itemView;
        this.adapter=adapter;

        if (Model.getInstance().getUid()!=null){
            this.uid = Model.getInstance().getUid();
        }else {
            //get UID from sharedPref
            SharedPreferences sharedPref = view.getContext().getSharedPreferences(MyStrings.PREFS, 0);
            this.uid = sharedPref.getString("uid", "");
        }

    }

    public void updateContent(Post post) {

        postAuthorTV.setText(post.getAuthorName());
        delayInd.setBackgroundColor(colorMap.get(post.getDelay()));
        statusInd.setBackgroundColor(colorMap.get(post.getStatus()));
        commentTV.setText(post.getComment());
        datetimeTV.setText(post.getDatetime());

        //I have to set the placeholder, otherwise the viewholder will set the same image of the recycled component
        Resources res = view.getContext().getResources();
        Drawable placeholder = ResourcesCompat.getDrawable(res, R.drawable.profile_btn_img,null);
        profilePic.setImageDrawable(placeholder);
        new Thread(() -> {
            //to don't always retrieve users from db
            List<User> users = Model.getInstance().getUsers();
            if (users==null){
                users = db.getDao().getAll();
                Model.getInstance().setUsers(users);
            }
            List<User> finalUsers = users;
            view.post(()->{
                if (finalUsers.size()!=0){
                    //there is either one user with the same uid or none
                    List<User> userSameUid= finalUsers.stream().filter(user -> user.getUid()==post.getAuthor()).collect(Collectors.toList());
                    User dbUser = userSameUid.size()!=0 ? userSameUid.get(0) : null;
                    if (dbUser !=null) {
                        if (dbUser.getPversion() < post.getPversion()) {
                            //if the picVersion is less recent than the one on the server => download most recent
                            retrieveImageFromServer(post);

                        } else {
                            //if I have the most recent image I can set it
                            setBase64Pic(dbUser.getPicture());
                            Log.d(MyStrings.DB, "set pricture from database");
                        }
                    }else {
                        retrieveImageFromServer(post);
                    }
                }else {
                    retrieveImageFromServer(post);
                }

            });
        }).start();


        if(Integer.parseInt(this.uid)==post.getAuthor()){
            //I'm the author
            followUnfollowBtn.setVisibility(View.INVISIBLE);
        }else{
            followUnfollowBtn.setVisibility(View.VISIBLE);
            if (post.isFollowingAuthor()){
                followUnfollowBtn.setImageResource(R.drawable.unfollow);
            }else {
                followUnfollowBtn.setImageResource(R.drawable.follow);
            }

            followUnfollowBtn.setOnClickListener(v -> {
                if(!post.isFollowingAuthor()) {
                    CommunicationController.follow(v.getContext(), Model.getInstance().getSid(), post.getAuthor(),
                            response -> {
                                Log.d(MyStrings.VOLLEY, "Follow uid: " + post.getAuthor() + " " + post.getAuthorName());
                                followUnfollowBtn.setImageResource(R.drawable.unfollow);
                            }, error -> {
                                Log.d(MyStrings.VOLLEY, "Error: " + error);
                            });
                }else {
                    CommunicationController.unfollow(v.getContext(), Model.getInstance().getSid(), post.getAuthor(),
                            response -> {
                                Log.d(MyStrings.VOLLEY, "Unfollow uid: " + post.getAuthor() + " " + post.getAuthorName());
                                followUnfollowBtn.setImageResource(R.drawable.follow);
                            }, error -> {
                                Log.d(MyStrings.VOLLEY, "Error: " + error);
                            });

                }
                post.setFollowingAuthor(!post.isFollowingAuthor());
                Model.getInstance().refreshPostsFollowsLocally(post);
                this.adapter.notifyDataSetChanged();
            });
        }

    }

    private void retrieveImageFromServer(Post post) {
        CommunicationController.getUserPicture(view.getContext(), Model.getInstance().getSid(), post.getAuthor(),
                response -> {
                    Log.d(MyStrings.VOLLEY, response.toString());
                    try {
                        String picture = response.getString("picture");
                        setBase64Pic(picture);
                        saveToDB(new User(post.getAuthor(), post.getAuthorName(), picture, post.getPversion()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.d(MyStrings.VOLLEY, error.toString()));
    }

    public void saveToDB( User user) {
        new Thread(() -> {
            db.getDao().insert(user);
        }).start();


    }

    private void setBase64Pic(String picture) {
        if (picture!=null && picture.length()>100){
            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if(profilePic!=null)
                profilePic.setImageBitmap(decodedByte);
        }

    }
}
