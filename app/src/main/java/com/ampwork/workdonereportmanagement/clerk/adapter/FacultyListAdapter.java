package com.ampwork.workdonereportmanagement.clerk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.SubjectModel;
import com.ampwork.workdonereportmanagement.model.UserInfo;
import com.ampwork.workdonereportmanagement.network.Api;
import com.amulyakhare.textdrawable.TextDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.ViewHolder> {


    private Context context;
    List<UserInfo> userInfos = new ArrayList<>();
    List<UserInfo> mFilteredList = new ArrayList<>();


    private RecycleItemViewClicked itemViewClicked;


    public interface RecycleItemViewClicked {
        public void onItemViewSelected(UserInfo model);
    }

    public FacultyListAdapter(Context context, List<UserInfo> userInfos,
                              RecycleItemViewClicked recycleItemViewClicked) {
        this.context = context;
        this.userInfos = userInfos;
        this.mFilteredList = userInfos;
        this.itemViewClicked = recycleItemViewClicked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faculty_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        String fullName = mFilteredList.get(position).getFirstName()+" "+mFilteredList.get(position).getLastName();
        String role = mFilteredList.get(position).getRole();
        String program = mFilteredList.get(position).getProgram();
        String phone = mFilteredList.get(position).getPhone();
        String profileUrl = mFilteredList.get(position).getProfileUrl();


        holder.tvRole.setText(role);
        holder.tvFullName.setText(fullName);
        holder.tvProgram.setText(program);
        holder.tvPhone.setText(phone);

        if (!TextUtils.isEmpty(profileUrl)) {
            Picasso.with(context)
                    .load(Api.BASE_URL+profileUrl)
                    .placeholder(R.drawable.user_default_image)
                    .error(R.drawable.user_default_image)
                    .into(holder.ivProfile);

        } else {
            holder.ivProfile.setImageResource(R.drawable.user_default_image);
        }

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
                                itemViewClicked.onItemViewSelected(mFilteredList.get(position));
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
        return mFilteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toLowerCase();
                if (charString.isEmpty()) {
                    mFilteredList = userInfos;
                } else if (charString.equals("all")) {
                    mFilteredList = userInfos;
                } else {
                    List<UserInfo> filteredList = new ArrayList<>();
                    for (UserInfo data : userInfos) {
                        if (data.getFirstName().toLowerCase().contains(charString) ||
                                data.getLastName().toLowerCase().contains(charString) ||
                                data.getRole().toLowerCase().contains(charString) ||
                                data.getProgram().toLowerCase().contains(charString)) {
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
                mFilteredList = (List<UserInfo>) results.values;
                updateFilterList(mFilteredList);
            }
        };
    }

    public void updateFilterList(List<UserInfo> userInfos) {
        this.notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName,tvPhone,tvRole,tvProgram;
        ImageButton imageButton;
        ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.nameTv);
            tvPhone = itemView.findViewById(R.id.phoneTv);
            tvProgram = itemView.findViewById(R.id.programTv);
            tvRole = itemView.findViewById(R.id.roleTv);
            ivProfile = itemView.findViewById(R.id.profileImage);
            imageButton = itemView.findViewById(R.id.updateBtn);
        }
    }

}
