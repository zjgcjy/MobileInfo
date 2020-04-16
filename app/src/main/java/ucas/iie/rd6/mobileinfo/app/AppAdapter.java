package ucas.iie.rd6.mobileinfo.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ucas.iie.rd6.mobileinfo.R;

public class AppAdapter extends ArrayAdapter {

    private final int resourceId;

    public AppAdapter(Context context, int textViewResourceId, List<AppInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AppInfo appinfo = (AppInfo) getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView appImage = (ImageView) view.findViewById(R.id.iv_app_img);
        TextView appName = (TextView) view.findViewById(R.id.tv_app_name);
        //appImage.setImageResource(appinfo.getIcon());
        appImage.setBackground(appinfo.getIcon());
        appName.setText(appinfo.getAppName());
        return view;
    }
}
