package com.example.munirafardous.lab1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    ListView listView;
    EditText editText;
    Button sendButton;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);



        ListView listView = (ListView) findViewById(R.id.listView);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button sendButton = (Button) findViewById(R.id.button3);
        messages = new ArrayList<String>();


        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messages.add(editText.getText().toString());
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    private class ChatAdapter extends ArrayAdapter {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null ;
            if(position%2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);}
            else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);}

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }
    }


}
