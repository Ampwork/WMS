package com.ampwork.workdonereportmanagement.faculty.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import com.ampwork.workdonereportmanagement.faculty.activities.EditReportAttendanceActivity;
import com.ampwork.workdonereportmanagement.model.ReportAttendanceModel;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceReportChildAdapter extends RecyclerView.Adapter<AttendanceReportChildAdapter.ViewHolder> {

    private Context context;
    private List<ReportAttendanceModel> reportModels = new ArrayList<>();
    private List<ReportAttendanceModel> mFilteredList = new ArrayList<>();

    String status;
    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(ReportAttendanceModel addReportModel);
    }

    public AttendanceReportChildAdapter(Context context, List<ReportAttendanceModel> reportModels,
                                        RecycleItemViewClicked itemViewClicked, String status) {
        this.context = context;
        this.reportModels = reportModels;
        this.mFilteredList = reportModels;
        this.itemViewClicked = itemViewClicked;
        this.status = status;
    }

    public AttendanceReportChildAdapter(Context context,
                                        List<ReportAttendanceModel> reportModels,
                                        String status) {
        this.context = context;
        this.reportModels = reportModels;
        this.mFilteredList = reportModels;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(context, R.layout.dept_list_item, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_report_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String date = mFilteredList.get(position).getAtt_date();
        String name = mFilteredList.get(position).getStudentName();
        String subject = mFilteredList.get(position).getSubject();
        String semester = mFilteredList.get(position).getSemester();
        String stat = mFilteredList.get(position).getStatus();

        String month = AppUtility.getDateMonth(date);
        String dateStr = AppUtility.getDateFormat(date);
        holder.tvDate.setText(month+"\n"+dateStr);
        holder.tvSemester.setText("Semester : "+semester);
        holder.tvSubject.setText(subject);
        holder.tvStatus.setText("status : "+stat);
        holder.tvName.setText(name);

        if (status.equals("Approved")) {
            holder.btnUpdate.setVisibility(View.GONE);
        } else if (status.equals("Accept")) {
            holder.btnUpdate.setVisibility(View.GONE);
        } else {
            holder.btnUpdate.setVisibility(View.VISIBLE);
        }

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.report_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                Intent intent = new Intent(context, EditReportAttendanceActivity.class);
                                intent.putExtra("data",(Parcelable) mFilteredList.get(position));
                                context.startActivity(intent);
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
                    mFilteredList = reportModels;
                } else if (charString.equals("all")){
                    mFilteredList = reportModels;
                }else {
                    List<ReportAttendanceModel> filteredList = new ArrayList<>();
                    for (ReportAttendanceModel data : reportModels) {
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
                mFilteredList = (List<ReportAttendanceModel>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<ReportAttendanceModel> studentDetails) {
        this.notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView tvDate,tvName,tvSubject,tvSemester,tvStatus;
        ImageButton btnUpdate;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.dateTv);
            tvName = itemView.findViewById(R.id.nameTv);
            tvSubject = itemView.findViewById(R.id.subjectTv);
            tvSemester = itemView.findViewById(R.id.semesterTv);
            tvStatus = itemView.findViewById(R.id.statusTv);
            btnUpdate = itemView.findViewById(R.id.updateBtn);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}