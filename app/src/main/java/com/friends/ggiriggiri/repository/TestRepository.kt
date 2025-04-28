package com.friends.ggiriggiri.repository

import com.friends.ggiriggiri.dataclass.model.GroupModel
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class TestRepository@Inject constructor() {

    suspend fun db(){
        val db = FirebaseFirestore.getInstance()

        val groupModel = GroupModel()
        groupModel.groupCode = "csb"
        groupModel.groupPw = "1234"
        groupModel.groupName = "테스트그룹"

        val groupVO = groupModel.toGroupVO()

        db.collection("_groups")
            .add(groupVO)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }
}