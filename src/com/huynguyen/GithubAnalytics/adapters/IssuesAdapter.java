package com.huynguyen.GithubAnalytics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.huynguyen.GithubAnalytics.R;
import com.huynguyen.GithubAnalytics.model.pojo.Issue;

import java.util.List;

public class IssuesAdapter extends ArrayAdapter<Issue> {

    private LayoutInflater layoutInflater;

    public IssuesAdapter(Context context, int textViewResourceId, List<Issue> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater = (LayoutInflater) context.getSystemService( Context
                .LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_issue, null);

            viewHolder = new ViewHolder();
            viewHolder.number = (TextView)convertView.findViewById(R.id.number);
            viewHolder.state = (TextView)convertView.findViewById(R.id.state);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.assignee = (TextView)convertView.findViewById(R.id.assignee);
            viewHolder.author = (TextView)convertView.findViewById(R.id
                    .author);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Issue issue = getItem(position);

        viewHolder.number.setText(""+ issue.getNumber());
        viewHolder.state.setText(issue.getState());
        viewHolder.title.setText(issue.getTitle());
        viewHolder.author.setText(issue.getAuthor().getLogin());
        if (issue.getAssignee() != null) {
            viewHolder.assignee.setText(issue.getAssignee().getLogin());
        } else {
            viewHolder.assignee.setText("Free!!!");
        }

        return convertView;
    }

    private class ViewHolder {
        TextView number;
        TextView state;
        TextView title;
        TextView assignee;
        TextView author;
    }
}
