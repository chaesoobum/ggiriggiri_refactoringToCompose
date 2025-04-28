package com.friends.ggiriggiri.firebase.repository

import javax.inject.Inject

class MemoriesRepository@Inject constructor() {

    fun takeInformationForRequestsListScreen():List<List<String>>{

        val list1 = listOf<String>("채수범","content1","2025.03.04 00:15")
        val list2 = listOf<String>("채수범","content2","2025.03.04 00:15")
        val list3 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list4 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list5 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list6 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list7 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list8 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list9 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list10 = listOf<String>("채수범","content3","2025.03.04 00:15")
        val list11 = listOf<String>("채수범","content3","2025.03.04 00:15")

        return listOf(list1,list2,list3,list4,list5,list6,list7,list8,list9,list10,list11)
    }
}