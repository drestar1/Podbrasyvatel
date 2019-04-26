package com.felipecsl.gifimageview.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Handler;
import com.felipecsl.gifimageview.library.GifImageView;
import java.util.Random;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = "MainActivity";
  private GifImageView gifImageView;
  private Button btnToggle;
  private Button btnBlur;
  private boolean shouldBlur = false;
  private Blur blur;



    @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    gifImageView = findViewById(R.id.gifImageView);
    btnToggle = findViewById(R.id.btnToggle);



    blur = Blur.newInstance(this);
    gifImageView.setOnFrameAvailable(new GifImageView.OnFrameAvailable() {
      @Override
      public Bitmap onFrameAvailable(Bitmap bitmap) {
        if (shouldBlur) {
          return blur.blur(bitmap);
        }
        return bitmap;
      }
    });

    gifImageView.setOnAnimationStop(new GifImageView.OnAnimationStop() {
      @Override public void onAnimationStop() {
        runOnUiThread(new Runnable() {
          @Override public void run() {

          }
        });
      }
    });

    btnToggle.setOnClickListener(this);



    new GifDataDownloader() {
      @Override protected void onPostExecute(final byte[] bytes) {
        gifImageView.setBytes(bytes);
        gifImageView.stopAnimation();
        gifImageView.setVisibility(View.GONE);
        Log.d(TAG, "GIF width is " + gifImageView.getGifWidth());
        Log.d(TAG, "GIF height is " + gifImageView.getGifHeight());
      }
    }.execute("http://www.gifki.org/data/media/389/moneta-animatsionnaya-kartinka-0021.gif");
  }


  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }


  @Override public void onClick(final View v) {
    final TextView showTextView = (TextView) findViewById(R.id.textView);
    if (v.equals(btnToggle)) {
      showTextView.setText("");
      btnToggle.setEnabled(false);
        gifImageView.setVisibility(View.VISIBLE);
    gifImageView.startAnimation();
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        public void run() {
            // Actions to do after 10 seconds
            gifImageView.setVisibility(View.GONE);
            gifImageView.stopAnimation();
            Random random = new Random();
            int r = random.nextInt(2);

            if(r==0)
            {
                showTextView.setText("Орёл");
            }
            else
              {
                showTextView.setText("Решка");
              }
          btnToggle.setEnabled(true);

        }
      }, 2000);

    }
  }
}
