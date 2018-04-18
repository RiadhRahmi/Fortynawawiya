package com.wridmob.fortynawawiya.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import com.wridmob.fortynawawiya.R;
import com.wridmob.fortynawawiya.entities.Fahres;


public class FahresAdapter extends BaseAdapter implements Filterable{

    Activity activity;
    List<Fahres> data;
    List<Fahres> filterData;
    static LayoutInflater inflater = null;
    private ItemFilter mFilter = new ItemFilter();

    public FahresAdapter(Activity activity, List<Fahres> data) {
        this.activity = activity;
        this.data = data;
        this.filterData=data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filterData.size();
    }

    @Override
    public Fahres getItem(int position) {
        return filterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Fahres fahres = filterData.get(position);

        if (view == null)
            view = inflater.inflate(R.layout.item_fahres, null);

        TextView textAtPage = (TextView) view.findViewById(R.id.text_at_page);
        textAtPage.setText(fahres.atPage);

        TextView textSummary = (TextView) view.findViewById(R.id.text_fahres);
        textSummary.setText(fahres.name);

        return view;
    }


    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString();
            FilterResults results = new FilterResults();
            final List<Fahres> list = data;
            int count = list.size();
            final ArrayList<Fahres> nlist = new ArrayList<Fahres>(count);
            Fahres filterableString ;
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.name.contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterData = (ArrayList<Fahres>) results.values;
            notifyDataSetChanged();
        }

    }
}

