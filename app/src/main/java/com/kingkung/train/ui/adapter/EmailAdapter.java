package com.kingkung.train.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kingkung.train.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {

    private Map<String, Boolean> emailMap;

    public EmailAdapter(Map<String, Boolean> emailMap) {
        this.emailMap = emailMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_email, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String email = (String) emailMap.keySet().toArray()[i];
        viewHolder.tvEmail.setText(email);
        viewHolder.cbEmail.setTag(email);
        viewHolder.cbEmail.setChecked(emailMap.get(email));
    }

    @Override
    public int getItemCount() {
        return emailMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_email)
        TextView tvEmail;
        @BindView(R.id.cb_email)
        CheckBox cbEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnCheckedChanged(R.id.cb_email)
        public void checkPassenger(CompoundButton buttonView, boolean isChecked) {
            String email = (String) buttonView.getTag();
            emailMap.put(email, isChecked);
        }
    }
}
