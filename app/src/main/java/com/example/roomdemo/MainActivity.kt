package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.roomdemo.database.UserEntity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        findViewById<RecyclerView>(R.id.recycler_view).apply {
//
//        }
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerViewAdapter

            val divider = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(divider)
        }
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.getAllUsersObservers().observe(this, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        save_button.setOnClickListener {
            val nameId  = name.text.toString()
            val emailAddress  = email.text.toString()
            val phoneNumber = phone.text.toString()
            if(save_button.text.equals("Save")) {
                val user = UserEntity(0, nameId, emailAddress, phoneNumber)
                viewModel.insertUserInfo(user)
            } else {
                val user = UserEntity(name.getTag(name.id).toString().toInt(), nameId, emailAddress, phoneNumber)
                viewModel.updateUserInfo(user)
                save_button.text = "Save"
            }
            name.setText("")
            email.setText("")
        }
    }


    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)
    }

    override fun onItemClickListener(user: UserEntity) {
        name.setText(user.name)
        email.setText(user.email)
        phone.setText(user.phone)
        name.setTag(name.id, user.id)
        save_button.text = "Update"
    }
}