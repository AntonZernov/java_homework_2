package ru.content.management.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
    public static <T> Specification<T> buildDurationSpec(LocalTime durationFrom, LocalTime durationTo, String durationField) {
        if (durationFrom != null && durationTo != null) {
            if (durationFrom.equals(durationTo)) {
                return (root, query, cb) -> cb.equal(root.get(durationField), durationFrom);
            }
            return (root, query, cb) -> cb.and(
                    cb.greaterThanOrEqualTo(root.get(durationField), durationFrom),
                    cb.lessThanOrEqualTo(root.get(durationField), durationTo)
            );
        }
        if (durationFrom != null) {
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(durationField), durationFrom);
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(durationField), durationTo);
    }
}
