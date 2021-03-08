package org.csystem.samples.application.samplecounterdonotdo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.csystem.samples.application.samplecounterdonotdo.databinding.ActivityMainBinding
import java.lang.ref.WeakReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mHandler = MyHandler(this)
    private lateinit var mDateTimeTimer: Timer

    private class MyHandler(mainActivity: MainActivity) : Handler(Looper.myLooper()!!) {
        private val mWeakReference = WeakReference(mainActivity)

        private fun handleCounter(mainActivity: MainActivity, msg: Message)
        {
            val counter = msg.obj.toString().toLong()

            mainActivity.mBinding.mainActivityTextViewCounter.text = counter.toString()
            Toast.makeText(mainActivity, counter.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun handleMessage(msg: Message)
        {
            val mainActivity = mWeakReference.get()!!

            when (msg.what) {
                0 -> mainActivity.title = msg.obj.toString()
                1 -> handleCounter(mainActivity, msg)
            }
        }
    }

    private fun onStartButtonClicked()
    {
        mBinding.mainActivityButtonStart.isEnabled = false

        var counter = 1L

        Thread { //Dikkat activity yok edildiğinde bu thread'in de sonlandırılması gerekir. Henüz yapmadık
            while (true) {
                mHandler.sendMessage(mHandler.obtainMessage(1, counter++))
                Thread.sleep(1000)
            }
        }.start()
    }

    private fun initStartButton()
    {
        mBinding.mainActivityButtonStart.setOnClickListener { onStartButtonClicked() }
    }

    private fun initDateTimeTimer()
    {
        val task = object: TimerTask() {
            override fun run()
            {
                val dateTimeTimerStr = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss").format(LocalDateTime.now())

                val msg = mHandler.obtainMessage(0, dateTimeTimerStr)

                mHandler.sendMessage(msg)
            }
        }
        mDateTimeTimer = Timer().apply { scheduleAtFixedRate(task, 0, 1000) } //Bu timer'ın da activity yok edildiğinde durdurulması gerekir
    }

    private fun initBinding()
    {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    private fun initialize()
    {
        initBinding()
        initDateTimeTimer()
        initStartButton()

    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        initialize()
    }

}