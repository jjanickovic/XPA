package org.xpa.example.activity;

import org.xpa.example.R;
import org.xpa.example.util.ResultSet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PerformanceResultActivity extends Activity {

	private TextView runCount, min, max, avg;
	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_performance_result);
		
		Bundle extras = getIntent().getExtras();
		ResultSet resultSet = (ResultSet) extras.getSerializable(ResultSet.KEY);
		
		this.runCount = (TextView) findViewById(R.id.text_runs);
		this.min = (TextView) findViewById(R.id.text_min);
		this.max = (TextView) findViewById(R.id.text_max);
		this.avg = (TextView) findViewById(R.id.text_avg);
		
		String runs = getResources().getString(R.string.msg_runs, resultSet.getRunCount());
		this.runCount.setText(runs);
		String min = getResources().getString(R.string.msg_min, resultSet.getMin());
		this.min.setText(min);
		String max = getResources().getString(R.string.msg_max, resultSet.getMax());
		this.max.setText(max);
		String avg = getResources().getString(R.string.msg_avg, resultSet.getAvg());
		this.avg.setText(avg);
		
		this.button = (Button) findViewById(R.id.button_ok);
		this.button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
}
