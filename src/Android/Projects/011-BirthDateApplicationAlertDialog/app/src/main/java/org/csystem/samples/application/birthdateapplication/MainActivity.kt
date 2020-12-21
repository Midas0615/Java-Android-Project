package org.csystem.samples.application.birthdateapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity() {
    private lateinit var mEditTextBirthDate : EditText
    private lateinit var mTextViewAge: TextView
    private val mFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    private fun getBirthDateMessage(birthDay: LocalDate, now: LocalDate) = when {
        now.isAfter(birthDay) -> "Geçmiş doğum gününüz kutlu olsun"
        now.isBefore(birthDay) -> "Doğum gününüzü şimdiden kutlu olsun"
        else -> "Doğum gününüz kutlu olsun"
    }

    private fun showAlert(message: String)
    {
        val dlg = android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK") { _, _  -> }
                .create()

        dlg.show()
    }

    private fun initViews()
    {
        mEditTextBirthDate = findViewById(R.id.mainActivityEditTextBirthDate)
        mTextViewAge = findViewById(R.id.mainActivityTextViewAge)
    }

    private fun initialize()
    {
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    fun onOKButtonClicked(view: View)
    {
        try {
            val birthDate = LocalDate.parse(mEditTextBirthDate.text.toString(), mFormatter)
            val now = LocalDate.now()
            val birthDay = birthDate.withYear(now.year)
            val age = ChronoUnit.DAYS.between(birthDate, now) / 365.0

            if (age < 0)
                throw DateTimeParseException("", "", 0);

            val message = getBirthDateMessage(birthDay, now)

            mTextViewAge.setText("Yeni yaşınız:%s".format(age))
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
        catch (ex:DateTimeParseException) {
            showAlert("Geçersiz tarih formatı")
        }
    }

    fun onExitButtonClicked(view: View)
    {
        finish()
    }
}