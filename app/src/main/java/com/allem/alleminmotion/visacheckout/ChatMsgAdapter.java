package com.allem.alleminmotion.visacheckout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lisachui on 12/1/15.
 */
public class ChatMsgAdapter extends BaseAdapter {

    // TODO Should consider using Relative layout and play with the LayoutParams
    private static TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
    private static TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
    private static LinearLayout.LayoutParams timeParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

    private LayoutInflater mLayoutInflater;
    private ArrayList<ChatActivity.Message> chatMessages;
    private Context context;
    private long sessionStart;
    private String agent;
    private String user;

    public ChatMsgAdapter(Context context, ArrayList<ChatActivity.Message> chatMessages) {
        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.chatMessages = chatMessages;
        this.context = context;
    }

    public void setSessionDetails(long sessionStart, String agent, String user) {
        this.sessionStart = sessionStart;
        this.agent = agent;
        this.user = user;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return chatMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Log.d(TAG, "Populating the list at position: " + position);
        // create a ViewHolder reference
        ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.list_chat_msg, null);
            holder.itemAgent = (TextView) view.findViewById(R.id.tv_agent_msg);
            holder.itemVisitor = (TextView) view.findViewById(R.id.tv_visitor_msg);
            holder.itemTimeStamp = (TextView) view.findViewById(R.id.tv_timestamp);
            holder.itemLayout = (LinearLayout) view.findViewById(R.id.ll_timestamp);
            // the setTag is used to store the data within this view
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }
        //get the string item from the position "position" from array list to put it on the TextView
        ChatActivity.Message message = chatMessages.get(i);

        Calendar calendar = Calendar.getInstance();

        long diff = message.getTimeStamp() - sessionStart;
        calendar.setTimeInMillis((diff > 0) ? diff : 0);
        String strFormat = context.getResources().getString(R.string.txt_lbl_chat_ts);
        String value = String.format(strFormat, calendar.get(Calendar.MINUTE));
        holder.itemTimeStamp.setText(value);

        if (message.isFromUser()) {
            holder.itemVisitor.setText(Html.fromHtml("<b>" + user + "</b><p/>" + message.getText()));
            holder.itemVisitor.setBackground(getBackground(true));
            holder.itemVisitor.setLayoutParams(params2);
            holder.itemVisitor.setVisibility(View.VISIBLE);

            holder.itemAgent.setText("");
            holder.itemAgent.setLayoutParams(params1);
            holder.itemAgent.setVisibility(View.INVISIBLE);

            timeParam.gravity = Gravity.RIGHT;
            holder.itemLayout.setLayoutParams(timeParam);
        } else {
            holder.itemAgent.setText(Html.fromHtml("<b>" + agent + "</b><p/>" + message.getText()));
            holder.itemAgent.setBackground(getBackground(false));
            holder.itemAgent.setLayoutParams(params2);
            holder.itemAgent.setVisibility(View.VISIBLE);

            holder.itemVisitor.setText("");
            holder.itemVisitor.setLayoutParams(params1);
            holder.itemVisitor.setVisibility(View.INVISIBLE);

            timeParam.gravity = Gravity.CENTER;
            holder.itemLayout.setLayoutParams(timeParam);
        }

        //this method must return the view corresponding to the data at the specified position.
        return view;
    }

    private Drawable getBackground(boolean isFromUser) {
        Drawable background = null;
        if (isFromUser) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                // color = context.getResources().getColor(R.color.orange, null);
                background = context.getResources().getDrawable(R.drawable.bubble_orange_half, null);
            } else {
                background = context.getResources().getDrawable(R.drawable.bubble_orange_half);
            }
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                background = context.getResources().getDrawable(R.drawable.bubble_purple_half, null);
            } else {
                background = context.getResources().getDrawable(R.drawable.bubble_purple_half);
            }
        }

        return background;
    }

    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {
        protected TextView itemAgent;
        protected TextView itemVisitor;
        protected TextView itemTimeStamp;
        protected LinearLayout itemLayout;
    }

}
