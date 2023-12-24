package dev.hooon.show.infrastructure.adaptor;

import static dev.hooon.show.domain.entity.ShowCategory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowPeriod;
import dev.hooon.show.domain.entity.ShowTime;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.repository.PlaceRepository;
import dev.hooon.show.domain.repository.ShowRepository;

@DisplayName("[ShowRepositoryAdaptor 테스트]")
class ShowRepositoryAdaptorTest extends DataJpaTestSupport {

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Test
	@DisplayName("[show_id 로 해당 공연에 대한 정보를 조회할 수 있다]")
	void findByIdTest() {
		// given
		Place place = new Place(
			"블루스퀘어 신한카드홀",
			"1544-1591",
			"서울특별시 용산구 이태원로 294 블루스퀘어(한남동)",
			"http://www.bluesquare.kr/index.asp"
		);
		Place savedPlace = placeRepository.save(place);
		ShowPeriod showPeriod = new ShowPeriod(LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 12));
		ShowTime showTime = new ShowTime(150, 15);
		Show show = new Show
			("레미제라블",
				MUSICAL,
				showPeriod,
				showTime,
				"만 8세 이상",
				300,
				savedPlace
			);
		Show savedShow = showRepository.save(show);

		// when
		Optional<Show> showOptional = showRepository.findById(savedShow.getId());

		// then
		Assertions.assertThat(showOptional).isNotEmpty();
		Show showResult = showOptional.get();
		assertAll(
			() -> assertThat(showResult.getName()).isNotEmpty(),
			() -> assertThat(showResult.getShowAgeLimit()).isNotEmpty(),
			() -> assertThat(showResult.getPlace().getName()).isNotEmpty()
		);
	}
}
