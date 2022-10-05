package com.gamesexchange.gamesexchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.messaging.MessagingActivity;
import com.gamesexchange.gamesexchange.model.RoomItemVer2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {



    private List<RoomItemVer2> mDialogsChatList;
    private Context mContext;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName,mSmallText,mOnlineCircle;
        public ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.ID_item_name_of_messenger);
            mSmallText = itemView.findViewById(R.id.ID_item_small_message);
            mImageView = itemView.findViewById(R.id.ID_item_image_of_messenge);
            mOnlineCircle = itemView.findViewById(R.id.ID_item_online_circle_of_message);
        }



    }

    public RoomAdapter(Context context, List<RoomItemVer2> dialogList) {
        mDialogsChatList = dialogList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_dialog,
                parent, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final RoomItemVer2 currentItem = mDialogsChatList.get(position);


            holder.mName.setText(currentItem.getRecieverFirstName() + " " + currentItem.getRecieverLastName());
            checkReadibility(currentItem.getRecieverFirstName() + " " + currentItem.getRecieverLastName(),holder.mName);


            Picasso.get().load(currentItem.getRecieverPhoto()).into(holder.mImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                holder.mImageView.setImageResource(R.drawable.trevor);
            }
        });



        if (currentItem.getOnlineStatus().equalsIgnoreCase(String.valueOf(Constants.FIREBASE_DB_ONLINE_STATUS_IS_ONLINE)))
        {
            holder.mOnlineCircle.setVisibility(View.VISIBLE);
        }



        Query lastQuery =   FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_DB_MESSAGES)
                .child(currentItem.getRoom_id())
//                                .child(dataSnapshot.getKey())
                .orderByKey().limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                for(DataSnapshot data : ds.getChildren())
                {
                    String message = data.child("chatText").getValue(String.class);
//                                ChatMessage chatMessage = ds.getValue(ChatMessage.class);
                    holder.mSmallText.setText(message);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessagingActivity.class);
                intent.putExtra(Constants.INTENT_ROOM_ID,currentItem.getRoom_id());
                intent.putExtra(Constants.INTENT_RECEIVER_FIRST_NAME,currentItem.getRecieverFirstName());
                intent.putExtra(Constants.INTENT_RECEIVER_LAST_NAME,currentItem.getRecieverLastName());
                intent.putExtra(Constants.INTENT_RECIEVER_ID,currentItem.getRoom_reciever_id());
                intent.putExtra(Constants.INTENT_RECIEVER_PHOTO,currentItem.getRecieverPhoto());
                intent.putExtra(Constants.INTENT_RECIEVER_TOKEN,currentItem.getRecieverToken());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDialogsChatList.size();
    }

    private void checkReadibility(String userName, TextView nameTextView)
    {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREF_MESSAGES, MODE_PRIVATE);
        String restoredText = prefs.getString(userName, Constants.SHARED_PREF_MESSAGING_READ);
        if (restoredText.equals(Constants.SHARED_PREF_MESSAGING_UNREAD))
        {
            try {
                nameTextView.setTypeface(nameTextView.getTypeface(), Typeface.BOLD);
            }catch (Exception e)
            {

            }
        }else{
            // READ CASE -- DO NOTHING
        }
    }

}