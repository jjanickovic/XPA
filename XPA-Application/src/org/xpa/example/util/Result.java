package org.xpa.example.util;

import java.util.List;

public class Result {

	public static ResultSet createResult(List<Long> runs) {
		long min = -1;
		long max = -1;
		double avg = Double.NaN;
		long total = 0;
		
		for(long value : runs) {
			if(min > value || min < 0) {
				min = value;
			}
			
			if(max < value) {
				max = value;
			}
			
			total += value;
		}
		
		avg = (double) total / (double) runs.size();
		ResultSet resultSet = new ResultSet(runs.size(), min, max, avg);
		return resultSet;
	}
	
}
