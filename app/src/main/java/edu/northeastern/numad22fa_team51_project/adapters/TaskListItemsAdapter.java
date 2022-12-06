package edu.northeastern.numad22fa_team51_project.adapters;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.Distribution;

import java.util.ArrayList;

import edu.northeastern.numad22fa_team51_project.R;
import edu.northeastern.numad22fa_team51_project.models.TaskSerializableModel;

public class TaskListItemsAdapter extends RecyclerView.Adapter<TaskListItemsAdapter.TaskCardViewHolder> {

    private Context context;
    private ArrayList<TaskSerializableModel> arrCards;

    public TaskListItemsAdapter(Context context, ArrayList<TaskSerializableModel> arrCards) {
        this.context = context;
        this.arrCards = arrCards;
    }

    @NonNull
    @Override
    public TaskCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        TaskCardViewHolder vh = new TaskCardViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCardViewHolder holder, int position) {
        holder.card_name.setText(arrCards.get(position).getCard_name());
         // TODO: array shown as string, need to change as needed!!
        holder.members_name.setText(arrCards.get(position).getAssignedTo().toString());

        View parentLayout = (View) ((Activity) context).findViewById(R.id.card_create_root_layout);
    }

    @Override
    public int getItemCount() {
        return arrCards.size();
    }

    public static class TaskCardViewHolder extends RecyclerView.ViewHolder{

        TextView card_name;
        TextView members_name;
        LinearLayout card_row;

        public TaskCardViewHolder(@NonNull View itemView) {
            super(itemView);
            card_name = itemView.findViewById(R.id.text_view_card_name);
            members_name = itemView.findViewById(R.id.text_view_members_name);
            card_row = itemView.findViewById(R.id.card_row);
        }
    }
}
