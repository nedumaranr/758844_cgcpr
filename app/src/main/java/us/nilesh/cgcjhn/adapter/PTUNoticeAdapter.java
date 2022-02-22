package us.nilesh.cgcjhn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import us.nilesh.cgcjhn.R;


public class PTUNoticeAdapter extends RecyclerView.Adapter<PTUNoticeAdapter.ViewHolder> {

    private List<String> mPtuNotice;
    private LayoutInflater mInflater;

    public PTUNoticeAdapter(Context context, List<String> ptuNotice){
        this.mInflater = LayoutInflater.from(context);
        this.mPtuNotice = ptuNotice;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ptu_notice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.noticeTxt.setText("Notice: #"+mPtuNotice.get(position));
    }

    @Override
    public int getItemCount() {
        return mPtuNotice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noticeTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTxt = itemView.findViewById(R.id.ptu_notice_num);
//            noticeNum=itemView.findViewById(R.id.all_notice_num);
        }
    }
}
