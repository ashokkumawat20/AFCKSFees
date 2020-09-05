package in.afckstechnologies.afcksfees.adapter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.Fragments.StudentList;
import in.afckstechnologies.afcksfees.model.OnGoingBatchDAO;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.view.ActualBatchDetailsView;
import in.afckstechnologies.afcksfees.view.AttendancePercDetailsView;
import in.afckstechnologies.afcksfees.view.FeesDetailsByAdminView;
import in.afckstechnologies.afcksfees.view.TotalBatchTimeDetailsView;


/**
 * Created by admin on 3/18/2017.
 */

public class OnGoingBatchesListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<OnGoingBatchDAO> data;
    OnGoingBatchDAO current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;
    int number = 1;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String sms_type = "";


    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String centerListResponse = "", callbackStatusResponse = "";
    boolean status;
    String message = "";
    String msg = "";
    String user_id = "";
    String batch_id = "";
    String s_id = "";
    int clickfalg = 0;
    private static FeesListener mListener;

    // create constructor to innitilize context and data sent from MainActivity
    public OnGoingBatchesListAdpter(Context context, List<OnGoingBatchDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_ongoing_batches_details, parent, false);
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
        myHolder.batch_code.setText(current.getBatch_code() + " " + current.getSt_date() + " " + current.getFrequency() + " " + current.getTimings() + " " + current.getBranch_name());
        myHolder.batch_code.setTag(position);


        myHolder.totalClasses.setText(current.getTotalClasses());
        myHolder.totalClasses.setTag(position);

        myHolder.totalTime.setText(current.getTotalTime());
        myHolder.totalTime.setTag(position);

        myHolder.totalStudents.setText(current.getStudentsInBatch());
        myHolder.totalStudents.setTag(position);

        myHolder.activeStudents.setText(current.getActiveStudents());
        myHolder.activeStudents.setTag(position);

        myHolder.disContinueStudents.setText(current.getDiscontinuedStudents());
        myHolder.disContinueStudents.setTag(position);

        myHolder.demoStudents.setText(current.getDemoStudents());
        myHolder.demoStudents.setTag(position);

        myHolder.activeStPre.setText(current.getActivePerc());
        myHolder.activeStPre.setTag(position);

        myHolder.latWCBtn.setTag(position);
        myHolder.whatsapp.setTag(position);
        myHolder.calling.setTag(position);
        myHolder.layout_quotation.setTag(position);
        myHolder.layhideshow.setTag(position);

        myHolder.presentPrec.setText(current.getPresentPerc());
        myHolder.presentPrec.setTag(position);
        if (!current.getTotalFees().equals("null")) {
            myHolder.weekFees.setText(current.getTotalFees());
            myHolder.weekFees.setTag(position);
        } else {
            myHolder.weekFees.setText("0");
            myHolder.weekFees.setTag(position);
        }

        if (preferences.getString("trainer_user_id", "").equals("RS")) {
            myHolder.latWCBtn.setVisibility(View.VISIBLE);
        } else if (preferences.getString("trainer_user_id", "").equals("AK")) {
            myHolder.latWCBtn.setVisibility(View.VISIBLE);
        } else {
            myHolder.latWCBtn.setVisibility(View.GONE);
        }
        myHolder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                String textToCopy = current.getBatch_code() + "\n"
                        + current.getSt_date() + " | " + current.getFrequency() + " | " + current.getTimings() + "\n"
                        + "Total | Active | Discontinue  " + current.getStudentsInBatch() + " | " + current.getActiveStudents() + " | " + current.getDiscontinuedStudents() + "\n"
                        + "Active Students : " + current.getActivePerc() + "\n"
                        + "Present Percentage : " + current.getPresentPerc() + "\n";
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(textToCopy);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("myLabel", textToCopy);
                    clipboard.setPrimaryClip(clip);
                }
               /* try {
                    Uri uri = Uri.parse("smsto:" + current.getMobile_no());
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    sendIntent.setPackage("com.whatsapp");
                    context.startActivity(sendIntent);
                } catch (Exception e) {
                    Toast.makeText(context, "Error" + e.toString(), Toast.LENGTH_SHORT).show();

                }*/

                PackageManager packageManager = context.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "91" + current.getMobile_no() + "&text=" + URLEncoder.encode(textToCopy, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        myHolder.calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                Log.i("call ph no", "" + current.getMobile_no());
                callIntent.setData(Uri.parse("tel:" + current.getMobile_no()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);
            }
        });
        myHolder.layout_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickfalg == 0) {
                    clickfalg = 1;
                    myHolder.layhideshow.setVisibility(View.VISIBLE);
                } else {
                    clickfalg = 0;
                    myHolder.layhideshow.setVisibility(View.GONE);
                }
            }
        });
        myHolder.layout_quotation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                //  prefEditor.putString("batch_id", current.getBatch_code());
                prefEditor.putString("prebatchcall", "1");
                prefEditor.commit();
                //  mListener.messageReceived(current.getBatch_code());
                //  ((Activity) context).finish();
               // AppCompatActivity activity = (AppCompatActivity) v.getContext();
            //    Fragment myFragment = new StudentList();
             //   activity.getSupportFragmentManager().beginTransaction().replace(R.id.name, myFragment).addToBackStack(null).commit();

              //  Intent intent = new Intent(context, StudentList.class);
             //   intent.putExtra("batch_id", current.getBatch_code());
              //  context.startActivity(intent);
                return true;
            }
        });

        myHolder.activeStudents.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("prebatchcall", "1");
                prefEditor.putString("batch_id", current.getBatch_code());
                prefEditor.commit();
                Intent intent = new Intent(context, StudentList.class);
                intent.putExtra("batch_id", current.getBatch_code());
                context.startActivity(intent);
                // ((Activity) context).finish();
                return true;

            }
        });
        myHolder.disContinueStudents.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("prebatchcall", "2");
                prefEditor.putString("batch_id", current.getBatch_code());
                prefEditor.commit();
                Intent intent = new Intent(context, StudentList.class);
                intent.putExtra("batch_id", current.getBatch_code());
                context.startActivity(intent);
                //   ((Activity) context).finish();
                return true;
            }
        });
        myHolder.totalTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("batch_id", current.getBatch_code());
                prefEditor.commit();
                TotalBatchTimeDetailsView totalBatchTimeDetailsView = new TotalBatchTimeDetailsView();
                totalBatchTimeDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "totalBatchTimeDetailsView");
                return true;
            }
        });
        myHolder.presentPrec.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("batch_id", current.getBatch_code());
                prefEditor.commit();
                AttendancePercDetailsView attendancePercDetailsView = new AttendancePercDetailsView();
                attendancePercDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "attendancePercDetailsView");

                return true;
            }
        });


        myHolder.totalClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("batch_id", current.getBatch_code());
                prefEditor.commit();
                ActualBatchDetailsView actualBatchDetailsView = new ActualBatchDetailsView();
                actualBatchDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "actualBatchDetailsView");

            }
        });

        myHolder.weekFees.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("batchId", current.getBatch_code());
                prefEditor.commit();
                FeesDetailsByAdminView feesDetailsByAdminView = new FeesDetailsByAdminView();
                feesDetailsByAdminView.show(((FragmentActivity) context).getSupportFragmentManager(), "feesDetailsByAdminView");
                //   Toast.makeText(context, "Error" + current.getBranch_name(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView batch_code, totalClasses, totalTime, totalStudents, activeStudents, disContinueStudents, activeStPre, presentPrec, weekFees,demoStudents;
        ImageView whatsapp, calling;
        LinearLayout latWCBtn, weekFeesLay, layout_quotation, layhideshow;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            batch_code = (TextView) itemView.findViewById(R.id.batch_code);
            totalClasses = (TextView) itemView.findViewById(R.id.totalClasses);
            totalTime = (TextView) itemView.findViewById(R.id.totalTime);
            totalStudents = (TextView) itemView.findViewById(R.id.totalStudents);
            activeStudents = (TextView) itemView.findViewById(R.id.activeStudents);
            disContinueStudents = (TextView) itemView.findViewById(R.id.disContinueStudents);
            activeStPre = (TextView) itemView.findViewById(R.id.activeStPre);
            presentPrec = (TextView) itemView.findViewById(R.id.presentPrec);
            weekFees = (TextView) itemView.findViewById(R.id.weekFees);
            demoStudents = (TextView) itemView.findViewById(R.id.demoStudents);
            whatsapp = (ImageView) itemView.findViewById(R.id.whatsapp);
            calling = (ImageView) itemView.findViewById(R.id.calling);
            latWCBtn = (LinearLayout) itemView.findViewById(R.id.latWCBtn);

            layout_quotation = (LinearLayout) itemView.findViewById(R.id.layout_quotation);
            layhideshow = (LinearLayout) itemView.findViewById(R.id.layhideshow);

        }

    }

    /**
     * Sort shopping list by name descending
     */
    public void sortByNameDesc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object2.getSt_date().compareToIgnoreCase(object1.getSt_date());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /**
     * Sort shopping list by name ascending
     */
    public void sortByNameAsc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object1.getSt_date().compareToIgnoreCase(object2.getSt_date());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /**
     * Sort shopping list by name descending
     */
    public void sortByFrequencyDesc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object2.getFrequency().compareToIgnoreCase(object1.getFrequency());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /**
     * Sort shopping list by name ascending
     */
    public void sortByFrequencyAsc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object1.getFrequency().compareToIgnoreCase(object2.getFrequency());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /**
     * Sort shopping list by name descending
     */
    public void sortByTimingsDesc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object2.getTimings().compareToIgnoreCase(object1.getTimings());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /**
     * Sort shopping list by name ascending
     */
    public void sortByTimingsAsc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object1.getTimings().compareToIgnoreCase(object2.getTimings());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /**
     * Sort shopping list by name descending
     */
    public void sortByBranchDesc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object2.getBranch_name().compareToIgnoreCase(object1.getBranch_name());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    /**
     * Sort shopping list by name ascending
     */
    public void sortByBranchAsc() {
        Comparator<OnGoingBatchDAO> comparator = new Comparator<OnGoingBatchDAO>() {

            @Override
            public int compare(OnGoingBatchDAO object1, OnGoingBatchDAO object2) {
                return object1.getBranch_name().compareToIgnoreCase(object2.getBranch_name());
            }
        };
        Collections.sort(data, comparator);
        notifyDataSetChanged();
    }

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }
}
