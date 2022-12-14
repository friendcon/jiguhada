package com.project.jiguhada.util

enum class SocialType {
    GENERAL, KAKAO
}

enum class ROLE {
    ROLE_USER, ROLE_ADMIN
}

enum class IS_USER_INFO_PUBLIC {
    PUBLIC,
    PRIVATE
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
    USER_ALREADY_LIKE,
    USER_ALREADY_CHALLENGE_MEMBER,
    CHALLENGE_JOIN_COUNT,
    CHALLENGE_CLOSE,
    CANT_AUTH_CHALLENGE,
    ALREADY_AUTH_CHALLENGE,
    USER_INFO_PRIVATE,
    OVER_REQUEST,
    BAD_REQUEST
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
    ZERO_WASTE, // 제로웨이스트
    ZEROENERGE, // 제로에너지
    PLOGGING, // 쓰레기 줍기
    TUMBLER, // 텀블러 들고다니기
    RECYCLING, // 재활용
    VEGAN, // 비건
    VEGANRECIPE, // 비건 레시피
    VEGANBEAUTY, // 비건 화장품
    VEGANFASHION, // 비건 패션
    PESCOVEGAN, // 페스코 비건 (해산물, 유제품 허용하는 비건)
    FLEXITERIANVEGAN, // 육식을 가끔하는 비건 (챌린지 기간동안 비건)
    ETC, // 기타
    LIFESTYLE, // 생활습관
    ENVIRONMENT_DAY, // 환경의 날
    EARTH_DAY, // 지구의 날
    PLANT_DAY, // 식목일
    WATER_DAY, // 물의 날
    SEA_DAY, // 바다의 날
    BUY_NOTHING_DAY, // 아무것도 사지 않는 날
    VEGAN_DAY, // 비건의 날
    ENERGE_DAY // 에너지의 날
}

enum class CHALLENGE_CATEGORY {
    VEGAN,
    ENVIRONMENT,
    ETC
}

enum class CHALLENGE_STATUS {
    BEFORE,
    INPROGRESS,
    END
}

enum class CHALLENGE_IS_SUCCESS {
    SUCCESS,
    INPROGRESS
}

enum class CHALLENGE_ORDER_TYPE {
    RECENTLY,
    POPULAR
}

enum class CHALLENGE_SEARCH_TYPE {
    TITLE,
    CONTENT,
    TAG
}

enum class CHALLENGE_AUTH_STATUS {
    SUCCESS,
    FAIL,
    WAIT
}

enum class CHALLENGE_AUTH_IS_APPROVE {
    APPROVE,
    WAIT,
    REFUSE
}

enum class CHALLENTE_IS_JOIN {
    JOIN,
    NOTJOIN
}

