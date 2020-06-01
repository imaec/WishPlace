package com.imaec.wishplace

enum class NaverPlaceResult(val msg: String) {
    SUCCESS("성공"),
    FAIL_EMPTY_NAME("장소 이름을 입력해주세요."),
    FAIL_EMPTY_RESULT("검색 결과가 없습니다."),
    FAIL_UNKNWON("오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
}