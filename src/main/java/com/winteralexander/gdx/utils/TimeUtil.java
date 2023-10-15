package com.winteralexander.gdx.utils;

import com.badlogic.gdx.utils.StringBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Utility class to display time
 * <p>
 * Created on 2017-02-01.
 *
 * @author Alexander Winter
 */
public class TimeUtil {
	public static String displayFromMillis(long millisInterval) {
		int hours = (int)(millisInterval / 1000 / 60 / 60);
		int minutes = (int)(millisInterval / 1000 / 60 % 60);
		int seconds = (int)(millisInterval / 1000 % 60);
		int millis = (int)(millisInterval % 1000);

		return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
	}

	public static String displayFromTicks(int ticks, float subFrame) {
		return displayFromMillis(toMillis(ticks, subFrame));
	}

	public static void initTimeDisplay(StringBuilder sb) {
		sb.setLength(12);
		sb.setCharAt(2, ':');
		sb.setCharAt(5, ':');
		sb.setCharAt(8, '.');
	}

	public static void displayIntoFromMillis(StringBuilder sb, long millisTimestamp) {
		int hours = (int)(millisTimestamp / 1000 / 60 / 60);
		sb.setCharAt(0, (char)('0' + hours / 10));
		sb.setCharAt(1, (char)('0' + hours % 10));

		int minutes = (int)(millisTimestamp / 1000 / 60 % 60);
		sb.setCharAt(3, (char)('0' + minutes / 10));
		sb.setCharAt(4, (char)('0' + minutes % 10));

		int seconds = (int)(millisTimestamp / 1000 % 60);
		sb.setCharAt(6, (char)('0' + seconds / 10));
		sb.setCharAt(7, (char)('0' + seconds % 10));

		int millis = (int)(millisTimestamp % 1000);
		sb.setCharAt(9, (char)('0' + millis / 100));
		sb.setCharAt(10, (char)('0' + millis / 10 % 10));
		sb.setCharAt(11, (char)('0' + millis % 10));
	}

	public static void displayIntoFromTicks(StringBuilder sb, int ticks, float subFrame) {
		displayIntoFromMillis(sb, toMillis(ticks, subFrame));
	}

	/**
	 * Converts ticks and a subframe ratio to an amount of milliseconds
	 *
	 * @param length   length of the run
	 * @param subFrame subFrame ratio
	 * @return time in milliseconds
	 */
	public static long toMillis(int length, float subFrame) {
		return Math.round((length * 1000d - subFrame * 1000d) / 60d);
	}

	public static boolean isToday(long millis) {
		LocalDate date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
		return LocalDate.now().equals(date);
	}

	/**
	 * Parses duration described as a string with time unit suffixes
	 * e.g. 10h3m
	 *
	 * @param duration duration string
	 * @return number of milliseconds in duration
	 */
	public static long parseDuration(String duration) {
		long total = 0L;
		double curNumber = 0L;
		boolean reachedDecimal = false;
		boolean numberEntered = false;
		int decimalPosition = 1;

		for(char c : duration.toCharArray()) {
			boolean isDigit = c >= '0' && c <= '9';
			boolean isDot = c == '.';

			if(isDot) {
				if(reachedDecimal)
					throw new NumberFormatException("Multiple decimal dots in duration");

				reachedDecimal = true;
			} else if(isDigit) {
				if(!reachedDecimal) {
					curNumber *= 10.0;
					curNumber += c - '0';
				} else {
					curNumber += (c - '0') / Math.pow(10.0, decimalPosition);
					decimalPosition++;
				}
				numberEntered = true;
			} else if(numberEntered) {
				switch(c) {
					case 'y':
					case 'Y':
						total += Math.round(curNumber * 365.0 * 24.0 * 60.0 * 60.0 * 1000.0);
						break;

					case 'd':
					case 'D':
						total += Math.round(curNumber * 24.0 * 60.0 * 60.0 * 1000.0);
						break;

					case 'h':
					case 'H':
						total += Math.round(curNumber * 60.0 * 60.0 * 1000.0);
						break;

					case 'm':
					case 'M':
						total += Math.round(curNumber * 60.0 * 1000.0);
						break;

					case 's':
					case 'S':
						total += Math.round(curNumber * 1000.0);
						break;

					default:
						throw new NumberFormatException("Invalid time unit '" + c + "' in duration");
				}
				curNumber = 0L;
				reachedDecimal = false;
				numberEntered = false;
				decimalPosition = 1;
			} else // means we are seeing a non digit non character char when no number has been entered
				throw new NumberFormatException("Invalid duration amount '" + c + "'");
		}

		return total;
	}
}
