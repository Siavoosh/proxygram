package com.proxygram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.proxygram.model.Proxy;

import java.util.List;

public class ProxyAdapter extends RecyclerView.Adapter<ProxyAdapter.ViewHolder> {

    private List<Proxy> proxies;
    private onOpenClicked onOpenClicked;

    public ProxyAdapter(List<Proxy> proxies, ProxyAdapter.onOpenClicked onOpenClicked) {
        this.proxies = proxies;
        this.onOpenClicked = onOpenClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.proxy_layout, viewGroup, false);
        return new ViewHolder(view, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(proxies.get(i), onOpenClicked);
    }

    @Override
    public int getItemCount() {
        return proxies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView type, time;
        Button open;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            type = itemView.findViewById(R.id.proxy_type);
            time = itemView.findViewById(R.id.proxy_time);
            open = itemView.findViewById(R.id.proxy_open);
        }

        public void bind(final Proxy proxy, final onOpenClicked onOpenClicked) {
            type.setText(proxy.getType());
            time.setText(proxy.getDate());
            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOpenClicked.onClick(proxy);
                }
            });
        }
    }

    public interface onOpenClicked {
        void onClick(Proxy proxy);
    }
}
