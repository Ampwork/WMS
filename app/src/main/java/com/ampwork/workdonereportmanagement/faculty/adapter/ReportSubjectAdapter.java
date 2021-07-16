package com.ampwork.workdonereportmanagement.faculty.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;

import java.util.ArrayList;
import java.util.List;

public class ReportSubjectAdapter extends RecyclerView.Adapter<ReportSubjectAdapter.ViewHolder> {

    private Context context;
    private List<GenerateReportResponse.ReportSubject> reportModels = new ArrayList<>();


    public ReportSubjectAdapter(Context context, List<GenerateReportResponse.ReportSubject> reportModels) {
        this.context = context;
        this.reportModels = reportModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(context, R.layout.dept_list_item, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_suibject_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String subject = reportModels.get(position).getSubject_name();
        String subjectCode = reportModels.get(position).getSubject_code();
        String percentage = reportModels.get(position).getPercentage();
        String desc = reportModels.get(position).getDescription();

        holder.tvSubject.setText(subject);
        holder.tvSubjectCode.setText(subjectCode);
        holder.tvSubjectPercentage.setText(percentage);
        if (!TextUtils.isEmpty(desc)){
            holder.tvDesc.setText(desc);
        }else {
            holder.tvDesc.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubject, tvSubjectCode, tvSubjectPercentage, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubject = itemView.findViewById(R.id.subjectTv);
            tvSubjectCode = itemView.findViewById(R.id.subjectCodeTv);
            tvSubjectPercentage = itemView.findViewById(R.id.PercentageTv);
            tvDesc = itemView.findViewById(R.id.descTv);

        }
    }
}
