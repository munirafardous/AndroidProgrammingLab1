package com.example.munirafardous.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.munirafardous.lab1.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Messages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MessageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MessageListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    protected static final String ACTIVITY_NAME = "ChatWindow";
    static ListView listView = null;
    EditText editText;
    static Button sendButton = null;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
*/

        //setContentView(R.layout.activity_chat_window);

        final ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(MessageListActivity.this); // Step 5 of Lab 5
        db = dbHelper.getWritableDatabase(); // Step 5 of Lab 5

        ListView listView = (ListView) findViewById(R.id.listView);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button sendButton = (Button) findViewById(R.id.button3);
        messages = new ArrayList<String>();
try {
    messageAdapter = new ChatAdapter(this);
    listView.setAdapter(messageAdapter);

    sendButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            messages.add(editText.getText().toString());

            ContentValues contentValues = new ContentValues(); // Step 6 for Lab 5
            contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, editText.getText().toString()); // Step 6 for Lab 5
            db.insert(ChatDatabaseHelper.CHAT_TABLE, "", contentValues); // Step 6 for Lab 5- insert takes 3 parameters- tablename,
            //nullColumnHack to check for the null columns, ContentValues object

            messageAdapter.notifyDataSetChanged();
            editText.setText("");

        }
    });


    if (findViewById(R.id.message_detail_container) != null) {
        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        mTwoPane = true;
    }
}catch (Exception e)
{
    Log.d("Excption" , e.getMessage());
}
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }


    private class ChatAdapter extends ArrayAdapter {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            messages.clear();
            // Step 5 of Lab 5
            Cursor cursor;
            cursor = db.rawQuery(ChatDatabaseHelper.READALL_CHAT_TABLE, null); //it takes 2 parameters- string sql and string[] selectionArgs
            int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);

            // Print an information message about the Cursor
            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
            // Then use a for loop to print out the name of each column returned by the cursor.
            for (int colIndex = 0; colIndex < cursor.getColumnCount(); colIndex++) {
                Log.i(ACTIVITY_NAME, "Column name of " + colIndex + " = " + cursor.getColumnName(colIndex));
            }

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                messages.add(cursor.getString(messageIndex));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(messageIndex));
                cursor.moveToNext();
            }


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

            final String messageText = getItem(position);

            LayoutInflater inflater = MessageListActivity.this.getLayoutInflater();

            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(messageText);

            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, messageText);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, messageText);

                        context.startActivity(intent);
                    }
                }
            });


            return result;
        }


    }
}

