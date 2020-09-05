package in.afckstechnologies.afcksfees.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.StudentsDiscontinueDetailsDAO;


/**
 * Created by admin on 3/18/2017.
 */

public class StudentDiscontinueDetailsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<StudentsDiscontinueDetailsDAO> data;
    StudentsDiscontinueDetailsDAO current;


    // create constructor to innitilize context and data sent from MainActivity
    public StudentDiscontinueDetailsListAdpter(Context context, List<StudentsDiscontinueDetailsDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_stdiscontinue_details, parent, false);
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


        myHolder.txt_date.setText(current.getDiscontinue_date());
        myHolder.txt_date.setTag(position);

        myHolder.batchcode.setText(current.getBatch_id());
        myHolder.batchcode.setTag(position);

        myHolder.txt_reson.setText(current.getDiscontinue_reason());
        myHolder.txt_reson.setTag(position);


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView batchcode, txt_date, txt_reson;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            batchcode = (TextView) itemView.findViewById(R.id.batchcode);
            txt_reson = (TextView) itemView.findViewById(R.id.txt_reson);


        }

    }


}
