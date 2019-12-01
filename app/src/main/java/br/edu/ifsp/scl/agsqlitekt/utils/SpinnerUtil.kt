package br.edu.ifsp.scl.agsqlitekt.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import br.edu.ifsp.scl.agsqlitekt.R
import androidx.appcompat.app.AppCompatActivity



class SpinnerUtil: AppCompatActivity() {
    companion object {
        fun setSelect(context: Context, compareValue: String, operacao: Spinner, strArray: String) {
            val arrayName_ID = context.getResources().getIdentifier(strArray, "array", context.getPackageName())
            val adapter = ArrayAdapter.createFromResource(
                context,
                arrayName_ID,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            operacao.setAdapter(adapter)
            if (compareValue != null) {
                val spinnerPosition = adapter.getPosition(compareValue)
                operacao.setSelection(spinnerPosition)
            }
        }
    }
}