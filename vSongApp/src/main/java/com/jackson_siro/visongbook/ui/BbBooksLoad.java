package com.jackson_siro.visongbook.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.*;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.*;
import com.jackson_siro.visongbook.models.Callback.*;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.CallJson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BbBooksLoad extends AppCompatActivity implements SelectableViewHolder.OnItemSelectedListener{
    private SQLiteHelper db = new SQLiteHelper(this);
    private RecyclerView recyclerView;
    private Call<CallbackCategory> callbackBookCall;
    private SliderAdapter sliderAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabdone;
    private List<CategoryModel> selectedBooks;
    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb_books_load);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.slider_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        fabdone = findViewById(R.id.fabdone);
        fabdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCheckout();
            }
        });

        requestAction();
    }

    @Override
    public void onItemSelected(SelectableBook selectableItem) {
        selectedBooks = sliderAdapter.getSelectedItems();
        if (selectableItem.isSelected())
            Snackbar.make(recyclerView,"You've selected: "+ selectableItem.getTitle()+
                "\n" + selectedBooks.size() + " songbooks selected",Snackbar.LENGTH_LONG).show();
        else
            Snackbar.make(recyclerView,"You've unselected: "+ selectableItem.getTitle()+
                    "\n" + selectedBooks.size() + " songbooks selected",Snackbar.LENGTH_LONG).show();
    }

    private void requestAction(){
        swipeProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData();
            }
        }, 1000);
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipeRefreshLayout.setRefreshing(show);
            return;
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    private void requestData() {
        API api = CallJson.callJson();
        callbackBookCall = api.BooksSelect();
        callbackBookCall.enqueue(new Callback<CallbackCategory>() {
            @Override
            public void onResponse(Call<CallbackCategory> call, Response<CallbackCategory> response) {
                CallbackCategory callbackBooks = response.body();
                if (callbackBooks != null) {
                    swipeProgress(false);
                    displayApiResult(callbackBooks.data);
                }
            }

            @Override
            public void onFailure(Call<CallbackCategory> call, Throwable t) {
                if (!call.isCanceled()) ;
            }
        });
    }

    private void displayApiResult(final List<CategoryModel> books) {
        List<CategoryModel> booksItems = new ArrayList<>();
        for (int i = 0; i < books.size(); i ++) {
            booksItems.add( new CategoryModel(
                    books.get(i).getBookid(),
                    books.get(i).getCategoryid(),
                    books.get(i).getTitle(),
                    (i + 1) + "/" + books.size(),
                    books.get(i).getQcount(),
                    books.get(i).getPosition(),
                    books.get(i).getContent(),
                    books.get(i).getBackpath()
            )
            );
        }

        sliderAdapter = new SliderAdapter(this, booksItems, true);
        recyclerView.setAdapter(sliderAdapter);

        showFeedback();
    }

    private void showFeedback(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Just a minute!");
        builder.setMessage("Take time to select a songbook at a time so as to setup your vSongBook Collection.\n\n" +
                "Having selected a songbook swipe to the right to select another till you are done.\n\n" +
                "Once done that, proceed to press the 'TICK' button at the bottom right for next stage");
        builder.setNegativeButton("Okay Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.show();
    }

    private void confirmCheckout(){
        String selectedones = "";
        for (int i = 0; i<selectedBooks.size(); i++){
            selectedones = selectedones  + (i + 1) + ". " + selectedBooks.get(i).getTitle() +
                    " (" + selectedBooks.get(i).getQcount() + " songs).\n";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you Okay with your Selection?");
        builder.setMessage("You selected:\n\n" + selectedones);
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                loadBooks();
            }
        });
        builder.show();
    }
    
    public void loadBooks(){
        for (int count = 0; count < selectedBooks.size(); count ++) {
            CategoryModel book = new CategoryModel(
                selectedBooks.get(count).getBookid(),
                selectedBooks.get(count).getCategoryid(),
                selectedBooks.get(count).getTitle(),
                selectedBooks.get(count).getTags(),
                selectedBooks.get(count).getQcount(),
                selectedBooks.get(count).getPosition(),
                selectedBooks.get(count).getContent(),
                selectedBooks.get(count).getBackpath()
            );
            db.addBook(book);
            Log.d("MyAdapter", book.title + " added");
        }
        
        String selectedones = selectedBooks.get(0).getCategoryid() + "";
        for (int i = 1; i<selectedBooks.size(); i++){
            selectedones = selectedones  + "," + selectedBooks.get(i).getCategoryid();
        }
        prefedit.putBoolean("app_books_loaded", true);
        prefedit.putBoolean("app_books_reload", false);
        prefedit.putString("app_selected_books", selectedones);
        prefedit.commit();
        startActivity(new Intent(BbBooksLoad.this, BbSongsLoad.class));
        finish();
    }
    
}
