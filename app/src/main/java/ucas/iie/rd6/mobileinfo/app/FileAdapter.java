package ucas.iie.rd6.mobileinfo.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ucas.iie.rd6.mobileinfo.R;

public class FileAdapter extends ArrayAdapter {

    private final int resourceId;

    public FileAdapter(Context context, int textViewResourceId, List<File> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView fileImage = (ImageView) view.findViewById(R.id.iv_file_img);
        TextView fileName = (TextView) view.findViewById(R.id.tv_file_name);
        TextView fileItem = (TextView) view.findViewById(R.id.tv_file_item);
        //FileInfo fileInfo = (FileInfo) getItem(position);
        File file = (File) getItem(position);

        if (file.isDirectory()) fileImage.setImageResource(R.drawable.default_folder);
        if (!file.isDirectory()) fileImage.setImageResource(R.drawable.default_file);

        fileName.setText(file.getName());

        StringBuilder builder = new StringBuilder();
        if (file.isDirectory()) builder.append("D ");
        else builder.append("F ");
        builder.append("-");
        if (file.canRead()) builder.append("r");
        if (file.canWrite()) builder.append("w");
        if (file.canExecute()) builder.append("x");
        builder.append(" ");

        long filesize = 0;
        if (!file.isDirectory()) {
            filesize = file.length();
            builder.append(Formatter.formatFileSize(getContext(), Long.valueOf(filesize)));
            builder.append(" ");
        }

        long modTime = file.lastModified();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        builder.append(dateFormat.format(new Date(modTime)));
        fileItem.setText(builder.toString());
        return view;
    }
}
