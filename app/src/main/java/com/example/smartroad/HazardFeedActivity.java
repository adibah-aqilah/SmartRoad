package com.example.smartroad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HazardFeedActivity extends AppCompatActivity {

    private RecyclerView rvHazards;
    private HazardAdapter adapter;
    private List<Map<String, Object>> hazardList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_feed);

        rvHazards = findViewById(R.id.rvHazards);
        rvHazards.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HazardAdapter(hazardList);
        rvHazards.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadHazards();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadHazards() {
        db.collection("hazards")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    hazardList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Map<String, Object> data = doc.getData();
                        data.put("id", doc.getId());
                        hazardList.add(data);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private class HazardAdapter extends RecyclerView.Adapter<HazardAdapter.ViewHolder> {
        private List<Map<String, Object>> list;

        public HazardAdapter(List<Map<String, Object>> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hazard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> hazard = list.get(position);
            holder.tvType.setText((String) hazard.get("hazardType"));
            holder.tvStatus.setText("Status: " + hazard.get("status"));
            
            Double lat = (Double) hazard.get("latitude");
            Double lng = (Double) hazard.get("longitude");
            holder.tvLocation.setText(lat + ", " + lng);

            String imageUrl = (String) hazard.get("imageUrl");
            Glide.with(holder.ivThumb.getContext())
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(holder.ivThumb);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(HazardFeedActivity.this, HazardDetailsActivity.class);
                intent.putExtra("DOCUMENT_ID", (String) hazard.get("id"));
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvType, tvLocation, tvStatus;
            ImageView ivThumb;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvType = itemView.findViewById(R.id.tvHazardType);
                tvLocation = itemView.findViewById(R.id.tvHazardLocation);
                tvStatus = itemView.findViewById(R.id.tvHazardStatus);
                ivThumb = itemView.findViewById(R.id.ivHazardThumb);
            }
        }
    }
}