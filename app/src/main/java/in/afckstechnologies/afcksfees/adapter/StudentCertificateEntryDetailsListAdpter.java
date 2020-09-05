package in.afckstechnologies.afcksfees.adapter;

import android.content.Context;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.StudentCertificateDetailsDAO;


/**
 * Created by admin on 3/18/2017.
 */

public class StudentCertificateEntryDetailsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<StudentCertificateDetailsDAO> data;
    StudentCertificateDetailsDAO current;


    // create constructor to innitilize context and data sent from MainActivity
    public StudentCertificateEntryDetailsListAdpter(Context context, List<StudentCertificateDetailsDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_certificate_details_enter, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);


        myHolder.txt_name.setText(current.getStudent_Name());
        myHolder.txt_name.setTag(position);
        myHolder.sno.setText(current.getStudent_certificate_name());
        myHolder.sno.setTag(position);


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_name,sno;
        ImageView checked,unchecked;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            sno= (TextView) itemView.findViewById(R.id.sno);


        }

    }


}
