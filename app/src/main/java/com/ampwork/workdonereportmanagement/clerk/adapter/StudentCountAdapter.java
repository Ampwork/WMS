package com.ampwork.workdonereportmanagement.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.adapter.DailyReportAdapter;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.ClerkResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


public class StudentCountAdapter extends RecyclerView.Adapter<StudentCountAdapter.ViewHolder> {


    private Context context;
    List<ClerkResponse.ProgramCount> programCounts = new ArrayList<>();
    private List<ClerkResponse.ProgramCount> mFilteredList = new ArrayList<>();
    List<ClerkResponse.count> countList = new ArrayList<>();

    private RecycleItemViewClicked itemViewClicked;

    public interface RecycleItemViewClicked {
        public void onItemViewSelected(ClerkResponse.ProgramCount programCount);
    }

    public StudentCountAdapter(Context context, List<ClerkResponse.ProgramCount> programCounts,
                               RecycleItemViewClicked recycleItemViewClicked) {
        this.context = context;
        this.programCounts = programCounts;
        this.mFilteredList = programCounts;
        this.itemViewClicked = recycleItemViewClicked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.program_list_count_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String program = mFilteredList.get(position).getProgram();
        countList = mFilteredList.get(position).getCountList();
        holder.tvProgramName.setText(program);

        for (int i = 0;i<countList.size();i++)
        {
            if (countList.get(i).getSemester().equals("1")) {
                holder.tvSem1.setText(countList.get(i).getTotal_students());
            } else if (countList.get(i).getSemester().equals("2")) {
                holder.tvSem2.setText(countList.get(i).getTotal_students());
            } else if (countList.get(i).getSemester().equals("3")) {
                holder.tvSem3.setText(countList.get(i).getTotal_students());
            } else if (countList.get(i).getSemester().equals("4")) {
                holder.tvSem4.setText(countList.get(i).getTotal_students());
            }else {
                holder.tvSem1.setText("0");
                holder.tvSem2.setText("0");
                holder.tvSem3.setText("0");
                holder.tvSem4.setText("0");

            }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProgramName, tvSem1, tvSem2, tvSem3, tvSem4;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProgramName = itemView.findViewById(R.id.programTv);
            tvSem1 = itemView.findViewById(R.id.semOneTv);
            tvSem2 = itemView.findViewById(R.id.semTwoTv);
            tvSem3 = itemView.findViewById(R.id.semThreeTv);
            tvSem4 = itemView.findViewById(R.id.semFourTv);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

}
