package dev.hooon.show.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.hooon.common.dto.PagedResponse;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.ShowMapper;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import dev.hooon.show.dto.response.ShowDetailsInfoResponse;
import dev.hooon.show.dto.response.ShowInfoResponse;
import dev.hooon.show.exception.ShowErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {

	private final SeatRepository seatRepository;

	private final ShowRepository showRepository;

	public AbleBookingDateRoundResponse getAbleBookingDateRoundInfo(Long showId) {
		List<SeatDateRoundDto> seatDateRoundDtoList = seatRepository.findSeatDateRoundInfoByShowId(showId);
		return ShowMapper.toSeatDateRoundResponse(seatDateRoundDtoList);
	}

	public ShowDetailsInfoResponse getShowDetailInfo(Long showId) {
		Show show = showRepository.findById(showId).orElseThrow(
			() -> new NotFoundException(ShowErrorCode.SHOW_NOT_FOUND)
		);
		return ShowMapper.toShowDetailInfoResponse(show);
	}

	public PagedResponse<ShowInfoResponse> getShowsByCategory(int page, int size, String category) {
		ShowCategory showCategory = ShowCategory.of(category);
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Show> showsPage = showRepository.findByCategoryOrderByIdDesc(showCategory, pageRequest);
		List<ShowInfoResponse> content = showsPage.map(ShowMapper::toShowInfoResponse).getContent();

		return new PagedResponse<>(
			content,
			showsPage.getTotalPages(),
			showsPage.getNumberOfElements(),
			showsPage.getNumber(),
			showsPage.getSize()
		);
	}
}
