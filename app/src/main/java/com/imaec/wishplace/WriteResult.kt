package com.imaec.wishplace

enum class WriteResult(val msg: String) {
    SUCCESS("성공"),
    FAIL_CATEGORY("카테고리를 선택 해주세요."),
    FAIL_TITLE("이름을 입력 해주세요."),
    FAIL_ADDRESS("주소를 입력 해주세요.")
}