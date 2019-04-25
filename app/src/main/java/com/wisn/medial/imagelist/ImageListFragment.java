package com.wisn.medial.imagelist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by Wisn on 2019/4/21 下午10:59.
 */
public class ImageListFragment extends Fragment {

    private RecyclerView recycler_view;
    private RadioGroup rg_select;
    private int mExitPosition;

    private int mEnterPosition;
    private GridLayoutManager layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View mContextView = inflater.inflate(R.layout.fragment_imagelist, null);
        recycler_view = mContextView.findViewById(R.id.recycler_view);
        rg_select = mContextView.findViewById(R.id.rg_select);
        initView(mContextView);
        return mContextView;
    }

    private void initView(View mContextView) {
        layout = new GridLayoutManager(this.getContext(), 3);
        recycler_view.setLayoutManager(layout);
        recycler_view.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(ImageListFragment.this.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return new ViewHoderM(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                ImageView imageView = (ImageView) viewHolder.itemView;
                int screenWidth = ImageListFragment.this.getContext().getResources().getDisplayMetrics().widthPixels;
                imageView.setLayoutParams(new ViewGroup.LayoutParams(screenWidth / 3, screenWidth / 3));
                GlideApp.with(ImageListFragment.this.getContext()).load(Constants.res[position]).into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int checkedRadioButtonId = rg_select.getCheckedRadioButtonId();
                        if (checkedRadioButtonId == R.id.rb_SharedElement) {
                            mEnterPosition = mExitPosition = position;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                imageView.setTransitionName(String.valueOf(position));
                            }
                            ImageListFragment.this.getActivity(). setExitSharedElementCallback(new SharedElementCallback() {
                                @Override
                                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                                    Log.e("onBindViewHolder","names size："+ names.size()+" sharedElements:"+sharedElements.size());
                                    for(String position:names){
                                        int i = Integer.parseInt(position);
                                        RecyclerView.ViewHolder viewHolderForAdapterPosition = recycler_view.findViewHolderForAdapterPosition(i);
                                        if(viewHolderForAdapterPosition!=null&&viewHolderForAdapterPosition.itemView!=null){
                                            sharedElements.put(position,  viewHolderForAdapterPosition.itemView);
                                        }
                                    }
                                    if(sharedElements==null||sharedElements.size()==0){
                                        names.clear();
                                    }
                                }
                            });
                            //获取最后一个可见view的位置
                            int lastItemPosition = layout.findLastVisibleItemPosition();
                            //获取第一个可见view的位置
                            int firstItemPosition = layout.findFirstVisibleItemPosition();

                            Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
                            intent.putExtra(FullScreenImageActivity.EXTRA_DEFAULT_INDEX, position);
                            intent.putExtra(FullScreenImageActivity.lastItemPosition, lastItemPosition);
                            intent.putExtra(FullScreenImageActivity.firstItemPosition, firstItemPosition);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                        imageView, ViewCompat.getTransitionName(imageView) ).toBundle();
                                ActivityCompat.startActivity(getContext(), intent,
                                        bundle);
//                                ImageListFragment.this.getActivity().startActivity(intent, bundle);
                                Log.e("onBindViewHolder","ActivityOptionsCompat "+position);

                            }
                        } else if (checkedRadioButtonId == R.id.rb_Animator) {

                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return Constants.res.length;
            }
        });
    }

//    public void onActivityReenter(int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && data != null) {
//            mExitPosition = data.getIntExtra(FullScreenImageActivity.EXTRA_EXIT_INDEX, -1);
//        }
//    }

    static class ViewHoderM extends RecyclerView.ViewHolder {
        public ViewHoderM(@NonNull View itemView) {
            super(itemView);
        }
    }
}
