package com.jackson_siro.visongbook.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jackson_siro.visongbook.R
import com.jackson_siro.visongbook.adapters.*
import com.jackson_siro.visongbook.data.SQLiteHelper
import com.jackson_siro.visongbook.models.*
import com.jackson_siro.visongbook.models.callback.*
import com.jackson_siro.visongbook.retrofitconfig.CallJson

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CcBooksLoad : AppCompatActivity(), SelectableViewHolder.OnItemSelectedListener {
    private val db = SQLiteHelper(this)
    private var recyclerView: RecyclerView? = null
    private var callbackBookCall: Call<CallbackCategory>? = null
    private var booksAdapter: BooksListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var fabdone: FloatingActionButton? = null
    private var selectedBooks: List<CategoryModel>? = null
    private var prefget: SharedPreferences? = null
    private var prefedit: SharedPreferences.Editor? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cc_books_load)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        prefget = PreferenceManager.getDefaultSharedPreferences(this)
        prefedit = prefget!!.edit()
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView = findViewById(R.id.slider_recycler_view)
        recyclerView!!.layoutManager = layoutManager

        fabdone = findViewById(R.id.fabdone)
        fabdone!!.setOnClickListener {
            try {
                confirmCheckout()
            } catch (ex: Exception) {
            }
        }

        requestAction()
    }

    override fun onItemSelected(selectableItem: SelectableBook) {
        selectedBooks = booksAdapter!!.selectedItems
        if (selectableItem.isSelected)
            Snackbar.make(recyclerView!!, "You've selected: " + selectableItem.getTitle() +
                    "\n" + selectedBooks!!.size + " songbooks selected", Snackbar.LENGTH_LONG).show()
        else
            Snackbar.make(recyclerView!!, "You've unselected: " + selectableItem.getTitle() +
                    "\n" + selectedBooks!!.size + " songbooks selected", Snackbar.LENGTH_LONG).show()
    }

    private fun requestAction() {
        swipeProgress(true)
        Handler().postDelayed({ requestData() }, 1000)
    }

    private fun swipeProgress(show: Boolean) {
        if (!show) {
            swipeRefreshLayout!!.isRefreshing = show
            return
        }
        swipeRefreshLayout!!.post { swipeRefreshLayout!!.isRefreshing = show }
    }

    private fun requestData() {
        val api = CallJson.callJson()
        callbackBookCall = api.BooksSelect()
        callbackBookCall!!.enqueue(object : Callback<CallbackCategory> {
            override fun onResponse(call: Call<CallbackCategory>, response: Response<CallbackCategory>) {
                val callbackBooks = response.body()
                if (callbackBooks != null) {
                    swipeProgress(false)
                    displayApiResult(callbackBooks.data)
                }
            }

            override fun onFailure(call: Call<CallbackCategory>, t: Throwable) {
                if (!call.isCanceled);
            }
        })
    }

    private fun displayApiResult(books: List<CategoryModel>) {
        val booksItems = ArrayList<CategoryModel>()
        for (i in books.indices) {
            booksItems.add(CategoryModel(
                    books[i].getBookid(),
                    books[i].getCategoryid(),
                    books[i].getTitle(),
                    (i + 1).toString() + "/" + books.size,
                    books[i].getQcount(),
                    books[i].getPosition(),
                    books[i].getContent(),
                    books[i].getBackpath()
            )
            )
        }

        booksAdapter = BooksListAdapter(this, booksItems, true)
        recyclerView!!.adapter = booksAdapter

        showDisclaimer()
    }

    private fun showDisclaimer() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Just a minute!")
        builder.setMessage("Take time to select a songbook at a time so as to setup your vSongBook Collection.\n\n" +
                "Having selected a songbook swipe to the right to select another till you are done.\n\n" +
                "Once done that, proceed to press the 'TICK' button at the bottom right for next stage")
        builder.setNegativeButton("Okay Got it") { arg0, arg1 -> }
        builder.show()
    }

    private fun confirmCheckout() {
        var selectedones = ""
        val builder = AlertDialog.Builder(this)
        if (selectedBooks!!.size > 0) {
            for (i in selectedBooks!!.indices) {
                selectedones = selectedones + (i + 1) + ". " + selectedBooks!![i].getTitle() +
                        " (" + selectedBooks!![i].getQcount() + " songs).\n"
            }

            builder.setTitle("Done with selecting?")
            builder.setMessage(selectedones)
            builder.setNegativeButton("Go Back") { arg0, arg1 -> }
            builder.setPositiveButton("Proceed") { arg0, arg1 -> loadBooks() }
        } else {
            builder.setTitle("Just a Minute ...")
            builder.setMessage("Oops! This is so heart breaking. You haven't selected a book, you expect things to be okay. You got to select at least one book." + "\n\n By the way we can always bring you back here to select afresh. But for now select at least one.")
            builder.setPositiveButton("Okay, Got it") { arg0, arg1 -> }
        }
        builder.show()
    }

    fun loadBooks() {
        for (count in selectedBooks!!.indices) {
            val book = CategoryModel(
                    selectedBooks!![count].getBookid(),
                    selectedBooks!![count].getCategoryid(),
                    selectedBooks!![count].getTitle(),
                    selectedBooks!![count].getTags(),
                    selectedBooks!![count].getQcount(),
                    selectedBooks!![count].getPosition(),
                    selectedBooks!![count].getContent(),
                    selectedBooks!![count].getBackpath()
            )
            db.addBook(book)
            Log.d("MyAdapter", book.title + " added")
        }

        var selectedones = selectedBooks!![0].getCategoryid().toString() + ""
        for (i in 1 until selectedBooks!!.size) {
            selectedones = selectedones + "," + selectedBooks!![i].getCategoryid()
        }
        prefedit!!.putBoolean("app_books_loaded", true)
        prefedit!!.putBoolean("app_books_reload", false)
        prefedit!!.putString("app_selected_books", selectedones)
        prefedit!!.commit()
        startActivity(Intent(this@CcBooksLoad, CcSongsLoad::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bb_proceed, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_proceed -> {
                confirmCheckout()
                return true
            }

            else -> return false
        }
    }

}
