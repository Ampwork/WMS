package com.ampwork.workdonereportmanagement.faculty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class FacultyReportAdapter extends RecyclerView.Adapter<FacultyReportAdapter.ViewHolder> {

    private Context context;
    private List<GenerateReportResponse.ReportResponseModel> reportResponseModelList = new ArrayList<>();
    private List<GenerateReportResponse.ReportResponseModel> mFilteredList = new ArrayList<>();

    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(GenerateReportResponse.ReportResponseModel reportModel);
    }

    public FacultyReportAdapter(Context context, List<GenerateReportResponse.ReportResponseModel> reportModels, RecycleItemViewClicked itemViewClicked) {
        this.context = context;
        this.reportResponseModelList = reportModels;
        this.mFilteredList = reportModels;
        this.itemViewClicked = itemViewClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_header_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String month = mFilteredList.get(position).getMonth();
        String year = mFilteredList.get(position).getYear();
        String program = mFilteredList.get(position).getProgram();
        String leave = mFilteredList.get(position).getLeaves();
        String status = mFilteredList.get(position).getStatus();

        holder.tvMonthYear.setText(month + "\n" + year);
        holder.tvProgram.setText(program);
        holder.tvLeave.setText("Leaves Taken : " + leave);
        holder.tvStatus.setText("Report Status : " + status);
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
                    mFilteredList = reportResponseModelList;
                } else {
                    List<GenerateReportResponse.ReportResponseModel> filteredList = new ArrayList<>();
                    for (GenerateReportResponse.ReportResponseModel data : reportResponseModelList) {
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
        TextView tvReportId, tvMonthYear, tvProgram, tvLeave, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewMain = itemView.findViewById(R.id.cardView);
            tvMonthYear = itemView.findViewById(R.id.monthYearTv);
            tvProgram = itemView.findViewById(R.id.programTv);
            tvLeave = itemView.findViewById(R.id.leaveTv);
            tvStatus = itemView.findViewById(R.id.statusTv);
        }
    }
}