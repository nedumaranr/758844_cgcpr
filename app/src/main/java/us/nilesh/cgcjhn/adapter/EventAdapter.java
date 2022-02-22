package us.nilesh.cgcjhn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import us.nilesh.cgcjhn.interfaces.EnquiryInterface;
import us.nilesh.cgcjhn.R;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<String> allTitle, allContent, allDate, allTime;
    private final LayoutInflater mInflater;
    private final EnquiryInterface enquiryInterface;
    private static final String TAG="eventadapter";

    public EventAdapter(Context context, List<String> Title,List<String> Content, List<String> Date, List<String> Time, EnquiryInterface enquiryInterface){
        this.mInflater = LayoutInflater.from(context);
        this.enquiryInterface = enquiryInterface;
        this.allTitle = Title;
        this.allContent = Content;
        this.allDate=Date;
        this.allTime=Time;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.eventTitle.setText(allTitle.get(position));
        holder.eventContent.setText(allContent.get(position));
        holder.noticeNum.setText(String.valueOf(position+1));
        holder.eventDate.setText(allDate.get(position));
        holder.eventTime.setText(allTime.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onBindViewHolder: "+allContent.get(position).toString());
                enquiryInterface.onClickEnquiry(String.valueOf(allContent.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTitle.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventContent,noticeNum, eventDate,eventTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.noticeTitleItem);
            eventContent =itemView.findViewById(R.id.noticeContentItem);
            noticeNum=itemView.findViewById(R.id.itemNum);
            eventDate =itemView.findViewById(R.id.dateItem);
            eventTime=itemView.findViewById(R.id.timeItem);
        }
    }
}
