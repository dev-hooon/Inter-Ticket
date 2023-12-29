package dev.hooon.waitingbooking.application.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import dev.hooon.show.application.SeatService;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.application.WaitingBookingService;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;
import dev.hooon.waitingbooking.dto.response.WaitingRegisterResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingBookingFacade {

	private final WaitingBookingService waitingBookingService;
	private final UserService userService;
	private final SeatService seatService;

	// 선택한 좌석중에서 취소좌석에 포함되는 좌석 ID 를 LIST 로 응답
	private List<Long> fetchMatchingSeatIds(
		Set<Long> canceledSeatIds,
		List<Long> selectedSeatIds,
		int seatCount
	) {
		List<Long> matchSeatIds = new ArrayList<>();
		for (Long selectedSeatId : selectedSeatIds) {
			if (canceledSeatIds.contains(selectedSeatId)) {
				matchSeatIds.add(selectedSeatId);
			}
			if (matchSeatIds.size() == seatCount) {
				break;
			}
		}
		return matchSeatIds;
	}

	public WaitingRegisterResponse registerWaitingBooking(Long userId, WaitingRegisterRequest request) {
		User user = userService.getUserById(userId);
		WaitingBooking waitingBooking = waitingBookingService.createWaitingBooking(user, request);

		return new WaitingRegisterResponse(waitingBooking.getId());
	}

	public void processWaitingBooking() {
		// 1. 취소된 좌석을 모두 조회한다 (PK Set 으로)
		Set<Long> canceledSeatIds = seatService.getCanceledSeatIds();
		// 2. 대기중 상태인 예약대기 목록을 날짜순으로 조회한다
		List<WaitingBooking> waitingList = waitingBookingService.getWaitingBookingsByStatusIsWaiting();
		// 3. waitingList 반복하면서 아래 작업을 수행
		/*
		 * (1) 대기목록에 포함된 좌석의 PK 가 취소된 좌석 Set 에 존재하는지 확인
		 * (2) 대기목록의 좌석중에서 취소목록에 포함되는 좌석 아이디 가져옴
		 * (3) 가져온 좌석 아이디 사이즈가 선택좌석개수와 같으면 취소좌석 SET 에서 해당 PK 지우고 좌석 상태를 취소에서 대기로 바꾸고 예약대기를 활성화
		 *  + 사용자에게 메일 발송 (구현 예정)
		 */
		waitingList.forEach(waitingBooking -> {
			// (1)
			List<Long> selectedSeatIds = waitingBooking.getSelectedSeatIds();
			// (2)
			List<Long> matchSeatIds = fetchMatchingSeatIds(
				canceledSeatIds,
				selectedSeatIds,
				waitingBooking.getSeatCount()
			);
			// (3)
			if (matchSeatIds.size() == waitingBooking.getSeatCount()) {
				matchSeatIds.forEach(canceledSeatIds::remove);
				seatService.updateSeatToWaiting(matchSeatIds);
				waitingBookingService.activateWaitingBooking(waitingBooking.getId());
				// 메일 알림 이벤트 발행
			}
		});
		// 4. 반복이 끝났는데 남아있는 취소 좌석들은 예약가능 상태로 변경
		seatService.updateSeatToAvailable(canceledSeatIds);
	}
}
