package com.manhdong.recycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Saphiro on 7/18/2016.
 */
public class NewsAdapter extends RecyclerView.Adapter <NewsAdapter.myViewHolder>{

  //  public static final Locale  VIETNAM = Locale constant for en_US;
    Context mContext;
    int mLayout;
    List<News> mList;
    public static final Locale VIETNAMESE = new Locale("vi", "VN");

    public NewsAdapter(Context context, int resLayout, List<News> myList) {
        mContext = context;
        mLayout = resLayout;
        mList = myList;

    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mLayout,parent,false);
        myViewHolder myview = new myViewHolder(view);

        return myview;
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {

        //nơi set dữ liệu
        holder.newsTitle.setText(mList.get(position).getTitle());
        holder.newsDesc.setText(mList.get(position).getDescription());
        String time = mList.get(position).getPubDate();
//        String inputformat = "EEE, d MMM yyyy HH:mm:ss Z";
//        String outputformat = "EEE, d MMM yyyy HH:mm:ss Z";
//
//        Locale VIETNAM = new Locale("Vietnamese", "VIETNAM", "vi_VN");
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
//        SimpleDateFormat vnf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", new Locale("vi", "VN"));

        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));




//        String localizedDate = localizeDate(time, new Locale("vi","VN"));
//        Log.d("myLocal", localizedDate);
//        localizedDate = localizeDate("Mon, 18 Jul 2016 11:46:00 +0900", new Locale("vi", "VN"));
//        Log.d("myLocal2", localizedDate);


//        try {
//            Date date = sdf.parse(time);
//            vnf.format(date);
//            Date date2 = vnf.parse(time);
//            Log.d("MYDATE", "onBindViewHolder: "+ date);
//            Log.d("MYDATE2", "onBindViewHolder: "+ date2);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        holder.newsPub.setText(localizeDate(time, VIETNAMESE));
        Picasso.with(mContext).load(mList.get(position).getImageLink()).into(holder.newsPic);

        holder.newsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (v.getId() == holder.newsItem.getId()){
//                    String link = mList.get(position).getLink();
//                    Log.d("LLL", link);
//                }
                String link = mList.get(position).getLink();
                Log.d("LLL", link);

            //    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                Intent intent = new Intent(mContext,ViewNews.class);
                intent.putExtra("Link", link);
                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsDesc;
        TextView newsPub;
        ImageView newsPic;
        CardView newsItem;
        public myViewHolder(final View itemView) {
            super(itemView);
            newsItem = (CardView) itemView;
            newsTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            newsDesc = (TextView) itemView.findViewById(R.id.tvDescription);
            newsPub = (TextView) itemView.findViewById(R.id.tvPubDate);
            newsPic = (ImageView) itemView.findViewById(R.id.newsPic);


        }

    }

    private String localizeDate(String inputdate, Locale locale) {

        Date date = new Date();
        SimpleDateFormat dateFormatOutPut = new SimpleDateFormat("EEEE, d MMMM yyyy HH:mm:ss", locale);
        SimpleDateFormat dateFormatInput = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");


        try {
            date = dateFormatInput.parse(inputdate);
        } catch (ParseException e) {
            //log.warn("Input date was not correct. Can not localize it.");
            return inputdate;
        }

        return dateFormatOutPut.format(date);
    }

}




