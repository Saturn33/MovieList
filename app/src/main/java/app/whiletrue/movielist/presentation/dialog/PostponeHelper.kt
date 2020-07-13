package app.whiletrue.movielist.presentation.dialog

import android.app.DatePickerDialog
import android.view.View
import app.whiletrue.movielist.data.entity.MovieDTO
import app.whiletrue.movielist.presentation.viewmodel.MovieListViewModel
import java.util.*

object PostponeHelper {
    fun selectDate(view: View, item: MovieDTO,  viewModel: MovieListViewModel?) {
        DatePickerDialog(view.context).apply {
            item.postponeMillis?.let {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it
                updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            }
            setOnDateSetListener { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth, 10, 0, 0) // 10 утра - хорошее время для просмотра фильмов!
                viewModel?.onMoviePostpone(item, cal.time)
            }
        }.show()
    }
}
