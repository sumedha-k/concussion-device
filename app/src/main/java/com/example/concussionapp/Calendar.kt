package com.example.concussionapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.concussionapp.R
import android.animation.ValueAnimator
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.concussionapp.databinding.FragmentCalendarBinding
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [Calendar.newInstance] factory method to
 * create an instance of this fragment.
 */
class Calendar : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var _binding: FragmentCalendarBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding = FragmentCalendarBinding.bind(view) this is a val, cannot be reassigned. this was in kizitonwose's example

        val calendarView: CalendarView = view.findViewById(R.id.calendarView)

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendarDayText)

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
                        textView.setTextColor(Color.WHITE) // same color as header background
                        textView.setBackgroundResource(R.drawable.selection_background)
                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        // textView.setTextColor(Color.rgb(0x37, 0x00, 0xb3))
                        textView.setTextColor(Color.BLACK)
                        textView.background = null
                    }
                } else {
                    // inDates and outDates appear, but grayed-out
                    textView.setTextColor(Color.rgb(0x37, 0x00, 0xb3)) // sets in and outDate color
                    textView.alpha = 0.2f // sets in and outDates to 50% opacity of above color. if you don't specify color in line above, automatically does gray
                    // Hide in and out dates
                    // textView.visibility = View.INVISIBLE
                }
                if (day.date == today) {
                    textView.setTextColor(getResources().getColor(R.color.bmes_color))
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

    /** override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Log.d("debugTag", "hello world") // idek how to get this to work <3 but i keep

    // getActivity()?.setContentView(R.layout.fragment_calendar) wait holy shit this is useless in Fragment. this why we return a view in onCreateView
    val calendarView: CalendarView = inflatedView!!.findViewById(R.id.calendarView)  // TODO: resolve this!

    val currentMonth = YearMonth.now()
    val firstMonth = currentMonth.minusMonths(10)
    val lastMonth = currentMonth.plusMonths(10)
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
    calendarView.scrollToMonth(currentMonth)

    // not really sure what this if statement is for yet? she do be empty rn but imma leave anyways for now
    if (arguments != null) {
    mParam1 = arguments!!.getString(ARG_PARAM1)
    mParam2 = arguments!!.getString(ARG_PARAM2)
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
    }

    calendarView.dayBinder = object : DayBinder<DayViewContainer> {
    // Called only when a new container is needed.
    override fun create(view: View) = DayViewContainer(view)

    // Called every time we need to reuse a container.
    override fun bind(container: DayViewContainer, day: CalendarDay) {
    container.textView.text = day.date.dayOfMonth.toString()
    }
    }
    } **/

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Calendar.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Calendar {
            val fragment = Calendar()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}