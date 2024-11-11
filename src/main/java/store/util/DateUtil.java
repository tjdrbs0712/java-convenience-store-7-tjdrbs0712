package store.util;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import store.domain.store.Promotion;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 프로모션 기간을 계산해서 프로모션 기간이라면 참 반환
     *
     * @param promotion 프로모션
     * @return 프로모션 여부
     */
    public static boolean isPromotionActive(Promotion promotion) {
        LocalDate currentDate = DateTimes.now().toLocalDate();

        LocalDate startDate = LocalDate.parse(promotion.getStartDate(), DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(promotion.getEndDate(), DATE_FORMATTER);

        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }


}
