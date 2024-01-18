package dev.hooon.booking.aop;

import static dev.hooon.booking.exception.BookingErrorCode.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import dev.hooon.common.exception.ValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class BookingConcurrencyHandlerAspect {

	private static final String KEY_PREFIX = "seat_";

	private final RedisTemplate<String, Object> redisTemplate;

	private void validateIsPreemptibleSeat(List<Long> seatIds) {
		List<String> seatIdKeys = seatIds.stream().map(seatId -> KEY_PREFIX + seatId).toList();

		List<Object> seatsData = redisTemplate.opsForValue().multiGet(seatIdKeys);
		if (seatsData != null && seatsData.stream().anyMatch(Objects::nonNull)) {
			throw new ValidationException(NOT_AVAILABLE_SEAT);
		}
	}

	private void validateIsPreemptibleSeatAndPreempt(List<Long> seatIds) {
		Map<String, Boolean> seatIdKeyMap = seatIds.stream()
			.collect(Collectors.toMap(seatId -> KEY_PREFIX + seatId, seatId -> true));

		Boolean isPreempted = redisTemplate.opsForValue().multiSetIfAbsent(seatIdKeyMap);
		if (Boolean.FALSE.equals(isPreempted)) {
			throw new ValidationException(NOT_AVAILABLE_SEAT);
		}

		seatIdKeyMap.keySet().forEach(key -> redisTemplate.expire(key, 30, TimeUnit.MINUTES));
	}

	@Transactional
	@Around("@annotation(dev.hooon.booking.aop.BookingConcurrency) && args(Long, seatIds, ..)")
	public Object handleBookingConcurrency(
		ProceedingJoinPoint joinPoint,
		List<Long> seatIds
	) throws Throwable {

		validateIsPreemptibleSeat(seatIds);
		Object bookingResponse = joinPoint.proceed();
		validateIsPreemptibleSeatAndPreempt(seatIds);

		return bookingResponse;
	}
}
