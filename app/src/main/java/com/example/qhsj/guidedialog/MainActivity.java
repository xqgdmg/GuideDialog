package com.example.qhsj.guidedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.qhsj.guidedialog.transformer.ZoomOutPageTransformer;
import com.example.qhsj.guidedialog.view.MyDialogFragment;

public class MainActivity extends AppCompatActivity {

    private Button button;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        MyDialogFragment.getInstance()
                .setImages(new int[]{R.drawable.new_user_guide_1, R.drawable.new_user_guide_2, R.drawable.new_user_guide_3, R.drawable.new_user_guide_4})
                .setCanceledOnTouchOutside(true)
                .setPageTransformer(new ZoomOutPageTransformer())
                .show(getFragmentManager());
    }
}
