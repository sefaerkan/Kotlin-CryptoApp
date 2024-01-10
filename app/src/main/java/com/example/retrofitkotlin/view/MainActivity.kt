package com.example.retrofitkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitkotlin.adapter.CryptoAdapter
import com.example.retrofitkotlin.databinding.ActivityMainBinding
import com.example.retrofitkotlin.model.CryptoModel
import com.example.retrofitkotlin.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://raw.githubusercontent.com/"
    private var cryptoModels : ArrayList<CryptoModel>? = null
    private var adapter: CryptoAdapter? = null
    private lateinit var binding: ActivityMainBinding

    //Disposable -> Kullan At
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        compositeDisposable = CompositeDisposable()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    private fun loadData() {

        //RxJava
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)

        compositeDisposable?.add(retrofit.getData() //Gelen veriyi arka alanda dinliyor, main threade işliyor ve handler response yolluyor.
            .subscribeOn(Schedulers.io()) //Arka Plan
            .observeOn(AndroidSchedulers.mainThread()) //Ön Plan
            .subscribe(this::handlerResponse))

        /*
        Eski hali
        val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()
        call.enqueue(object: Callback<List<CryptoModel>>{

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            adapter = CryptoAdapter(it)
                            binding.recyclerView.adapter = adapter
                        }
                    }
                }
            }
        })
        */
    }

    private fun handlerResponse(cryptoList : List<CryptoModel>) {
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            adapter = CryptoAdapter(it)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear() //Call'leri Temizliyoruz -> Hafıza yönetimi
    }

}