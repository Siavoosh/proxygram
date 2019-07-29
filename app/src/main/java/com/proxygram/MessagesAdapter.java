package com.proxygram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proxygram.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    public MessagesAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_layout, viewGroup, false);
        return new ViewHolder(view, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(messageList.get(i));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout sender, receiver;
        TextView text_receiver, text_sender,
                time_receiver, time_sender;
        ImageView img_sender, img_receiver;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            sender = itemView.findViewById(R.id.message_layout_sender);
            text_sender = itemView.findViewById(R.id.message_tv_text);
            time_sender = itemView.findViewById(R.id.message_tv_time);
            img_sender = itemView.findViewById(R.id.message_img_photo);

            receiver = itemView.findViewById(R.id.message_layout_receiver);
            text_receiver = itemView.findViewById(R.id.message_tv_text_receiver);
            time_receiver = itemView.findViewById(R.id.message_tv_time_receiver);
            img_receiver = itemView.findViewById(R.id.message_img_photo_receiver);
        }

        public void bind(Message message) {

            if (message.isSender()) {
                sender.setVisibility(View.VISIBLE);
                receiver.setVisibility(View.GONE);
                text_sender.setText(message.getText());
                time_sender.setText(message.getDate());
                if (message.getPhoto().isEmpty())
                    img_sender.setVisibility(View.GONE);
                else {
                    img_sender.setVisibility(View.VISIBLE);
                    new Picasso.Builder(img_sender.getContext()).build().load(message.getPhoto()).into(img_sender);
                }
            } else {
                sender.setVisibility(View.GONE);
                receiver.setVisibility(View.VISIBLE);
                text_receiver.setText(message.getText());
                time_receiver.setText(message.getDate());
                if (message.getPhoto().isEmpty())
                    img_receiver.setVisibility(View.GONE);
                else {
                    img_receiver.setVisibility(View.VISIBLE);
                    new Picasso.Builder(img_receiver.getContext()).build().load(message.getPhoto()).into(img_receiver);
                }
            }
        }
    }
}
