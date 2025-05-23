package com.friends.ggiriggiri.screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.friends.ggiriggiri.firebase.model.RequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//데이터를 전역모델에 넣어서 돌려쓰기위한 뷰모델
//val pvm: PublicViewModel = hiltViewModel(LocalContext.current.findActivity())

@HiltViewModel
class PublicViewModel @Inject constructor() : ViewModel() {

    //그룹원 이미지 리스트전달
    private var _memberImageUrls = mutableStateOf<List<Pair<String, String>>>(emptyList())
    val memberImageUrls: State<List<Pair<String, String>>> = _memberImageUrls
    fun setMemberImageUrls(memberImageUrls:List<Pair<String, String>>){
        _memberImageUrls.value = memberImageUrls
    }
    fun deleteMemberImageUrls(){
        _memberImageUrls.value = emptyList()
    }

    //그룹코드 전달(다른네비게이션으로)
    private val _groupCode = mutableStateOf("")
    val groupCode: State<String> = _groupCode
    fun setGroupCode(groupCode:String){
        _groupCode.value = groupCode
    }
    fun deleteGroupCode(){
        _groupCode.value = ""
    }

    //요청모델 저장
    private val _requestModel = mutableStateOf<RequestModel?>(null)
    val requestModel: State<RequestModel?> = _requestModel
    fun setRequestModel(requestModel: RequestModel?){
        _requestModel.value = requestModel
    }
    fun deleteRequestModel(){
        _requestModel.value = null
    }

    // 요청자 이름
    private val _requesterName = mutableStateOf<String?>(null)
    val requesterName:State<String?> = _requesterName

    fun setRequesterName(requesterName: String){
        _requesterName.value = requesterName
    }
    fun deleteRequesterName(){
        _requesterName.value = null
    }
}