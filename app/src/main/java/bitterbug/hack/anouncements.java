package bitterbug.hack;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Gopi on 08-Apr-16.
 */
public class anouncements extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    JSONArray jsonArray;
    String line;
    StringBuffer str;
    String ip="http://117.196.152.164:8000";
    ListView list;
    ArrayAdapter<String> adapter;
    public static anouncements newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        anouncements fragment = new anouncements();
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
        View view = inflater.inflate(R.layout.anouncements, container, false);
        list = (ListView) view.findViewById(R.id.list1);

        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1);
        getAnouncements();


        // TextView textView = (TextView) view.findViewById(R.id.but1);
       // textView.setText("Fragment #" + mPage);
        return view;
    }

    public void getAnouncements()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONObject json=new JSONObject();
                    json.put("view",true);
                    URL url = new URL(ip + "/announcements/");
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
                    jsonArray=jsonObject.getJSONArray("announcements");
                    String[] values = new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        try {
                            values[i]=jsonArray.getJSONObject(i).getString("announcement");
                            System.out.println(values[i]);
                            adapter.add(values[i]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(adapter);
                    list.setAdapter(adapter);
                }catch(JSONException j){
                    j.printStackTrace();
                }


            }
        }.execute(null, null, null);

    }
}
