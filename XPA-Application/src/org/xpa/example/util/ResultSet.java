package org.xpa.example.util;

import java.io.Serializable;

public class ResultSet implements Serializable {

	public static final String KEY = "resultset";
	
	private static final long serialVersionUID = 1L;
	
	private long runCount, min, max;
	private double avg;
	
	public long getRunCount() {
		return runCount;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public double getAvg() {
		return avg;
	}

	public ResultSet(long runCount, long min, long max, double avg) {
		this.runCount = runCount;
		this.min = min;
		this.max = max;
		this.avg = avg;
	}

}
