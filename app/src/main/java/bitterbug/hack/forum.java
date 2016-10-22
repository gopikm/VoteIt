package bitterbug.hack;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.ArrayList;

import bitterbug.hack.custom.ThreadModel;

/**
 * Created by Gopi on 08-Apr-16.
 */
public class forum extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";


    ListView list;
    ThreadAdapter adapter;
    public forum CustomListView = null;
    public  ArrayList<ThreadModel> CustomListViewValuestrend;

    private int mPage;
    JSONArray jsonArray;
    String line;
    StringBuffer str;
    String ip="http://117.196.152.164:8000";
    ListView tre;

    public static forum newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        forum fragment = new forum();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forum, container, false);
        CustomListViewValuestrend = new ArrayList<ThreadModel>();

        CustomListView = this;

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        setListData();


       tre= (ListView) view.findViewById( R.id.trending );  // List defined in XML ( See Below )


        /**************** Create Custom Adapter *********/
        adapter=new ThreadAdapter( getContext(),R.id.trending );

        tre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ThreadModel tm = (ThreadModel) adapter.getItem(position);
                Intent intent = new Intent(getContext(),ChatActivity.class).putExtra("obj",tm);
                startActivity(intent);
            }
        });

        getThreads();
        return view;
    }

    public void setListData()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONObject json=new JSONObject();
                    json.put("user","abc");
                    json.put("view",true);
                    URL url = new URL(ip + "/threads/");
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
                    JSONArray jsonArray=jsonObject.getJSONArray("threads");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        final ThreadModel sched = new ThreadModel();
                        JSONObject json=jsonArray.getJSONObject(i);
                        sched.setThreaddata(json.getString("question"));
                        System.out.println(sched.getThreaddata());
                        sched.setComments(json.getInt("comments"));
                        System.out.println(sched.getComments());
                        sched.setPolls(json.getInt("polls"));
                        System.out.println(sched.getPolls());
                        sched.setId(Integer.parseInt(json.getString("id")));
                        System.out.println(sched.getId());
                        adapter.add(sched);
                    }
                    tre.setAdapter(adapter);
                }catch(JSONException j){
                    j.printStackTrace();
                }


            }
        }.execute(null, null, null);
    }




    public void getThreads()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONObject json=new JSONObject();
                    json.put("view",true);
                    URL url = new URL(ip + "/threads/");
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
                    jsonArray=jsonObject.getJSONArray("threads");
                }catch(JSONException j){
                    j.printStackTrace();
                }


            }
        }.execute(null, null, null);

    }
}
