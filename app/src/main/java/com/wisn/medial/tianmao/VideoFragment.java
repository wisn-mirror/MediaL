package com.wisn.medial.tianmao;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

/**
 * Created by Wisn on 2019-05-09 10:07.
 */
public class VideoFragment extends Fragment {
    private static String TAG = "VideoFragment";

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layout;
    private int adapterPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = inflater.inflate(R.layout.fragment_video, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);

        recyclerView.setAdapter(new RecyclerView.Adapter() {
            public static final int video = 1;
            public static final int product = 0;
            int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                if (i == product) {
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    TextView textView = new TextView(getContext());
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);
                    return new ViewHoderM(linearLayout);
                } else {
                    FrameLayout linearLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_exo_player, null);
                    return new VideoViewHoderM(linearLayout);
                }
            }
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                int i = getItemViewType(position);
                if (i == product) {
                    LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, screenWidth / 2));
                    TextView tv = (TextView) linearLayout.getChildAt(1);
                    tv.setText("position:" + position);
                    GlideApp.with(getContext()).load(Constants.res[position]).into(imageView);
                } else {
                    VideoViewHoderM videoViewHoderM = (VideoViewHoderM) viewHolder;
                    videoViewHoderM.preview();
                    Log.d(TAG, "preview position:" + position+" "+videoViewHoderM);
                }

            }

            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                super.onViewRecycled(holder);

                Log.d(TAG, "preview position onViewRecycled:" + holder.getAdapterPosition()+" "+holder+" "+(holder instanceof VideoViewHoderM));

            }
            @Override
            public int getItemViewType(int position) {
                if (position == 3 || position == 8||position == 14||position == 20) {
                    return video;
                } else {
                    return product;
                }
            }

            @Override
            public int getItemCount() {
                return Constants.res.length;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                checkVideo(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 当不滚动时
                    Glide.with(getActivity()).resumeRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // Log.i("SCROLL_STATE_DRAGGING", "手滑动：SCROLL_STATE_DRAGGING");
                    Glide.with(getActivity()).pauseRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    // Log.i("SCROLL_STATE_SETTLING", "松开惯性滑动：SCROLL_STATE_SETTLING");
                    Glide.with(getActivity()).resumeRequests();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dy > 0) {
                    //大于0表示正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }

        });
    }

    private int findPosition(int[] lastPositions, boolean isTop) {
        int best = lastPositions[0];
        for (int value : lastPositions) {
            if (isTop) {
                if (value < best) {
                    best = value;
                }
            } else {
                if (value > best) {
                    best = value;
                }
            }
        }
        return best;
    }

    public void checkVideo(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 当不滚动时
            int[] lastpoint=new int[layout.getSpanCount()] ;
            int[] firstpoint=new int[layout.getSpanCount()] ;
            int[] firstVisibleItemPositions = layout.findFirstVisibleItemPositions(firstpoint);
            int[] lastVisibleItemPositions = layout.findLastVisibleItemPositions(lastpoint);
            int first= findPosition(firstVisibleItemPositions,true);
            int last= findPosition(lastVisibleItemPositions,false);
//            Log.d(TAG, "childCount:" + childCount + "  first：" + first+ " last：" + last);

            int childCount = recyclerView.getChildCount();
            Log.d(TAG, "childCount:" + childCount);
            VideoViewHoderM videoViewHoder = null;
            for (int i = 0; i < childCount; i++) {
                View view = recyclerView.getChildAt(i);
                if (view == null) continue;
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(view);
                if (null != childViewHolder) {
//                    Log.d(TAG, "childViewHolder:" + childViewHolder + "  ViewHoderMType：" + (childViewHolder instanceof ViewHoderM) + "  VideoViewHoderMType：" + (childViewHolder instanceof VideoViewHoderM));
                    if (childViewHolder instanceof VideoViewHoderM) {
                        int[] location = new int[2];
                        childViewHolder.itemView.getLocationOnScreen(location);
                        videoViewHoder = (VideoViewHoderM) childViewHolder;
//                        videoViewHoder.play(player);
//                        player.setPlayWhenReady(true);
                        break;
                    }
                }
            }
            if(adapterPosition>last||adapterPosition<first){
                RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(adapterPosition);
                if(vh instanceof VideoViewHoderM){
                    ((VideoViewHoderM)vh).preview();
                }
            }
            if (videoViewHoder != null) {
                int playosition = videoViewHoder.getAdapterPosition();
                if (playosition != adapterPosition) {
                    videoViewHoder.play();
                    adapterPosition = playosition;
                }
            }


        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            // Log.i("SCROLL_STATE_DRAGGING", "手滑动：SCROLL_STATE_DRAGGING");
        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            // Log.i("SCROLL_STATE_SETTLING", "松开惯性滑动：SCROLL_STATE_SETTLING");
        }

    }


    static class ViewHoderM extends RecyclerView.ViewHolder {
        public ViewHoderM(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class VideoViewHoderM extends RecyclerView.ViewHolder {

        private final CardView cardview;
        private final int screenWidth;
        private final PlayerView playerView;
        private final ImageView preview;
        public ExoPlayer player;

        public VideoViewHoderM(@NonNull View itemView) {
            super(itemView);
            screenWidth = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
            cardview = itemView.findViewById(R.id.cardview);
            playerView = itemView.findViewById(R.id.playerView);
            preview = itemView.findViewById(R.id.preview);
            cardview.setLayoutParams(new FrameLayout.LayoutParams((int) (screenWidth / 2.4), screenWidth / 2));
        }

        public void preview() {
            preview.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            GlideApp.with(preview.getContext()).load(Constants.res[0]).into(preview);
           if(player!=null)   player.setPlayWhenReady(false);
        }

        public void play() {
            player = ExoPlayerFactory.newSimpleInstance(itemView.getContext());
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            if (player.getPlaybackState() == Player.STATE_IDLE) {
                Log.d(TAG, "getPlaybackState：" + player.getPlaybackState() + " isPlayingAd：" + player.isPlayingAd() + " isLoading()：" + player.isLoading() + " getContentDuration()：" + player.getContentDuration() + " getDuration()：" + player.getDuration());
               int index=0;
                if(this.getAdapterPosition()==3){
                    index=1;
                }else if(this.getAdapterPosition()==8){
                    index=2;
                }else if(this.getAdapterPosition()==14){
                    index=3;
                }else if(this.getAdapterPosition()==20){
                    index=4;
                }
                ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("wisn")).createMediaSource(Uri.parse(Constants.ip + Constants.local_resvideo[index]));
                player.setRepeatMode(Player.REPEAT_MODE_ALL);
                player.prepare(mediaSource, true, false);
                player.addListener(new Player.DefaultEventListener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        String stateString;
                        // actually playing media
                        if (playWhenReady && playbackState == Player.STATE_READY) {
                            Log.d(TAG, "onPlayerStateChanged: actually playing media");
                            preview.setVisibility(View.GONE);
                            playerView.setVisibility(View.VISIBLE);
                        }
                        switch (playbackState) {
                            case Player.STATE_IDLE:
                                stateString = "ExoPlayer.STATE_IDLE      -";
                                break;
                            case Player.STATE_BUFFERING:
                                stateString = "ExoPlayer.STATE_BUFFERING -";
                                break;
                            case Player.STATE_READY:
                                stateString = "ExoPlayer.STATE_READY     -";
                                break;
                            case Player.STATE_ENDED:
                                stateString = "ExoPlayer.STATE_ENDED     -";
                                break;
                            default:
                                stateString = "UNKNOWN_STATE             -";
                                break;
                        }
                        Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);

                    }
                });
            }

          /*  if (player.getPlaybackState() == Player.STATE_IDLE) {
                preview.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                playerView.setPlayer(player);
                player.setPlayWhenReady(true);
                ExtractorMediaSource mediaSource =
                        aaa.createMediaSource(Uri.parse(Constants.ip + Constants.local_resvideo[2]));
                player.setRepeatMode(Player.REPEAT_MODE_ALL);
                player.prepare(mediaSource, true, false);
                player.addListener(new Player.DefaultEventListener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        String stateString;
                        // actually playing media
                        if (playWhenReady && playbackState == Player.STATE_READY) {
                            Log.d(TAG, "onPlayerStateChanged: actually playing media");
                            preview.setVisibility(View.GONE);
                            playerView.setVisibility(View.VISIBLE);
                        }
                        switch (playbackState) {
                            case Player.STATE_IDLE:
                                stateString = "ExoPlayer.STATE_IDLE      -";
                                break;
                            case Player.STATE_BUFFERING:
                                stateString = "ExoPlayer.STATE_BUFFERING -";
                                break;
                            case Player.STATE_READY:
                                stateString = "ExoPlayer.STATE_READY     -";
                                break;
                            case Player.STATE_ENDED:
                                stateString = "ExoPlayer.STATE_ENDED     -";
                                break;
                            default:
                                stateString = "UNKNOWN_STATE             -";
                                break;
                        }
                        Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);

                    }
                });
            }else{
                preview.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
            }*/
        }

        public void release() {
            playerView.setVisibility(View.GONE);
            preview.setVisibility(View.VISIBLE);
        }


    }


}
