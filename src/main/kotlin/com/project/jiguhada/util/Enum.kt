package com.project.jiguhada.util

enum class SocialType {
    GENERAL, KAKAO
}

enum class ROLE {
    ROLE_USER, ROLE_ADMIN
}

enum class BOARD_CATEGORY{
    ENVIRONMENT,
    FREE,
    VEGAN,
    QUESTION
}

enum class BOARD_ORDER_TYPE {
    RECENT,
    POPULAR,
    VIEW,
    COMMENT_COUNT
}

enum class ERRORCODE {
    ID_PASSWORD_NOTMATCH,
    NOT_EXITS_ID,
    INSUFFICIENT_REQUEST,
    DUPLICATE_NICKNAME,
    DUPLICATE_ID,
    NOW_PASSWORD_NOTMATCH,
    EXPIRE_ACCESS_TOKEN,
    UNAUTHORIZED_REQUEST,
    NOT_SUPPORTED_TOKEN,
    FAIL_TO_UPLOAD_IMG,
    REQUEST_NOT_INCLUDE_TOKEN,
    INCORRECT_JWT_SIGNATURE,
    MALFORMED_JWT_SIGNATURE,
    REQUEST_BOARDID_NOT_MATCHED,
    KEY_NOT_EXIST,
    LIMIT_FILE_COUNT,
    INVALID_FORMAT_REQUEST,
    USER_ALREADY_LIKE
}

enum class BOARD_SEARCH_TYPE {
    WRITER,
    CONTENT,
    TITLE
}

enum class CHALLENGE_PERIOD {
    ONEWEEK,
    TWOWEEK,
    THREEWEEK,
    FOURWEEK
}

enum class AUTH_FREQUENCY {
    EVERYDAY,
    WEEKDAY,
    WEEKEND,
    ONCEAWEEK,
    TWICEAWEEK,
    THIRDAWEEK,
    FORTHAWEEK,
    FIFTHAWEEK,
    SIXTHAWEEK
}

enum class AUTH_AVAILABLE_TIME_TYPE {
    ALLDAY,
    CUSTOMTIME
}

enum class CHALLENGE_TAG {
    ZERO_WASTE,
    VEGAN,
    LIFESTYLE,
    TUMBLER,
    RECYCLING,
    ENVIRONMENT_DAY,
    ETC
}

