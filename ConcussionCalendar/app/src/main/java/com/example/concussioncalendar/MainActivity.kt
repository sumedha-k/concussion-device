// import com.example.concussioncalendar.R // this is from when this file was part of natasha's navigation project

package com.example.concussioncalendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class MainActivity : AppCompatActivity() {

    private var selectedDate: LocalDate? = null // because private, only visible in this file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val calendarView: CalendarView = findViewById(R.id.calendarView)

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendarDayText)

            // Will be set when this container is bound
            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    // Check the day owner as we do not want to select in or out dates.
                    if (day.owner == DayOwner.THIS_MONTH) {
                        // Keep a reference to any previous selection
                        // in case we overwrite it and need to reload it.
                        val currentSelection = selectedDate
                        if (currentSelection == day.date) {
                            // If the user clicks the same date, clear selection.
                            selectedDate = null
                            // Reload this date so the dayBinder is called
                            // and we can REMOVE the selection background.
                            calendarView.notifyDateChanged(currentSelection)
                        } else {
                            selectedDate = day.date
                            // Reload the newly selected date so the dayBinder is
                            // called and we can ADD the selection background.
                            calendarView.notifyDateChanged(day.date)
                            if (currentSelection != null) {
                                // We need to also reload the previously selected
                                // date so we can REMOVE the selection background.
                                calendarView.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }
            }
        }

        // With ViewBinding
        // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)
            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                // Set the calendar day for this container
                container.day = day
                // Set the day text
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.alpha = 1f // sets this month's dates to 100% opacity
                    // Show the month dates. Remember that views are recycled!
                    // textView.visibility = View.VISIBLE
                    if (day.date == selectedDate) {
                        // If this is the selected date, show a round background and change the text color.
                        // textView.setTextColor(Color.rgb(0x65,0x68,0xb8)) // same color as dayView background, figure out how to do this by referencing the color variable i made in colors.xml
                        textView.setTextColor(Color.rgb(0x37,0x00,0xb3)) // same color as header background
                        // textView.setTextColor(Color.BLACK)
                        textView.setBackgroundResource(R.drawable.selection_background)
                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        textView.setTextColor(Color.WHITE)
                        textView.background = null
                    }
                } else {
                    // inDates and outDates appear, but grayed-out
                    textView.setTextColor(Color.WHITE) // sets in and outDate color
                    textView.alpha = 0.2f // sets in and outDates to 50% opacity of above color. if you don't specify color in line above, automatically does gray
                    // Hide in and out dates
                    // textView.visibility = View.INVISIBLE
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.headerTextView)
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.setTextColor(Color.WHITE)
            }
        }
    }
}