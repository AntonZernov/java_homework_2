package ru.content.management.controller;

public final class TestConstants {

    private TestConstants() {}

    public static final String VALIDATION_ERROR_MESSAGE = "Проверьте наличие всех обязательных полей и их корректность.";
    public static final String NOT_FOUND_ERROR_MESSAGE = "%s не найдена";
    public static final String NOT_FOUND_EDIT_MESSAGE = "%s для редактирования не найдена";
    public static final String NOT_FOUND_DELETE_MESSAGE = "%s для удаления не найдена";

    public static final String ARTICLE_NOT_FOUND = String.format(NOT_FOUND_ERROR_MESSAGE, "Статья");
    public static final String ARTICLE_EDIT_NOT_FOUND = String.format(NOT_FOUND_EDIT_MESSAGE, "Статья");
    public static final String ARTICLE_DELETE_NOT_FOUND = String.format(NOT_FOUND_DELETE_MESSAGE, "Статья");

    public static final String VIDEO_NOT_FOUND = String.format(NOT_FOUND_ERROR_MESSAGE, "Видео");
    public static final String VIDEO_EDIT_NOT_FOUND = String.format(NOT_FOUND_EDIT_MESSAGE, "Видео");
    public static final String VIDEO_DELETE_NOT_FOUND = String.format(NOT_FOUND_DELETE_MESSAGE, "Видео");

    public static final String PODCAST_NOT_FOUND = String.format(NOT_FOUND_ERROR_MESSAGE, "Подкаст");
    public static final String PODCAST_EDIT_NOT_FOUND = String.format(NOT_FOUND_EDIT_MESSAGE, "Подкаст");
    public static final String PODCAST_DELETE_NOT_FOUND = String.format(NOT_FOUND_DELETE_MESSAGE, "Подкаст");
}
