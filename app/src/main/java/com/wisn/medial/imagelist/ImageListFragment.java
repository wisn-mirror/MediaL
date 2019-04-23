package com.wisn.medial.imagelist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

/**
 * Created by Wisn on 2019/4/21 下午10:59.
 */
public class ImageListFragment extends Fragment {

    private RecyclerView recycler_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View mContextView = inflater.inflate(R.layout.fragment_imagelist, null);
        recycler_view = mContextView.findViewById(R.id.recycler_view);
        initView(mContextView);
        return mContextView;
    }

    private void initView(View mContextView) {
        recycler_view.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        recycler_view.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(ImageListFragment.this.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return new ViewHoderM(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ImageView imageView = (ImageView) viewHolder.itemView;
                int screenWidth =ImageListFragment.this.getContext().getResources().getDisplayMetrics().widthPixels;
                imageView.setLayoutParams(new ViewGroup.LayoutParams(screenWidth / 3, screenWidth / 3));
                GlideApp.with(ImageListFragment.this.getContext()).load(Constants.res[i]).into(imageView);
            }

            @Override
            public int getItemCount() {
                return Constants.res.length;
            }
        });
    }

    static class ViewHoderM extends RecyclerView.ViewHolder {
        public ViewHoderM(@NonNull View itemView) {
            super(itemView);
        }
    }
}
