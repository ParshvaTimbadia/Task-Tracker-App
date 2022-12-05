package edu.northeastern.numad22fa_team51_project.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.numad22fa_team51_project.R;
import edu.northeastern.numad22fa_team51_project.models.Task;

public class TaskListItemAdapter extends RecyclerView.Adapter<TaskListItemAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Task> list;

    public TaskListItemAdapter(Context context, ArrayList<Task> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        int wdith = (int) Math.round(parent.getWidth()*0.7);
        int newHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
        //Use RelativeLayout.LayoutParams if your parent is a RelativeLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                wdith, newHeight);
        params.setMargins((int)((15/ Resources.getSystem().getDisplayMetrics().density)*Resources.getSystem().getDisplayMetrics().density),
                0, (int)((40/ Resources.getSystem().getDisplayMetrics().density)*Resources.getSystem().getDisplayMetrics().density),
                0);
        v.setLayoutParams(params);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = list.get(position);
        if (position == list.size() - 1){
            holder.taskList.setVisibility(View.VISIBLE);
            holder.task_items.setVisibility(View.GONE);
        }else{
            holder.taskList.setVisibility(View.GONE);
            holder.task_items.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView taskList;
        private LinearLayout task_items;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskList = itemView.findViewById(R.id.tv_add_task_list);
            task_items = itemView.findViewById(R.id.ll_task_item);

        }
    }

}
