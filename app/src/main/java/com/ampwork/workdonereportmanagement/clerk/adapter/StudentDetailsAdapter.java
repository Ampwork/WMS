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
import com.ampwork.workdonereportmanagement.model.StudentDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class StudentDetailsAdapter extends RecyclerView.Adapter<StudentDetailsAdapter.ViewHolder> {

    private Context context;
    private List<StudentDetailsModel> detailsModels = new ArrayList<>();
    private List<StudentDetailsModel> mFilteredList = new ArrayList<>();

    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(StudentDetailsModel detailsModel);
    }

    public StudentDetailsAdapter(Context context, List<StudentDetailsModel> detailsModels,
                                 RecycleItemViewClicked itemViewClicked) {
        this.context = context;
        this.detailsModels = detailsModels;
        this.mFilteredList = detailsModels;
        this.itemViewClicked = itemViewClicked;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(context, R.layout.dept_list_item, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String name = mFilteredList.get(position).getName();
        String sem = mFilteredList.get(position).getSemester();

        holder.tvName.setText(name);
        holder.tvSemester.setText("Semester " + sem);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.student_edit, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                itemViewClicked.onItemViewSelected(mFilteredList.get(position));
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
        if (mFilteredList == null) {
            return 0;
        } else {
            return mFilteredList.size();
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toLowerCase();
                if (charString.isEmpty()) {
                    mFilteredList = detailsModels;
                } else if (charString.equals("all")){
                    mFilteredList = detailsModels;
                }else {
                    List<StudentDetailsModel> filteredList = new ArrayList<>();
                    for (StudentDetailsModel data : detailsModels) {
                        if (data.getSemester().toLowerCase().contains(charString)) {
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
                mFilteredList = (List<StudentDetailsModel>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<StudentDetailsModel> studentDetails) {
        this.notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSemester;
        ImageButton btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.nameTv);
            tvSemester = itemView.findViewById(R.id.semTv);
            btnEdit = itemView.findViewById(R.id.editBtn);

        }
    }
}