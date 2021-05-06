package com.example.concussionapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import com.example.concussionapp.databinding.FragmentCalendarBinding
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.YearMonth
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
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