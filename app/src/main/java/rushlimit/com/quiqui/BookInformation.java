package rushlimit.com.quiqui;

import android.widget.ImageView;

public class BookInformation {
    String title;
    String author;
    String isbn;
    String seller;
    String condition;
    String category;
    String price;
    int pic;
//    int pic = R.drawable.ic_book;

    BookInformation(String title, String author, String isbn, String seller, String condition, String category, String price, int pic){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.seller = seller;
        this.condition = condition;
        this.category = category;
        this.price = price;
        this.pic = pic;
    }
}
