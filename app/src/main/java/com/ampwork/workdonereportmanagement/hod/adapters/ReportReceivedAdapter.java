package com.ampwork.workdonereportmanagement.hod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.adapter.ReportAdapter;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.model.ReportModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ReportReceivedAdapter extends RecyclerView.Adapter<ReportReceivedAdapter.ViewHolder> {

    private Context context;
    private List<GenerateReportResponse.ReportResponseModel> reportModels = new ArrayList<>();
    private List<GenerateReportResponse.ReportResponseModel> mFilteredList = new ArrayList<>();

    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(GenerateReportResponse.ReportResponseModel reportModel);
    }

    public ReportReceivedAdapter(Context context, List<GenerateReportResponse.ReportResponseModel> reportModels, RecycleItemViewClicked itemViewClicked) {
        this.context = context;
        this.reportModels = reportModels;
        this.mFilteredList = reportModels;
        this.itemViewClicked = itemViewClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(context, R.layout.dept_list_item, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hod_report_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String name = mFilteredList.get(position).getUsername();
        String month = mFilteredList.get(position).getMonth();
        String year = mFilteredList.get(position).getYear();
        String program = mFilteredList.get(position).getProgram();
        String status = mFilteredList.get(position).getStatus();
        String monthYear = month+","+" "+year;
        holder.tvFullName.setText(name);
        holder.tvMonthYear.setText("Submitted Report for the month "+monthYear);
        holder.tvProgram.setText("Program : "+program);
        holder.tvStatus.setText("Report Status : "+status);
        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemViewClicked.onItemViewSelected(mFilteredList.get(position));
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
                    mFilteredList = reportModels;
                } else {
                    List<GenerateReportResponse.ReportResponseModel> filteredList = new ArrayList<>();
                    for (GenerateReportResponse.ReportResponseModel data : reportModels) {
                        if (data.getMonth().toLowerCase().contains(charString)) {
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
                mFilteredList = (List<GenerateReportResponse.ReportResponseModel>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<GenerateReportResponse.ReportResponseModel> studentDetails) {
        this.notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardViewMain;
        TextView tvFullName, tvMonthYear, tvProgram,tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewMain = itemView.findViewById(R.id.cardView);
            tvFullName = itemView.findViewById(R.id.nameTv);
            tvMonthYear = itemView.findViewById(R.id.monthTv);
            tvProgram = itemView.findViewById(R.id.programTv);
            tvStatus = itemView.findViewById(R.id.statusTv);
        }
    }
}