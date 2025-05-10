package com.friends.ggiriggiri.firebase.repository

import com.friends.ggiriggiri.firebase.model.GroupModel
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class TestRepository@Inject constructor(
    val firestore: FirebaseFirestore
) {

    suspend fun db(){

        val groupModel = GroupModel()
        groupModel.groupCode = "csb"
        groupModel.groupPw = "1234"
        groupModel.groupName = "테스트그룹"

        val groupVO = groupModel.toGroupVO()

        firestore.collection("_groups")
            .add(groupVO)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }
}