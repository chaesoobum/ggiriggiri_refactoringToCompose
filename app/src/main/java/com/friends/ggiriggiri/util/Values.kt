package com.friends.ggiriggiri.util

import com.friends.ggiriggiri.R

enum class MainScreenName{
    //로그인 화면
    SCREEN_USER_LOGIN,
    //메인 화면
    SCREEN_USER_MAIN,
    //그룹화면
    SCREEN_USER_GROUP,
    //요청 보는화면
    SCREEN_VIEW_ONE_REQUEST,
    //답변 보는화면
    SCREEN_VIEW_ONE_QUESTION,
    //요청하는화면
    SCREEN_DO_REQUEST,
    //질문에 답하는 화면
    SCREEN_DO_ANSWER,
    //알림창
    SCREEN_NOTIFICATION,
    //그룹설정화면
    SCREEN_SETTING_GROUP,
    //개인정보처리방침/이용약관화면
    SCREEN_LEGAL,
    //그룹원보기화면
    SCREEN_MEMBER_LIST_DETAIL,
}

sealed class Screen(val route: String, val label: String, val icon: Int) {
    object Home : Screen("home", "홈", R.drawable.ic_home)
    object Memories : Screen("memories", "추억들", R.drawable.ic_photo_library)
    object MyPage : Screen("mypage", "마이 페이지", R.drawable.ic_person)
}

sealed class Group(val route: String,val label: String,val icon: Int){
    object MakeGroup : Group("MakeGroup","그룹 만들기",R.drawable.group_add_24px)
    object JoinGroup : Group("JoinGroup","그룹 들어가기",R.drawable.group_24px)
}

sealed class Memories(val route: String,val label: String,val icon: Int){
    object Requests : Memories("Requests","요청",R.drawable.exclamation_24px)
    object Answers : Memories("Answers","답변",R.drawable.question_mark_24px)
}
//유저 상태값
enum class UserState(val num: Int, val str:String){
    NORMAL(1,"정상"),
    WITHDRAW(2,"탈퇴")
}

//유저 소셜 로그인
enum class UserSocialLoginState(val num: Int, val str:String){
    NOTHING(1,"없음"),
    KAKAO(2,"카카오"),
    NAVER(3,"네이버"),
    GOOGLE(4,"구글"),
}

enum class RequestState(val value: Int) {
    ACTIVE(1),
    INACTIVE(2);

    companion object {
        fun fromValue(value: Int?): RequestState {
            return RequestState.entries.find { it.value == value } ?: ACTIVE
        }
    }
}

// 그룹 상태값
enum class GroupState(val num: Int, val str:String){
    ACTIVE(1,"활성화"),
    INACTIVE(2,"비활성화")
}