package com.ampwork.workdonereportmanagement.faculty.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.ViewHolder> {

    private Context context;
    private List<AddReportModel> reportModels = new ArrayList<>();
    private List<AddReportModel> mFilteredList = new ArrayList<>();

    String status;
    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(AddReportModel addReportModel);
    }

    public DailyReportAdapter(Context context, List<AddReportModel> reportModels,
                              RecycleItemViewClicked itemViewClicked, String status) {
        this.context = context;
        this.reportModels = reportModels;
        this.mFilteredList = reportModels;
        this.itemViewClicked = itemViewClicked;
        this.status = status;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(context, R.layout.dept_list_item, null);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_reports_list_items, parent, false);
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
                                itemViewClicked.onItemViewSelected(mFilteredList.get(position));
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.hiddenLayout.getVisibility() == View.VISIBLE) {

                    holder.hiddenLayout.setVisibility(View.GONE);
                    holder.arrow.setBackgroundResource(R.drawable.ic_baseline_expand_more_24);
                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                }else {

                    holder.hiddenLayout.setVisibility(View.VISIBLE);
                    holder.arrow.setBackgroundResource(R.drawable.ic_baseline_expand_less_24);
                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
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
                    mFilteredList = reportModels;
                } else if (charString.equals("all")){
                    mFilteredList = reportModels;
                }else {
                    List<AddReportModel> filteredList = new ArrayList<>();
                    for (AddReportModel data : reportModels) {
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
                mFilteredList = (List<AddReportModel>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<AddReportModel> studentDetails) {
        this.notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvSubject, tvSemester, tvTime, tvTotalPresent, tvTotalAbsent,arrow;
        ImageButton btnUpdate;
        LinearLayout hiddenLayout;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.dateTv);
            tvSemester = itemView.findViewById(R.id.semTv);
            tvSubject = itemView.findViewById(R.id.subjectTv);
            tvTime = itemView.findViewById(R.id.timeTv);
            tvTotalAbsent = itemView.findViewById(R.id.totalAbsentATv);
            tvTotalPresent = itemView.findViewById(R.id.totalPresentTv);
            btnUpdate = itemView.findViewById(R.id.updateBtn);
            arrow = itemView.findViewById(R.id.arrow_button);
            hiddenLayout = itemView.findViewById(R.id.ll1);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}