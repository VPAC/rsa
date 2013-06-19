package org.vpac.ndg.cli.smadaptor.local;

import java.util.Date;

import org.vpac.ndg.query.Progress;

public class CliQueryProgress implements Progress {

	private int nsteps;
	private int step;
	private Date start;
	private Date lastTime;
	private int percentage;
	private long totalQuanta;
	private long lastProcessedQuanta;
	private long totalProcessedQuanta;

	@Override
	public void setNsteps(int nsteps) {
		this.nsteps = nsteps;
		step = 0;
		totalQuanta = 0;
		lastProcessedQuanta = 0;
		totalProcessedQuanta = 0;
		start = new Date();
		lastTime = new Date();
		percentage = -1;
	}

	@Override
	public void setStep(int step, String message) {
		if (this.step == 0) {
			// Set the start time when the first step starts.
			start = new Date();
		}
		this.step = step;
		System.out.println(String.format("%s (%d/%d)", message, step, nsteps));
	}

	@Override
	public void setTotalQuanta(long totalQuanta) {
		this.totalQuanta = totalQuanta;
	}

	@Override
	public void addProcessedQuanta(long processedQuanta) {
		totalProcessedQuanta += processedQuanta;
		double fraction = (double)totalProcessedQuanta / (double)totalQuanta;
		int percentage = (int)(fraction * 100.0f);

		if (percentage == this.percentage)
			return;

		this.percentage = percentage;
		updateStats();
	}

	private void updateStats() {
		Date currentTime = new Date();
		long timePeriod = currentTime.getTime() - lastTime.getTime();
		long processedQuanta = totalProcessedQuanta - lastProcessedQuanta;
		double rate = (double)processedQuanta / ((double)timePeriod / 1000.0);

		long remainingQuanta = totalQuanta - totalProcessedQuanta;
		double remainingTime = (double)remainingQuanta / rate;

		String timecode = formatTimecode((long)remainingTime);
		String rateStr = formatQuanta((long)rate);

		System.out.format("\r%3d%% T-%s (%spps)       ",
				percentage, timecode, rateStr);

		this.lastTime = currentTime;
		this.lastProcessedQuanta = this.totalProcessedQuanta;
	}

	private String formatTimecode(long seconds) {
		long minutes = seconds / 60;
		long hours = minutes / 60;
		minutes %= 60;
		seconds %= 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	private String formatQuanta(long quanta) {
		String suffix;
		double scaledQuanta;
		if (quanta >= 1000000000) {
			scaledQuanta = (double)quanta / 1000000000.0;
			suffix = "G";
		} else if (quanta >= 1000000) {
			scaledQuanta = (double)quanta / 1000000.0;
			suffix = "M";
		} else if (quanta >= 1000) {
			scaledQuanta = (double)quanta / 1000.0;
			suffix = "k";
		} else {
			scaledQuanta = (double)quanta;
			suffix = "";
		}
		return String.format("%.1f%s", scaledQuanta, suffix);
	}

	@Override
	public void finishedStep() {
		System.out.println();
	}

	@Override
	public void finished() {
		Date finish = new Date();
		double totalDuration = (double)(finish.getTime() - start.getTime()) / 1000.0;
		String durationStr = formatTimecode((long)totalDuration);
		String quantaStr = formatQuanta(totalQuanta);
		double rate = totalQuanta / totalDuration;
		String rateStr = formatQuanta((long)rate);
		System.out.format("Wrote %s cells in %s (%spps)\n", quantaStr,
				durationStr, rateStr);
	}

}
