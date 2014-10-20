package com.derek.ltapoc;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.derek.ltapoc.model.DateFormatUtil;
import com.derek.ltapoc.model.LTADataStore;
import com.derek.ltapoc.model.RateTemplate;
import com.derek.ltapoc.model.RateTemplateDataSource;

public class RateTemplateListFragment extends Fragment {
	private TextView mCreateTemplateTextView;
	private ListView mListView;
	private ArrayList<RateTemplate> mRateTemplates;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RateTemplateDataSource dataSource = new RateTemplateDataSource(getActivity());
		dataSource.open();
		ArrayList<RateTemplate> templates = dataSource.getAllRateTemplates();
		dataSource.close();

		if (templates != null) {
			LTADataStore.get().setRateTemplates(templates);
		}
		mRateTemplates = LTADataStore.get().getRateTemplates();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_rate_template_list, container, false);

		mCreateTemplateTextView = (TextView) rootView.findViewById(R.id.text_view_rate_template_list_create_template);
		mListView = (ListView) rootView.findViewById(R.id.list_view_rate_template_list);

		RateTemplateAdapter adapter = new RateTemplateAdapter(getActivity(), 0, mRateTemplates);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				RateTemplate template = mRateTemplates.get(position);
				Intent intent = new Intent(getActivity(), RateTemplateActivity.class);
				intent.putExtra("rateTemplateId", template.getRateTemplateId());
				startActivity(intent);
			}
		});

		mCreateTemplateTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RateTemplateActivity.class);
				startActivity(intent);
			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		RateTemplateAdapter adapter = (RateTemplateAdapter) mListView.getAdapter();
		adapter.notifyDataSetChanged();
	}

	private class RateTemplateAdapter extends ArrayAdapter<RateTemplate> {

		public RateTemplateAdapter(Context context, int resource, List<RateTemplate> templates) {
			super(context, resource, templates);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_rate_template, null, false);
			}

			TextView schemeNameTextView = (TextView) convertView.findViewById(R.id.text_view_scheme_name);
			TextView createdByTextView = (TextView) convertView.findViewById(R.id.text_view_created_by);
			TextView createdDateTextView = (TextView) convertView.findViewById(R.id.text_view_created_date);

			RateTemplate template = getItem(position);
			schemeNameTextView.setText(template.getChargingScheme().getSchemeName());
			createdByTextView.setText(template.getChargingScheme().getCreatedBy());

			String dateString = DateFormatUtil.getDateStringFromDate(template.getChargingScheme().getCreatedDate(),
					"dd/MM/yyyy HH:mm");
			createdDateTextView.setText(dateString);

			return convertView;
		}
	}
}
