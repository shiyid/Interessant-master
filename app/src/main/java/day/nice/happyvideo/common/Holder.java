package day.nice.happyvideo.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import day.nice.happyvideo.widget.RatioImageView;
import day.nice.happyvideo.R;

/**
 * @author zsj
 */

public class Holder extends RecyclerView.ViewHolder {

    public View itemView;
    public RatioImageView movieAlbum;
    public TextView movieDesc;
    public FrameLayout movieContent;
    public TextView tag;

    public Holder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        movieContent = (FrameLayout) itemView.findViewById(R.id.movie_content);
        movieAlbum = (RatioImageView) itemView.findViewById(R.id.movie_album);
        movieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
        tag = (TextView) itemView.findViewById(R.id.author_tag);
    }
}
