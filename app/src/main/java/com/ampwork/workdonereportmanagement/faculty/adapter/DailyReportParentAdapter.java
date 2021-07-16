package com.ampwork.workdonereportmanagement.faculty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.DailyReportModel;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DailyReportParentAdapter extends RecyclerView.Adapter<DailyReportParentAdapter.ViewHolder> {

    private Context context;
    private List<AddReportModel> reportModels = new ArrayList<>();

    private List<DailyReportModel.ReportsModel> models = new ArrayList<>();
    String status;


    public DailyReportParentAdapter(Context context,
                                    List<DailyReportModel.ReportsModel> reportModels,
                                    List<AddReportModel> addReportModels,
                                    String status) {
        this.context = context;
        this.models = reportModels;
        this.reportModels = addReportModels;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_daily_reports_list_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        String date = models.get(position).getDate();
        String dateFormat = AppUtility.getDateFormats(date);

        holder.tvDate.setText(dateFormat);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                holder
                        .ChildRecyclerView
                        .getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        reportModels = models.get(position).getAddReportModels();

        DailyReportChildAdapter reportAdapter = new DailyReportChildAdapter(context,
                reportModels, status);

        holder
                .ChildRecyclerView
                .setLayoutManager(layoutManager);

        holder
                .ChildRecyclerView
                .setAdapter(reportAdapter);

     /*   holder
                .ChildRecyclerView
                .setRecycledViewPool(viewPool);*/

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ChildRecyclerView.getVisibility() == View.VISIBLE) {

                    holder.ChildRecyclerView.setVisibility(View.GONE);
                    holder.arrow.setBackgroundResource(R.drawable.ic_baseline_expand_more_24);
                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                }else {

                    holder.ChildRecyclerView.setVisibility(View.VISIBLE);
                    holder.arrow.setBackgroundResource(R.drawable.ic_baseline_expand_less_24);
                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (models == null) {
            return 0;
        } else {
            return models.size();
        }

    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,arrow;
        private RecyclerView ChildRecyclerView;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.dateTv);
            ChildRecyclerView
                    = itemView
                    .findViewById(
                            R.id.child_recyclerview);
            arrow = itemView.findViewById(R.id.arrow_button);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}