package com.ampwork.workdonereportmanagement.faculty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.adapter.StudentDetailsAdapter;
import com.ampwork.workdonereportmanagement.model.StudentDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {

    private Context context;
    private List<StudentDetailsModel> detailsModels = new ArrayList<>();
    private List<StudentDetailsModel> finalDetailsModels = new ArrayList<>();
    private List<StudentDetailsModel> mFilteredList = new ArrayList<>();

    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(StudentDetailsModel detailsModel);
    }

    public StudentAttendanceAdapter(Context context, List<StudentDetailsModel> detailsModels) {
        this.context = context;
        this.detailsModels = detailsModels;
        this.mFilteredList = detailsModels;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(context, R.layout.dept_list_item, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_att_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String name = mFilteredList.get(position).getName();
        String usn = mFilteredList.get(position).getUsn();

        holder.tvName.setText(name);
        holder.tvUsn.setText(usn);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    mFilteredList.get(position).setChecked(true);

                }else {
                    mFilteredList.get(position).setChecked(false);
                }
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
        TextView tvName, tvUsn;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.nameTv);
            tvUsn = itemView.findViewById(R.id.usnTv);
            checkBox = itemView.findViewById(R.id.chk);
        }
    }

    public List<StudentDetailsModel> updatedList(){
        return mFilteredList;
    }
}
