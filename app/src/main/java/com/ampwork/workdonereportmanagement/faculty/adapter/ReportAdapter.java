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
import com.ampwork.workdonereportmanagement.model.ReportModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private Context context;
    private List<ReportModel> reportModels = new ArrayList<>();
    private List<ReportModel> mFilteredList = new ArrayList<>();

    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(ReportModel reportModel);
    }

    public ReportAdapter(Context context, List<ReportModel> reportModels, RecycleItemViewClicked itemViewClicked) {
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
                .inflate(R.layout.report_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String id = mFilteredList.get(position).getReportId();
        String month = mFilteredList.get(position).getMonth();
        String year = mFilteredList.get(position).getYear();
        String program = mFilteredList.get(position).getProgram();
        String leave = mFilteredList.get(position).getLeaves();
        String status = mFilteredList.get(position).getStatus();
        holder.tvReportId.setText("Report Id : " + id);
        holder.tvMonthYear.setText("Report of month "+month+" ,"+year);
        //holder.tvYear.setText(year);
        holder.tvProgram.setText(program);
        holder.tvLeave.setText("Leaves Taken : "+leave);
        holder.tvStatus.setText("Report Status : "+status);

        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (status.equals("Approved")) {
                    Toast.makeText(context, "Report is approved not allowed to edit ", Toast.LENGTH_SHORT).show();
                } else if (status.equals("Accept")) {
                    Toast.makeText(context, "Report is accepted not allowed to edit ", Toast.LENGTH_SHORT).show();
                } else {

                }*/
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
                    List<ReportModel> filteredList = new ArrayList<>();
                    for (ReportModel data : reportModels) {
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
                mFilteredList = (List<ReportModel>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<ReportModel> studentDetails) {
        this.notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardViewMain;
        TextView tvReportId, tvMonthYear, tvProgram, tvLeave, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewMain = itemView.findViewById(R.id.cardView);
            tvReportId = itemView.findViewById(R.id.reportIdTv);
            tvMonthYear = itemView.findViewById(R.id.monthYearTv);
           // tvYear = itemView.findViewById(R.id.yearTv);
            tvProgram = itemView.findViewById(R.id.programTv);
            tvLeave = itemView.findViewById(R.id.leaveTv);
            tvStatus = itemView.findViewById(R.id.statusTv);
        }
    }
}