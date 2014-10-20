package com.derek.ltapoc;

import android.app.Activity;
import android.os.Bundle;

public class RateTemplateListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_template_list);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new RateTemplateListFragment()).commit();
		}
	}
}
