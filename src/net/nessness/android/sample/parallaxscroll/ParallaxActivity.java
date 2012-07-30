package net.nessness.android.sample.parallaxscroll;

import net.nessness.android.sample.parallaxscroll.ParallaxScrollView.OnTouchDispatcher;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

public class ParallaxActivity extends Activity {
    
    private static final String TAG = ParallaxActivity.class.getSimpleName();

    ParallaxScrollView mLayer1;
    ParallaxScrollView mLayer2;
    ParallaxScrollView mLayer3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax);

        mLayer1 = (ParallaxScrollView)findViewById(R.id.layer1);
        mLayer2 = (ParallaxScrollView)findViewById(R.id.layer2);
        mLayer3 = (ParallaxScrollView)findViewById(R.id.layer3);

        // layer2にタッチを伝播
        mLayer1.setOnTouchListener(new OnTouchDispatcher(mLayer2, false));
        // layer3にタッチを伝播 layer2,3はタッチイベントでスクロールなどさせないようにするのでtrue
        mLayer2.setOnTouchListener(new OnTouchDispatcher(mLayer3, true));
        // タッチを伝播しない
        mLayer3.setOnTouchListener(new OnTouchDispatcher(null, true));
        
    }
    
    /**
     * mScroll1 width: w1
     * mScroll2 width: w2
     *  可動右端 l = w - (描画領域横幅)
     *  ratio = l2/l1
     *  @return 
     */
    private float getLayerWidthRatio(HorizontalScrollView baseLayer, HorizontalScrollView backLayer){
        View base = baseLayer.getChildAt(0);
        View back = backLayer.getChildAt(0);
        int w = getWindowManager().getDefaultDisplay().getWidth();
        float r = (back.getWidth() - w) / (base.getWidth() * 1f - w);
        Log.d(TAG, "ratio: " + r + ", width: " + back.getWidth());
        return r;
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) return;
        float ratio2 = getLayerWidthRatio(mLayer1, mLayer2);
        float ratio3 = getLayerWidthRatio(mLayer1, mLayer3);
        // layer2,3を1に追従させる
        mLayer1.addBackLayer(mLayer2, ratio2).addBackLayer(mLayer3, ratio3);
    }
    
    public void onButtonClick(View v){
        String text = ((Button)v).getText().toString() + " clicked.";
        Log.d(TAG, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    
}
