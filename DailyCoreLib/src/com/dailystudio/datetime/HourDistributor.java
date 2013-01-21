package com.dailystudio.datetime;

import com.dailystudio.datetime.CalendarUtils;

public class HourDistributor {
	
	public long[] calculateHourDistrib(long start, long end) {
		if (start >= end) {
			return null;
		}
		
		long startHour = start - start % CalendarUtils.HOUR_IN_MILLIS;
		long endHour = end - end % CalendarUtils.HOUR_IN_MILLIS;

		if (end - start > CalendarUtils.DAY_IN_MILLIS) {
			endHour += ((end - start) / CalendarUtils.DAY_IN_MILLIS) * CalendarUtils.DAY_IN_MILLIS;
		} else {
			if (endHour < startHour) {
				endHour += 24 * CalendarUtils.HOUR_IN_MILLIS;
			}
		}
/*		Logger.debug("[%s - %s]: [%s - %s]",
				CalendarUtils.timeToReadableString(start),
				CalendarUtils.timeToReadableString(end),
				CalendarUtils.timeToReadableString(startHour),
				CalendarUtils.timeToReadableString(endHour));
*/		
		
		long[] hoursDistribution = new long[24];
		
		long time = 0;
		long distrib = 0;
		int hourIndex = -1;
		for (time = startHour; time <= endHour; time += CalendarUtils.HOUR_IN_MILLIS) {
			hourIndex = CalendarUtils.getHour(time);
					
			distrib = Math.min(time + CalendarUtils.HOUR_IN_MILLIS, end) 
					- Math.max(time, start);
				
			hoursDistribution[hourIndex] += distrib;
		}
		
		return hoursDistribution;
	}

}
