package us.nilesh.cgcjhn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import us.nilesh.cgcjhn.R;
import us.nilesh.cgcjhn.interfaces.NoticeInterface;


public class PubNoticeAdapter extends RecyclerView.Adapter<PubNoticeAdapter.ViewHolder> {

    private List<String> allTitle, allContent, allDate, allTime;
    private LayoutInflater mInflater;
    private NoticeInterface noticeInterface;
    private final static String SPLIT_VAL="696969";
    private String DocName;
    private String desc;

    public PubNoticeAdapter(Context context, List<String> Title,List<String> Content, List<String> Date, List<String> Time, NoticeInterface noticeInterface){
        this.mInflater = LayoutInflater.from(context);
        this.noticeInterface = noticeInterface;
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
        if (allContent.get(position).contains(SPLIT_VAL)){
            String[] array= allContent.get(position).split(SPLIT_VAL);
            desc=array[0];
            holder.noticeContent.setText(desc);
            DocName =array[1];
        }else {
            desc=allContent.get(position);
            holder.noticeContent.setText(desc);
        }
        holder.noticeTitle.setText(allTitle.get(position));
        holder.noticeNum.setText(String.valueOf(position+1));
        holder.noticeDate.setText(allDate.get(position));
        holder.noticeTime.setText(allTime.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allContent.get(position).contains(SPLIT_VAL)){
                    String[] array= allContent.get(position).split(SPLIT_VAL);
                    desc=array[0];
                    DocName =array[1];
                    noticeInterface.onClickNotice(DocName,allTitle.get(position),desc);
                }else {
                    desc=allContent.get(position);
                    noticeInterface.onClickNotice(null,allTitle.get(position),desc);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTitle.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noticeTitle, noticeContent,noticeNum, noticeDate, noticeTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTitle = itemView.findViewById(R.id.noticeTitleItem);
            noticeContent =itemView.findViewById(R.id.noticeContentItem);
            noticeNum=itemView.findViewById(R.id.itemNum);
            noticeDate =itemView.findViewById(R.id.dateItem);
            noticeTime =itemView.findViewById(R.id.timeItem);
        }
    }
}
