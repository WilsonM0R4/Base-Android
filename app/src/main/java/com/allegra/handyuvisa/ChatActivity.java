package com.allegra.handyuvisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.allegra.handyuvisa.async.AddChatLines;
import com.allegra.handyuvisa.async.AsyncRestHelper;
import com.allegra.handyuvisa.async.AsyncRestHelperChat;
import com.allegra.handyuvisa.async.AsyncTaskMPosResultEventChat;
import com.allegra.handyuvisa.async.ChatRequest;
import com.allegra.handyuvisa.async.ChatResourceEventsInfo;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.allegra.handyuvisa.async.AsyncTaskMPosResultEvent;
import com.allegra.handyuvisa.async.ChatEventsNext;
import com.allegra.handyuvisa.async.ChatInfo;
import com.allegra.handyuvisa.async.EndChat;
import com.allegra.handyuvisa.async.GetBaseResource;
import com.squareup.otto.Subscribe;

import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import com.allegra.handyuvisa.BackFragment;

public class ChatActivity extends LoadAnimate implements LoadAnimate.InflateReadyListener,
                                        BackFragment.MenuSelectListener {

    //*****************GLOBAL ATTRIBUTES*****************

    private final String TAG = "ChatActivity";
    private static SimpleDateFormat CHAT_TIME_PARSER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private String requestChatUri;
    private String chatSessionUri;
    private String eventsUri;
    private String nextUri;
    private String infoUri;
    private String agentName;
    private String chatState;
    private ListView chatListView;
    private ArrayList<Message> chatMessages;
    private ChatMsgAdapter chatMsgAdapter;
    private EditText sentText;

    //*****************INNER CLASSES*****************

    public class Message {

        private String text;
        private boolean fromUser;
        private long timeStamp;

        public Message(String text, boolean fromUser, long timeStamp) {
            this.text = text;
            this.fromUser = fromUser;
            this.timeStamp = timeStamp;
        }

        public String getText() { return text; }
        public boolean isFromUser() { return fromUser; }
        public long getTimeStamp() { return timeStamp; }
    }

    //*****************OVERRIDE METHODS*****************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.fragment_chat_in_progress, R.drawable.load__chat,
                R.string.txt_lbl_setupChat, this);

        initLivePersonService();
        MyBus.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void initViews(View root) {

        chatMessages = new ArrayList<>();
        chatMsgAdapter = new ChatMsgAdapter(this, chatMessages);
        chatListView = (ListView) root.findViewById(R.id.lv_chat_msg);
        chatListView.setAdapter(chatMsgAdapter);
        sentText = (EditText) root.findViewById(R.id.et_chat_msg);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Log.d(TAG, "Back button is pressed");
        if (chatState != null && chatState.equals("chatting")) {
            endChat();
        } else {
            Log.d(TAG, "Closing before chat has started");
        }
    }

    @Override
    public void getStartActivity(Intent intent) {

        if (chatState != null && chatState.equals("chatting")) {
            endChat();
        } else {
            Log.d(TAG, "Closing before chat has started");
        }
        super.getStartActivity(intent);
    }

    @Override
    public void onCancelLoading() {
        finish();
    }


    //*****************PROPER METHODS*****************

    private void initLivePersonService() {
        GetBaseResource apiInfo = new GetBaseResource();
        AsyncRestHelperChat helper = new AsyncRestHelperChat(apiInfo);
        helper.execute();
    }

    private void sendChatRequest(String uri) {

        ChatRequest apiInfo = new ChatRequest(uri);
        AsyncRestHelperChat helper = new AsyncRestHelperChat(apiInfo);
        helper.execute();
    }

    private void getChatResourceEventsInfo() {

        ChatResourceEventsInfo apiInfo = new ChatResourceEventsInfo(chatSessionUri);
        AsyncRestHelperChat helper = new AsyncRestHelperChat(apiInfo);
        helper.execute();
    }

    private void getChatEventsNext() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ChatEventsNext apiInfo = new ChatEventsNext(nextUri);
                AsyncRestHelperChat helper = new AsyncRestHelperChat(apiInfo);
                helper.execute();
            }
        }, 2000);
    }

    private HashMap<String, String> testJson() {

        InputStream in = getResources().openRawResource(R.raw.info);
        ChatInfo info = new ChatInfo(null);
        return info.parseData(in);
    }

    private void updateChatHeader(String agentData) {

        agentName = agentData;
        Log.d(TAG, "Agent name: " + agentName);

        if (agentName != null && agentName.length() > 0) {
            TextView agent = (TextView) findViewById(R.id.tv_chat_agent);
            String strFormat = getResources().getString(R.string.txt_lbl_agent);
            String value = String.format(strFormat, agentName);
            agent.setText(value);

            TextView start = (TextView) findViewById(R.id.tv_chat_start);
            Date dateObj = Calendar.getInstance().getTime();
            start.setText(Util.getFormattedTime(dateObj));

            String user = "";
            if (Util.isAuthenticated(this)) {
                if (Constants.getUser(this) != null) {
                    user = Constants.getUser(this).nombre;
                }
            }
            chatMsgAdapter.setSessionDetails(dateObj.getTime(), agentName, user);
        }
    }

    private void handleAgentChatMsg(HashMap<String, String> data) {

        int msgCount = Integer.parseInt(data.get(ChatEventsNext.MSG_COUNT));
        int id = Integer.parseInt(data.get(ChatEventsNext.MSG_START_ID));
        for (int i = 0; i < msgCount; i++, id++) {
            String text = data.get("text" + id);
            if (!TextUtils.isEmpty(text)) {
                if (!data.get("source" + id).equals("visitor")) {
                    String clean = Html.fromHtml(text).toString();
                    Date dateObj = CHAT_TIME_PARSER.parse(data.get("time" + id), new ParsePosition(0));
                    chatMessages.add(new Message(clean, false, dateObj.getTime()));
                    Log.d(TAG, "chatMessages[" + i + "]:" + text);
                }
            }

        }
        chatMsgAdapter.notifyDataSetChanged();
        chatListView.setSelection(chatMessages.size() - 1);
    }

    public void onHome(View view) {

        if (chatState != null && chatState.equals("chatting")) {
            endChat();
        } else {
            Log.d(TAG, "Closing before chat has started");
        }
        super.onHome(view);
    }

    public void onMenu(View view) {
        animate();
    }

    private void endChat() {

        EndChat apiInfo = new EndChat(eventsUri);
        AsyncRestHelperChat helper = new AsyncRestHelperChat(apiInfo);
        helper.execute();
    }

    private void getChatInfo() {

        ChatInfo apiInfo = new ChatInfo(infoUri);
        AsyncRestHelperChat helper = new AsyncRestHelperChat(apiInfo);
        helper.execute();
    }

    public void onAddLines(View view) {

        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        String text = sentText.getText().toString();

        if (text.isEmpty()) {
            return;
        }
        chatMessages.add(new Message(text, true, System.currentTimeMillis()));
        chatMsgAdapter.notifyDataSetChanged();
        sentText.setText("");

        chatListView.setSelection(chatMessages.size() - 1);
        AddChatLines apiInfo = new AddChatLines(eventsUri, text);
        AsyncRestHelperChat helper = new AsyncRestHelperChat(apiInfo);
        helper.execute();
    }

    //*****************SUBSCRIBE METHODS*****************
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskMPosResultEventChat event) {

        boolean errorExists = true;
        HashMap<String, String> data;

        if (event.getResult() != null) {
            data = event.getResult();
            System.out.println("entra al AsyncTaskMPosResultEventChat");
            for(Object objname:data.keySet()) {
                System.out.println("data");
                System.out.println(objname);
                System.out.println(data.get(objname));
            }
            if (event.getApiName().equals(GetBaseResource.APINAME)) {
                if (data.containsKey(GetBaseResource.CHAT_REQUEST)) {
                    requestChatUri = data.get(GetBaseResource.CHAT_REQUEST);
                    sendChatRequest(requestChatUri);
                    Log.d(TAG, "Es "+requestChatUri);
                    errorExists = false;
                } else {
                    Log.e(TAG, "GetBaseResource fail");
                }
            } else if (event.getApiName().equalsIgnoreCase(ChatRequest.APINAME)) {
                if (data.containsKey(ChatRequest.LOCATION)) {
                    chatSessionUri = data.get(ChatRequest.LOCATION);
                    errorExists = false;
                    getChatResourceEventsInfo();
                } else {
                    Log.e(TAG, "ChatRequest fails");
                }
            } else if (event.getApiName().equalsIgnoreCase(ChatResourceEventsInfo.APINAME)) {
                if (data.containsKey(ChatResourceEventsInfo.EVENTS_LINK)) {
                    eventsUri = data.get(ChatResourceEventsInfo.EVENTS_LINK);
                    infoUri = data.get(ChatResourceEventsInfo.INFO_LINK);
                    nextUri = data.get(ChatResourceEventsInfo.EVENT_NEXT);
                    if (data.get(ChatResourceEventsInfo.CHAT_STATE).equals("chatting")) {
                        handleAgentChatMsg(data);
                        updateChatHeader(data.get(ChatResourceEventsInfo.AGENT_NAME));
                        errorExists = false;
                        animate();
                        getChatEventsNext();
                    } else {
                        getChatInfo();
                    }
                    Log.d(TAG, "Got chat events and info");
                    Log.d(TAG, "eventsUri: " + eventsUri);
                    Log.d(TAG, "nextUri: " + nextUri);
                } else {
                    Log.e(TAG, "ChatResourceEventsInfo fails");
                }
            } else if (event.getApiName().equalsIgnoreCase(ChatInfo.APINAME)) {
                if (data.containsKey(ChatInfo.CHAT_STATE)) {
                    chatState = data.get(ChatInfo.CHAT_STATE);
                    if (chatState.equals("chatting")) {
                        agentName = data.get(ChatInfo.AGENT_NAME);
                        Log.d(TAG, "Agent name: " + agentName);
                        updateChatHeader(data.get(ChatInfo.AGENT_NAME));
                        animate();
                        getChatEventsNext(); // now keep polling for details
                    } else if (chatState.equals("ended")) {
                        Log.d(TAG, "info: Chat state ended");
                        finish();
                    }  else {
                        getChatInfo();
                        Log.d(TAG, "Still waiting for agent");
                    }
                    errorExists = false;

                } else {
                    Log.e(TAG, "ChatInfo fails");
                }
            } else if (event.getApiName().equalsIgnoreCase(AddChatLines.APINAME)) {
                if (data.containsKey(AddChatLines.RESP_CODE)) {
                    errorExists = false;
                } else {
                    Log.e(TAG, "AddChatLines fails");
                }
            } else if (event.getApiName().equalsIgnoreCase(ChatEventsNext.APINAME)) {
                if (data.containsKey(ChatEventsNext.CHAT_STATE)) {
                    String chatState = data.get(ChatEventsNext.CHAT_STATE);
                    if (chatState.equals("chatting")) {
                        nextUri = data.get(ChatEventsNext.EVENT_NEXT);
                        handleAgentChatMsg(data);
                    } else if (chatState.equals("ended")) {
                        finish();
                        Log.d(TAG, "next: chat state ended");
                    }
                    errorExists = false;
                    Log.d(TAG, "Got new chat events");
                }
                getChatEventsNext();

            } else if (event.getApiName().equalsIgnoreCase(EndChat.APINAME)) {
                if (data.containsKey(EndChat.RESP_CODE)) {
                    //finish();
                    errorExists = false;
                } else {
                    Log.e(TAG, "EndChat fails");
                }
            }
        }

        if (errorExists) {
            return;
        }

    }

}
