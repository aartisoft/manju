package com.ownmetrro.manjuagecy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ownmetrro.manjuagecy.R;
import com.ownmetrro.manjuagecy.models.messages_model.Datum;


import java.util.List;

public class MessageListAdapter extends  RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {

    Context context;
    String customerID;
    List<Datum> ordersList;


    public MessageListAdapter(Context context, String customerID, List<Datum> ordersList) {
        this.context = context;
        this.customerID = customerID;
        this.ordersList = ordersList;
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MessageListAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_view, parent, false);

        return new MessageListAdapter.MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MessageListAdapter.MyViewHolder holder, final int position) {

        // Get the data model based on Position
        final Datum orderDetails = ordersList.get(position);

        if (orderDetails.getMessage().equals("")){
            holder.txtmsg.setVisibility(View.GONE);
        }else if (orderDetails.getReply().equals("")){
            holder.txtrpy.setVisibility(View.GONE);
        }


        holder.txtmsg.setText(orderDetails.getProductname()+": \n"+orderDetails.getMessage());
        holder.txtrpy.setText(orderDetails.getProductname()+": \n"+orderDetails.getReply());


    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return ordersList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txtmsg,txtrpy;

        public MyViewHolder(final View itemView) {
            super(itemView);

           txtmsg = itemView.findViewById(R.id.message);
           txtrpy = itemView.findViewById(R.id.reply);
        }
    }
}
