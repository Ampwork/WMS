package com.ampwork.workdonereportmanagement.clerk.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.activities.programs.ProgramsHomeActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.EditDailyReportActivity;
import com.ampwork.workdonereportmanagement.model.ClerkResponse;
import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.ViewHolder> {


    private Context context;
    List<ProgramResponse.ProgramModel> programModelList = new ArrayList<>();



    private RecycleItemViewClicked itemViewClicked;


    public interface RecycleItemViewClicked {
        public void onItemViewSelected(ProgramResponse.ProgramModel model);
    }

    public ProgramsAdapter(Context context, List<ProgramResponse.ProgramModel> programCounts,
                               RecycleItemViewClicked recycleItemViewClicked) {
        this.context = context;
        this.programModelList = programCounts;
        this.itemViewClicked = recycleItemViewClicked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.programs_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String program = programModelList.get(position).getProgramName();

        holder.tvProgramName.setText(program);


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.program_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                itemViewClicked.onItemViewSelected(programModelList.get(position));
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
        return programModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProgramName;
        ImageButton imageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProgramName = itemView.findViewById(R.id.programTv);
            imageButton = itemView.findViewById(R.id.updateBtn);
        }
    }

}
