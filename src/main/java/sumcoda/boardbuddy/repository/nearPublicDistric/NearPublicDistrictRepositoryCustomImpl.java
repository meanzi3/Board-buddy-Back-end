package sumcoda.boardbuddy.repository.nearPublicDistric;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.NearPublicDistrictResponse;

import java.util.List;

import static sumcoda.boardbuddy.entity.QNearPublicDistrict.nearPublicDistrict;

@RequiredArgsConstructor
public class NearPublicDistrictRepositoryCustomImpl implements NearPublicDistrictRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NearPublicDistrictResponse.InfoDTO> findInfoDTOsByPublicDistrictId(Long publicDistrictId) {
        return jpaQueryFactory
                .select(Projections.fields(NearPublicDistrictResponse.InfoDTO.class,
                        nearPublicDistrict.sido,
                        nearPublicDistrict.sgg,
                        nearPublicDistrict.emd,
                        nearPublicDistrict.radius))
                .from(nearPublicDistrict)
                .where(nearPublicDistrict.publicDistrict.id.eq(publicDistrictId))
                .fetch();
    }

    @Override
    public List<NearPublicDistrictResponse.LocationDTO> findLocationDTOsByPublicDistrictIdAndRadius(Long publicDistrictId, Integer radius) {
        return jpaQueryFactory
                .select(Projections.fields(NearPublicDistrictResponse.LocationDTO.class,
                        nearPublicDistrict.sido,
                        nearPublicDistrict.sgg,
                        nearPublicDistrict.emd))
                .from(nearPublicDistrict)
                .where(nearPublicDistrict.publicDistrict.id.eq(publicDistrictId)
                        .and(nearPublicDistrict.radius.eq(radius)))
                .fetch();
    }
}
