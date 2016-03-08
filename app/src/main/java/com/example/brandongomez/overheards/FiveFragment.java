package com.example.brandongomez.overheards;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brandongomez.overheards.R;

import java.util.ArrayList;
import java.util.List;

public class FiveFragment extends Fragment {
    public FiveFragment() {
        // Required empty public constructor
    }

    private MyAdapter aa;
    private MyAdapter commentAdapter;

    private ArrayList<ListElement> aList;
    private ArrayList<ListElement> cList;

    private class ListElement {
        ListElement() {
        }

        ListElement(String tl, String t2) {
            textLabel = tl;
            textLabel2 = t2;
        }

        public String textLabel;
        public String textLabel2;
    }
    public class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView tv = (TextView) newView.findViewById(R.id.itemText2);
            TextView tv2 = (TextView) newView.findViewById(R.id.itemText);

            tv.setText(w.textLabel);
            tv2.setText(w.textLabel2);


            newView.setTag(w.textLabel);
            newView.setTag(w.textLabel2);


            return newView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aList = new ArrayList<ListElement>();
        cList = new ArrayList<ListElement>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_five, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aa = new MyAdapter(this.getActivity(), R.layout.list_element, aList);
        commentAdapter = new MyAdapter(this.getActivity(), R.layout.list_element, cList);
        ListView myListView = (ListView) view.findViewById(R.id.postListView);
        ListView cListView = (ListView) view.findViewById(R.id.commentListView);
        myListView.setAdapter(aa);
        cListView.setAdapter(commentAdapter);
        aa.notifyDataSetChanged();
        commentAdapter.notifyDataSetChanged();
        for (int i = 0; i < 8; ++i) {
            aList.add(new ListElement("July 21st", "Hello World!"));

        }
        for (int i = 0; i < 8; ++i) {
            cList.add(new ListElement("July 21st", "Hello World!"));

        }
        // We notify the ArrayList adapter that the underlying list has changed,
        // triggering a re-rendering of the list.
        aa.notifyDataSetChanged();
        commentAdapter.notifyDataSetChanged();

    }
}
