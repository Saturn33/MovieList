package ru.otus.saturn33.movielist.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import ru.otus.saturn33.movielist.R

class ExitDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_exit)

        val buttonYes = findViewById<Button>(R.id.yes_button)
        val buttonNo = findViewById<Button>(R.id.no_button)

        buttonYes.setOnClickListener {
            cancel()
        }

        buttonNo.setOnClickListener {
            dismiss()
        }
    }
}
