package com.ampwork.workdonereportmanagement.faculty.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.utils.AppUtility;

import java.util.ArrayList;
import java.util.List;

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder> {

    private Context context;
    private List<AddReportModel> reportModels = new ArrayList<>();
    private List<AddReportModel> mFilteredList = new ArrayList<>();

    public MonthlyReportAdapter(Context context, List<AddReportModel> reportModels) {
        this.context = context;
        this.reportModels = reportModels;
        this.mFilteredList = reportModels;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(context, R.layout.dept_list_item, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String date = mFilteredList.get(position).getDate();
        String subject = mFilteredList.get(position).getSubject();
        String semester = mFilteredList.get(position).getSemester();
        String fromTime = mFilteredList.get(position).getFrom_time();
        String toTime = mFilteredList.get(position).getTo_time();
        String totalPresent = mFilteredList.get(position).getTotal_present();
        String totalAbsent = mFilteredList.get(position).getTotal_absent();
        String month = AppUtility.getDateMonth(date);
        String dateStr = AppUtility.getDateFormat(date);
        holder.tvDate.setText(month+"\n"+dateStr);

        holder.tvSubject.setText(subject);
        holder.tvSemester.setText(semester);
        holder.tvTime.setText(fromTime + " " + "to" + " " + toTime);
        if (TextUtils.isEmpty(totalAbsent)) {
            holder.tvTotalAbsent.setText("Total A : " + 0);
        } else {
            holder.tvTotalAbsent.setText("Total A : " + totalAbsent);
        }
        if (TextUtils.isEmpty(totalPresent)) {
            holder.tvTotalPresent.setText("Total P : " + 0);
        } else {
            holder.tvTotalPresent.setText("Total P : " + totalPresent);
        }


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
                    mFilteredList = reportModels;
                } else {
                    List<AddReportModel> filteredList = new ArrayList<>();
                    for (AddReportModel data : reportModels) {
                        if (data.getDate().toLowerCase().contains(charString)) {
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
                mFilteredList = (List<AddReportModel>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<AddReportModel> studentDetails) {
        this.notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvSubject, tvSemester, tvTime, tvTotalPresent, tvTotalAbsent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.dateTv);
            tvSemester = itemView.findViewById(R.id.semTv);
            tvSubject = itemView.findViewById(R.id.subjectTv);
            tvTime = itemView.findViewById(R.id.timeTv);
            tvTotalAbsent = itemView.findViewById(R.id.totalAbsentATv);
            tvTotalPresent = itemView.findViewById(R.id.totalPresentTv);


        }
    }
}