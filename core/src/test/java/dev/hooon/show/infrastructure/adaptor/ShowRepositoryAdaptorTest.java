package dev.hooon.show.infrastructure.adaptor;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.domain.entity.Show;
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
		Place place = TestFixture.getPlace();
		Place savedPlace = placeRepository.save(place);

		Show show = TestFixture.getShow(savedPlace);
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
