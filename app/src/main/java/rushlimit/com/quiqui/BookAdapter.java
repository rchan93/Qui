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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{
    private LayoutInflater inflater;
    private List<BookInformation> data = Collections.emptyList();
    private Context context;

    SharedPreferences sP;
    SharedPreferences.Editor editor;

    public BookAdapter(Context context, List<BookInformation> data){
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.data = data;

        sP = context.getSharedPreferences("rushlimit.com.quiqui",
                context.MODE_PRIVATE);
        editor = sP.edit();
    }
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_book_item, parent, false);
        BookViewHolder holder = new BookViewHolder(view);
        return holder;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookInformation current = data.get(position);
        holder.bookPic.setImageResource(current.pic);
        holder.bookTitle.setText("Title: " + current.title);
        holder.bookISBN.setText("ISBN: " + current.isbn);
        //holder.bookSeller.setText(current.seller);
        holder.bookCondition.setText("Condition: " + current.condition);
        holder.bookPrice.setText("Price: " + current.price);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView bookPic;
        TextView bookTitle;
        TextView bookISBN;
        //TextView bookSeller;
        TextView bookCondition;
        TextView bookPrice;
        public BookViewHolder(View itemView) {
            super(itemView);

            bookPic = (ImageView) itemView.findViewById(R.id.bookIcon);
            bookTitle = (TextView) itemView.findViewById(R.id.bookTitle);
            bookISBN = (TextView) itemView.findViewById(R.id.bookISBN);
            //bookSeller = (TextView) itemView.findViewById(R.id.bookSeller);
            bookCondition = (TextView) itemView.findViewById(R.id.bookCondition);
            bookPrice = (TextView) itemView.findViewById(R.id.bookPrice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            int pos = getPosition();
            intent = new Intent(context, ListingInfoActivity.class);


        }
    }
}
