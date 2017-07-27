package de.elliepotato.glitterbot.util

import java.util.*



/**
 * Created by Ellie on 27.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class DateUtil {

    fun formatDateDiff(date: Long): String {
        val c = GregorianCalendar()
        c.timeInMillis = date
        val now = GregorianCalendar()
        return formatDateDiff(now, c)
    }

    fun formatDateDiff(fromDate: Calendar, toDate: Calendar): String {
        var future = false
        if (toDate == fromDate) {
            return "now"
        }
        if (toDate.after(fromDate)) {
            future = true
        }
        val sb = StringBuilder()
        val types = intArrayOf(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND)
        val names = arrayOf("year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes", "second", "seconds")
        var accuracy = 0

        var i = 0
        while (i < types.size) {
            println(names[i])
            if (accuracy > 2)
                break

            val diff = dateDiff(types[i], fromDate, toDate, future)
            if (diff > 0) {
                accuracy++
                sb.append(" ").append(diff).append(" ").append(names[i *2  + (if (diff > 1) 1 else 0)])
            }
            i++
        }
        if (sb.isEmpty()) {
            return "now"
        }
        return sb.toString().trim { it <= ' ' }

    }

    private fun dateDiff(type: Int, fromDate: Calendar, toDate: Calendar, future: Boolean): Int {
        var diff = 0
        var savedDate = fromDate.timeInMillis
        while (future && !fromDate.after(toDate) || !future && !fromDate.before(toDate)) {
            savedDate = fromDate.timeInMillis
            fromDate.add(type, if (future) 1 else -1)
            diff++
        }
        diff--
        fromDate.timeInMillis = savedDate
        return diff
    }

    private object container {
        val instance = DateUtil()
    }

    companion object {
        val instance: DateUtil by lazy {
            container.instance
        }
    }


}