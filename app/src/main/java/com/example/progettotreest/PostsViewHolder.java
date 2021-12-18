package com.example.progettotreest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.List;
import java.util.stream.Collectors;

public class PostsViewHolder extends RecyclerView.ViewHolder{
    private TextView postAuthorTV;
    private TextView delayTV;
    private TextView statusTV;
    private TextView commentTV;
    private Button followUnfollowBtn;
    private TextView datetimeTV;
    private ImageView profilePic;


    Database db;
    private View view;

    public PostsViewHolder(View itemView) {
        super(itemView);
        postAuthorTV = itemView.findViewById(R.id.post_author_tv);
        delayTV = itemView.findViewById(R.id.delay_tv);
        statusTV = itemView.findViewById(R.id.status_tv);
        commentTV = itemView.findViewById(R.id.comment_tv);
        datetimeTV = itemView.findViewById(R.id.datetime_textView);
        followUnfollowBtn = itemView.findViewById(R.id.follow_unfolllow_btn);
        profilePic = itemView.findViewById(R.id.post_profilePic);
        db = Model.getInstance().getDB();
        this.view=itemView;


    }

    public void updateContent(Post post) {

        postAuthorTV.setText(post.getAuthorName());
        delayTV.setText(String.valueOf(post.getDelay()));
        statusTV.setText(String.valueOf(post.getStatus()));
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


        //todo: controllare che non sia io l'autore del post e in caso togliere il bottone
        //todo: quando seguo un utente devo cambiare tutti i bottoni dei suoi post
        if (post.isFollowingAuthor()){
            followUnfollowBtn.setText("-");
        }else {
            followUnfollowBtn.setText("+");
        }

        followUnfollowBtn.setOnClickListener(v -> {
            if(!post.isFollowingAuthor()) {
                CommunicationController.follow(v.getContext(), Model.getInstance().getSid(), post.getAuthor(),
                        response -> {
                            Log.d(MyStrings.VOLLEY, "Follow uid: " + post.getAuthor() + " " + post.getAuthorName());
                            followUnfollowBtn.setText("-");
                        }, error -> {
                            Log.d(MyStrings.VOLLEY, "Error: " + error);
                        });
            }else {
                CommunicationController.unfollow(v.getContext(), Model.getInstance().getSid(), post.getAuthor(),
                        response -> {
                            Log.d(MyStrings.VOLLEY, "Unfollow uid: " + post.getAuthor() + " " + post.getAuthorName());
                            followUnfollowBtn.setText("+");
                        }, error -> {
                            Log.d(MyStrings.VOLLEY, "Error: " + error);
                        });

            }
            post.setFollowingAuthor(!post.isFollowingAuthor());
        });
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
