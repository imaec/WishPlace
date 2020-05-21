package com.imaec.wishplace

enum class CategoryUpdateResult(val msg: String) {
    SUCCESS("수정하였습니다!"),
    FAIL_EXIST("같은 이름의 카테고리가 있습니다."),
    FAIL("카테고리 수정에 실패했습니다.")
}