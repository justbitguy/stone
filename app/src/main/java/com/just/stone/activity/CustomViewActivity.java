package com.just.stone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.just.stone.R;
import com.just.stone.util.ResourceUtil;
import com.just.stone.view.PieChart;

/**
 * Created by zhangjinwei on 2016/9/24.
 */

public class CustomViewActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        final PieChart pie = (PieChart) this.findViewById(R.id.Pie);
        pie.addItem("Agamemnon", 2, ResourceUtil.getColor(R.color.seafoam));
        pie.addItem("Bocephus", 3.5f, ResourceUtil.getColor(R.color.chartreuse));
        pie.addItem("Calliope", 2.5f, ResourceUtil.getColor(R.color.emerald));
        pie.addItem("Daedalus", 3, ResourceUtil.getColor(R.color.bluegrass));
        pie.addItem("Euripides", 1, ResourceUtil.getColor(R.color.turquoise));
        pie.addItem("Ganymede", 3, ResourceUtil.getColor(R.color.slate));

        ((Button) findViewById(R.id.Reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pie.setCurrentItem(0);
            }
        });
    }
}
