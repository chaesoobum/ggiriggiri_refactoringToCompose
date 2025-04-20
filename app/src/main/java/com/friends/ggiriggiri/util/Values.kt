package com.friends.ggiriggiri.util

import com.friends.ggiriggiri.R

enum class MainScreenName{
    //로그인 화면
    SCREEN_USER_LOGIN,
    //메인 화면
    SCREEN_USER_MAIN,
    //그룹화면
    SCREEN_USER_GROUP,
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
    object Requests : Memories("Requests","요청들",R.drawable.exclamation_24px)
    object Answers : Memories("Answers","답변들",R.drawable.question_mark_24px)
}