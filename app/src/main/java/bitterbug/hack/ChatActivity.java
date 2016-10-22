package bitterbug.hack;

/**
 * Created by Gopi on 08-Apr-16.
 */
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import bitterbug.hack.custom.ThreadModel;


public class ChatActivity extends AppCompatActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    String ip="http://117.196.152.164:8000";
    String line,tentative;
    StringBuffer str;
    ThreadModel tm;
    TextView topic;
    int i,chosen;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tm=(ThreadModel) getIntent().getSerializableExtra("obj");
        System.out.println(tm.getThreaddata());
        topic=(TextView)findViewById(R.id.threadtopic);
        initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);
        loadHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);
                messageET.setText("");
                displayMessage(chatMessage);
                sendmessage(messageText);
            }
        });


    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadHistory(){

        chatHistory = new ArrayList<ChatMessage>();
        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONObject json=new JSONObject();
                    json.put("view",true);
                    json.put("user","vaanam");
                    URL url = new URL(ip + "/threads/"+tm.getId()+"/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty("content-type", "application/json");
                    Writer writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(String.valueOf(json));
                    writer.flush();
                    writer.close();
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    str = new StringBuffer();
                    while ((line = reader.readLine()) != null)
                        str.append(line);
                    input.close();
                    line=str.toString();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }catch (JSONException j){
                    j.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try{
                    JSONObject jsonObject=new JSONObject(line);
                    topic.setText(tm.getThreaddata());
                    tentative=jsonObject.getString("options");
                    System.out.println(tentative);
                    StringTokenizer st = new StringTokenizer(tentative,"|");
                    System.out.println(tentative+" ");
                    chosen=jsonObject.getInt("chosen");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    LinearLayout l=(LinearLayout)findViewById(R.id.Blayout);
                    RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                    radioGroup.setOrientation(RadioGroup.HORIZONTAL);
                    l.addView(radioGroup,params);
                    for(i=1;i<=st.countTokens()+1;i++)
                    {
                        RadioButton btn=new RadioButton(getApplicationContext());
                        String S=st.nextToken();
                        System.out.println(S + i);
                        btn.setId(i);
                        btn.setTextColor(Color.rgb(0, 0, 0));
                        btn.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                        btn.setText(S);
                        radioGroup.setWeightSum(st.countTokens());
                        LinearLayout.LayoutParams btnpara=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
                        if(chosen==i)
                            btn.setChecked(true);
                        radioGroup.addView(btn,btnpara);
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(final RadioGroup radioGroup,final int j) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json=new JSONObject();
                                            json.put("user","vaanam");
                                            json.put("option",""+j);
                                            json.put("id", tm.getId());
                                            System.out.println("" + j);
                                            URL url = new URL(ip + "/chooseoption/");
                                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                            connection.setDoOutput(true);
                                            connection.setDoInput(true);
                                            connection.setRequestMethod("POST");
                                            connection.addRequestProperty("content-type", "application/json");
                                            Writer writer = new OutputStreamWriter(connection.getOutputStream());
                                            writer.write(String.valueOf(json));
                                            writer.flush();
                                            writer.close();
                                            InputStream input = connection.getInputStream();
                                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                                            str = new StringBuffer();
                                            while ((line = reader.readLine()) != null)
                                                str.append(line);
                                            input.close();
                                            line=str.toString();

                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }catch (JSONException j){
                                            j.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        });
                    }
                    if(jsonObject.getString("m").equals("y")) {
                        jsonArray = jsonObject.getJSONArray("messages");
                        messagesContainer.setAdapter(adapter);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject eachMsg = jsonArray.getJSONObject(i);
                            ChatMessage msg = new ChatMessage();
                            msg.setId(1);
                            System.out.println(eachMsg.getString("user"));
                            if (eachMsg.getString("user").equals("vaanam"))
                                msg.setMe(true);
                            else
                                msg.setMe(false);
                            msg.setMessage(eachMsg.getString("text"));
                            // System.out.println(eachMsg.getString("text"));
                            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                            chatHistory.add(msg);
                            displayMessage(msg);
                        }
                    }


                }catch(JSONException j){
                    j.printStackTrace();
                }

            }
        }.execute(null, null, null);


    }

    public void sendmessage(final String message)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONObject json =new JSONObject();
                    json.put("user","vaanam");
                    json.put("message",message);
                    json.put("view",false);
                    URL url = new URL(ip + "/threads/"+tm.getId()+"/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty("content-type", "application/json");
                    Writer writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(String.valueOf(json));
                    writer.flush();
                    writer.close();
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    str = new StringBuffer();
                    while ((line = reader.readLine()) != null)
                        str.append(line);
                    input.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }catch (JSONException j){
                    j.printStackTrace();
                }
                return null;
            }
        }.execute(null, null, null);
    }
}
