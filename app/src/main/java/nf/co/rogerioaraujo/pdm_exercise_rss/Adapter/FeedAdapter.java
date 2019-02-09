package nf.co.rogerioaraujo.pdm_exercise_rss.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;

import nf.co.rogerioaraujo.pdm_exercise_rss.Interface.ItemClickListener;
import nf.co.rogerioaraujo.pdm_exercise_rss.Model.RSSObject;
import nf.co.rogerioaraujo.pdm_exercise_rss.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private RSSObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;

    public FeedAdapter(RSSObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtContent.setText(rssObject.getItems().get(position).getDescription());

        // Open news on browser after click
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean inLongClick) {
                if (!inLongClick) {
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                    rssObject.getItems().get(position).getLink()
                    ));

                    mContext.startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }
}
