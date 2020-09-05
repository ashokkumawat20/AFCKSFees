package in.afckstechnologies.afcksfees.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.AttendancePercDetailsDAO;


/**
 * Created by admin on 3/18/2017.
 */

public class AttendancePercDetailsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<AttendancePercDetailsDAO> data;
    AttendancePercDetailsDAO current;


    // create constructor to innitilize context and data sent from MainActivity
    public AttendancePercDetailsListAdpter(Context context, List<AttendancePercDetailsDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_attendance_perc_details, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);


        myHolder.txt_date.setText(current.getPresentDate());
        myHolder.txt_date.setTag(position);

        myHolder.studentname.setText(current.getPresentDate());
        myHolder.studentname.setTag(position);

        myHolder.attendancestatus.setText(current.getAttendancePerc());
        myHolder.attendancestatus.setTag(position);

        myHolder.sno.setText(current.getNumbers());
        myHolder.sno.setTag(position);


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView txt_date, studentname, attendancestatus, sno;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            sno = (TextView) itemView.findViewById(R.id.sno);
            studentname = (TextView) itemView.findViewById(R.id.studentname);
            attendancestatus = (TextView) itemView.findViewById(R.id.attendancestatus);


        }

    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

}
