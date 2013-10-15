package ru.poloniumarts.netutils;

public class NumericUtil {

	private static final long MINUTE = 60000;
	private static final long HOUR = MINUTE * 60;
	private static final long DAY = HOUR * 24;
	private static final long WEEK = DAY * 7;
	private static final long MONTH = DAY * 30;
	private static final long YEAR = DAY * 365;

	public static String getTimeElapsed(long timeStamp1, long timeStamp2) {
		long deltaTime;
		long res;
		String result = "";

		deltaTime = Math.abs(timeStamp2 - timeStamp1);

		if (deltaTime <= MINUTE) {
			result = "менее минуты";
		} else if (deltaTime > MINUTE && deltaTime <= HOUR) {
			res = deltaTime / MINUTE;
			result = res + declension(res, " минута", " минуты", " минут");
		} else if (deltaTime > HOUR && deltaTime <= DAY) {
			res = deltaTime / HOUR;
			result = res + declension(res, " час", " часа", " часов");
		} else if (deltaTime > DAY && deltaTime <= WEEK) {
			res = deltaTime / DAY;
			result = res + declension(res, " день", " дня", " дней");
		} else if (deltaTime > WEEK && deltaTime <= MONTH) {
			res = deltaTime / WEEK;
			result = res + declension(res, " неделя", " недели", " недель");
		} else if (deltaTime > MONTH && deltaTime <= YEAR) {
			res = deltaTime / MONTH;
			result = res + declension(res, " месяц", "месяца", " месяцев");
		} else if (deltaTime > YEAR) {
			res = deltaTime / YEAR;
			result = res + declension(res, " год", " года", " лет");
		}

		return result;
	}

	public static String declension(long number, String text1, String text2_4,
			String other) {

		String result = null;

		long mod10 = number % 10;
		long mod100 = number % 100;

		if (mod100 > 10 && mod100 < 20) {
			result = other;
		} else if (mod10 == 1) {
			result = text1;
		} else if (mod10 >= 2 && mod10 <= 4) {
			result = text2_4;
		} else {
			result = other;
		}
		return result;
	}

}
