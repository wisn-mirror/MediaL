package com.wisn.medial.scale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eftimoff.androipathview.PathView;
import com.wisn.medial.R;

/**
 * Created by Wisn on 2019-04-26 14:47.
 */
public class ScaleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);

    }

    public void click(View view) {
        if (view.getId() == R.id.bt_scale_start) {
            PathView pathView1 = findViewById(R.id.pathView1);
            pathView1.setPathColor(getResources().getColor(R.color.gray));
            pathView1.getPathAnimator()
                    .delay(1)
                    .duration(1)
                    .listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
                        @Override
                        public void onAnimationEnd() {
                            Toast.makeText(ScaleActivity.this, "success", Toast.LENGTH_SHORT).show();
                        }
                    }).start();
            PathView pathView2 = findViewById(R.id.pathView2);
//            pathView2.setPathColor(getResources().getColor(R.color.aaa));
            pathView2.setPathColor(getResources().getColor(R.color.aaa));
            pathView2.getPathAnimator()
                    .delay(5)
                    .duration(900)
                    .listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
                        @Override
                        public void onAnimationEnd() {
                            Toast.makeText(ScaleActivity.this, "success", Toast.LENGTH_SHORT).show();
                            pathView2.getPathAnimator().start();
                        }
                    }).start();
        }

    }
}
