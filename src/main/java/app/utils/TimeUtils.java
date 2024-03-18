package app.utils;

import java.time.LocalDateTime;

public class TimeUtils {

	public static LocalDateTime now() {
		LocalDateTime datetime = LocalDateTime.now();
		return roundToSecond(datetime);
	}
	
	public static LocalDateTime roundToSecond(LocalDateTime datetime) {
        return datetime.minusNanos(datetime.getNano());
    }
	
}
