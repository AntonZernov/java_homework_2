package ru.content.management.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalTime;

@UtilityClass
public class SpecificationUtils {

    /**
     * Создаёт спецификацию для поиска по LIKE с игнорированием регистра.
     *
     * @param value значение для поиска
     * @param field имя поля сущности
     */
    public static <T> Specification<T> buildLikeSpec(String value, String field) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    /**
     * Создаёт спецификацию для фильтрации по времени (duration).
     * Если оба параметра заданы и равны — точное совпадение.
     * Если оба заданы и различны — диапазон включительно.
     * Если задан один — больше или равно начальному времени, меньше или равно конечному времени.
     *
     * @param durationFrom начальное значение времени
     * @param durationTo конечное значение времени
     * @param durationField имя поля сущности
     */
    public static <T> Specification<T> buildDurationSpec(String durationFrom, String durationTo, String durationField) {
        if (StringUtils.hasText(durationFrom) && StringUtils.hasText(durationTo)) {
            if (durationFrom.equals(durationTo)) {
                LocalTime time = LocalTime.parse(durationFrom);
                return (root, query, cb) -> cb.equal(root.get(durationField), time);
            }
            LocalTime from = LocalTime.parse(durationFrom);
            LocalTime to = LocalTime.parse(durationTo);
            return (root, query, cb) -> cb.and(
                    cb.greaterThanOrEqualTo(root.get(durationField), from),
                    cb.lessThanOrEqualTo(root.get(durationField), to)
            );
        }
        if (StringUtils.hasText(durationFrom)) {
            LocalTime from = LocalTime.parse(durationFrom);
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(durationField), from);
        }
        LocalTime to = LocalTime.parse(durationTo);
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(durationField), to);
    }
}
