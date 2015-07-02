package rushlimit.com.quiqui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private List<Information> data = Collections.emptyList();
    private Context context;

    SharedPreferences sP;
    SharedPreferences.Editor editor;

    public DrawerAdapter(Context context, List<Information> data){
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.data = data;

        sP = context.getSharedPreferences("rushlimit.com.quiqui",
                context.MODE_PRIVATE);
        editor = sP.edit();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_drawer_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent;

            switch(getPosition()){
                case 0:
                    //Home
                    intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.d("HomeItemonDrawer", "StartedMainActivity");
                    context.startActivity(intent);
                    //get rid of previous stack
                    break;
//                case 1:
//                    //Categories
//                    context.startActivity(new Intent(context, SubActivity.class));
//                    break;
                case 2:
                    //Sell a book
                    context.startActivity(new Intent(context, ListingInfoActivity.class));
                    break;
                case 3:
                    //Profile
                    context.startActivity(new Intent(context, ProfileActivity.class));
                    break;
                case 4:
                    //Support
                    context.startActivity(new Intent(context, SupportActivity.class));
                    break;
//                case 5:
                      //Setting
//                    context.startActivity(new Intent(context, SubActivity.class));
//                    break;
                case 6:
                    //About Us
                    intent = new Intent(context, AboutUsActivity.class);
                    context.startActivity(intent);
//                    context.startActivity(new Intent(context, AboutUsActivity.class));
                    break;
                case 7:
                    //Log Out
                    if(sP.getBoolean("gmail", false)) {
                        intent = new Intent(context, SignUpActivity.class);
                        intent.putExtra("signedOut", true);
                        editor.putBoolean("gmail", false);
                        editor.apply();
                        context.startActivity(intent);
                    } else if(sP.getBoolean("facebook",false)) {
                        intent = new Intent(context, SignUpActivity.class);
                        intent.putExtra("signedOut", true);
                        editor.putBoolean("facebook", false);
                        editor.putBoolean("facebookSignOut", true);
                        editor.apply();
                        context.startActivity(intent);
                    }
                    break;

            }

           // DrawerAdapter adapter = context.findViewById(R.id.fragment_navigation_drawer)
        }
    }
}
