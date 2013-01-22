package com.dailystudio.datetime;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.development.Logger;

public class TimeSpanUtils {
	
	public long[] calculateHourDistribution(long start, long end) {
/*		Logger.debug("[%s - %s]",
				CalendarUtils.timeToReadableString(start),
				CalendarUtils.timeToReadableString(end));
*/
		if (start >= end) {
			return null;
		}
		
		long startHour = start - start % CalendarUtils.HOUR_IN_MILLIS;
		long endHour = end - end % CalendarUtils.HOUR_IN_MILLIS;

/*		if (end - start > CalendarUtils.DAY_IN_MILLIS) {
			endHour += ((end - start) / CalendarUtils.DAY_IN_MILLIS) * CalendarUtils.DAY_IN_MILLIS;
		} else {
			if (CalendarUtils.getHour(endHour) < CalendarUtils.getHour(startHour)) {
				endHour += 24 * CalendarUtils.HOUR_IN_MILLIS;
			}
		}
*/
		Logger.debug("hour range: [%s - %s]",
				CalendarUtils.timeToReadableString(startHour),
				CalendarUtils.timeToReadableString(endHour));
		
		long[] hoursDistribution = new long[24];
		
		long time = 0;
		long distrib = 0;
		long dstart = 0;
		long dend = 0;
		int hourIndex = -1;
		for (time = startHour; time <= endHour; time += CalendarUtils.HOUR_IN_MILLIS) {
			hourIndex = CalendarUtils.getHour(time);
			
			dstart = Math.max(time, start);
			dend = Math.min(time + CalendarUtils.HOUR_IN_MILLIS, end);
			
			distrib = dend - dstart;
				
/*			Logger.debug("[hour %d]: time = %s, distrib = %s - %s = %s",
					hourIndex,
					CalendarUtils.timeToReadableString(time),
					CalendarUtils.timeToReadableString(dend),
					CalendarUtils.timeToReadableString(dstart),
					CalendarUtils.durationToReadableString(distrib));
*/					
			hoursDistribution[hourIndex] += distrib;
		}
		
		return hoursDistribution;
	}

}
