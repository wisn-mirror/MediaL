package com.wisn.medial.testmorelist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.finalview.TestScrollFragment2;
import com.wisn.medial.src.Constants;

public class TestMoreActivity extends AppCompatActivity {

    private RecyclerView recycler_view1;
    private RecyclerView recycler_view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_more);
        recycler_view1 = findViewById(R.id.recycler_view1);
        recycler_view2 = findViewById(R.id.recycler_view2);
        setrecycle1(TestMoreActivity.this);
        setrecycle2(TestMoreActivity.this);
    }

    private void setrecycle2(Context context) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view2.setLayoutManager(llm);
        recycler_view2.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView textView = new TextView(context);
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                return new TestScrollFragment2.ViewHoderM(linearLayout);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
                ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth , screenWidth / 3));
                TextView tv = (TextView) linearLayout.getChildAt(1);
                tv.setText("横4444444动");
                if(position>=(Constants.res.length)){
                    position=Constants.res.length-1;
                }
                GlideApp.with(context).load(Constants.res[position]).into(imageView);
            }

            @Override
            public int getItemCount() {
                return 200;
            }
        });
        recycler_view2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int[] visiblePosition = getVisiblePosition((LinearLayoutManager) recyclerView.getLayoutManager());
                    Log.d("recycler_view2", "visiblePosition 0:" + visiblePosition[0]+ "   visiblePosition[1]：" +  visiblePosition[1]);
                }
            }
        });
    }

    public int[] getVisiblePosition(LinearLayoutManager layoutManager) {
        int[] result = new int[2];
        result[0] = layoutManager.findFirstVisibleItemPosition();
        result[1] = layoutManager.findLastVisibleItemPosition();
//            Log.d(TAG, "childCount:" + childCount + "  first：" + first+ " last：" + last);
        return result;
    }

    private void setrecycle1(Context context) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view1.setLayoutManager(llm);
        recycler_view1.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView textView = new TextView(context);
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                return new TestScrollFragment2.ViewHoderM(linearLayout);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
                ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth , screenWidth / 3));
                TextView tv = (TextView) linearLayout.getChildAt(1);
                tv.setText("横向的⌚️滑动");
                GlideApp.with(context).load(Constants.res[position]).into(imageView);
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });
        recycler_view1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int[] visiblePosition = getVisiblePosition((LinearLayoutManager) recyclerView.getLayoutManager());
                    Log.d("recycler_view1", "visiblePosition 0:" + visiblePosition[0]+ "   visiblePosition[1]：" +  visiblePosition[1]);
                }
            }
        });
    }

}
