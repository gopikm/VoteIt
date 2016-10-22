package bitterbug.hack;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import bitterbug.hack.custom.ThreadModel;

/**
 * Created by User on 4/8/2016.
 */
public class ThreadAdapter extends ArrayAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private List data=new ArrayList();
    private static LayoutInflater inflater=null;
    public Resources res;
    ThreadModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public ThreadAdapter (Context context,int resLocal) {

        /********** Take passed values **********/
       super(context,resLocal);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return this.data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void add(ThreadModel ta){
        data.add(ta);
        super.add(ta);
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView dat;
        public TextView pol;
        public TextView comm;


    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi=inflater.inflate(R.layout.thread_item,parent,false);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.dat = (TextView) vi.findViewById(R.id.threadqsn);
            holder.pol=(TextView)vi.findViewById(R.id.polls);
            holder.comm=(TextView)vi.findViewById(R.id.comments);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.dat.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ThreadModel ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.dat.setText( tempValues.getThreaddata() );
            holder.pol.setText( tempValues.getPolls()+" votes");
            holder.comm.setText(tempValues.getComments()+" comments");

            /******** Set Item Click Listner for LayoutInflater for each row *******/


        }
        return vi;
    }

//    @Override
//    public void onClick(View v) {
//        //Log.v("CustomAdapter", "=====Row button clicked=====");
//    }

    /********* Called when Item click in ListView ************/
//    private class OnItemClickListener  implements View.OnClickListener {
//        private int mPosition;
//
//        OnItemClickListener(int position){
//            mPosition = position;
//        }
//
//        @Override
//        public void onClick(View arg0) {
//
//
//            forum sct = (forum);
//
//            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
//
//            sct.onItemClick(mPosition);
//        }
//    }
}