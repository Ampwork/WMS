package com.ampwork.workdonereportmanagement.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.SubjectModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {


    private Context context;
    List<SubjectModel> subjectModelList = new ArrayList<>();
    List<SubjectModel> mFilteredList = new ArrayList<>();


    private RecycleItemViewClicked itemViewClicked;


    public interface RecycleItemViewClicked {
        public void onItemViewSelected(SubjectModel model, int id);
    }

    public SubjectListAdapter(Context context, List<SubjectModel> subjectModels,
                              RecycleItemViewClicked recycleItemViewClicked) {
        this.context = context;
        this.subjectModelList = subjectModels;
        this.mFilteredList = subjectModels;
        this.itemViewClicked = recycleItemViewClicked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String subject = mFilteredList.get(position).getSubjectName();
        String program = mFilteredList.get(position).getProgramName();

        holder.tvSubjectName.setText(subject);
        holder.tvProgramName.setText(program);


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.subject_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                itemViewClicked.onItemViewSelected(mFilteredList.get(position), 0);
                                break;

                            case R.id.update:
                                itemViewClicked.onItemViewSelected(mFilteredList.get(position), 1);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toLowerCase();
                if (charString.isEmpty()) {
                    mFilteredList = subjectModelList;
                } else if (charString.equals("all")) {
                    mFilteredList = subjectModelList;
                } else {
                    List<SubjectModel> filteredList = new ArrayList<>();
                    for (SubjectModel data : subjectModelList) {
                        if (data.getProgramName().toLowerCase().contains(charString) ||
                                data.getSubjectName().toLowerCase().contains(charString)) {
                            filteredList.add(data);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (List<SubjectModel>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<SubjectModel> studentDetails) {
        this.notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvProgramName;
        ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubjectName = itemView.findViewById(R.id.subjectTv);
            imageButton = itemView.findViewById(R.id.updateBtn);
            tvProgramName = itemView.findViewById(R.id.programTv);
        }
    }

}
