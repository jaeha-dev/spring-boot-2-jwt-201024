package com.github.devsjh.application.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 외부 생성자 호출 제한.
public final class MessageSourceUtils {

    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource messageSource) {
        MessageSourceUtils.messageSource = messageSource;
    }

    // Code 정보에 해당하는 코드를 MessageSource에서 조회한다.
    public static String getCode(String code) {
        return getMessageAndCode(code, null);
    }

    // Message 정보에 해당하는 메시지를 MessageSource에서 조회한다.
    public static String getMessage(String message) {
        return getMessageAndCode(message, null);
    }

    // Code 정보와 현재 Locale에 맞는 메시지를 MessageSource에서 조회한다.
    public static String getMessageAndCode(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}