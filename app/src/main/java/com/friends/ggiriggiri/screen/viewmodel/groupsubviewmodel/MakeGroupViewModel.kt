package com.friends.ggiriggiri.screen.viewmodel.groupsubviewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.GroupModel
import com.friends.ggiriggiri.firebase.service.GroupService
import com.friends.ggiriggiri.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeGroupViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val groupService: GroupService
) : ViewModel() {
    val friendsApplication = context as FriendsApplication
    // 그룹이름 입력 요소
    val textFieldMakeGroupNameValue = mutableStateOf("")

    // 마지막 그룹명의 유효화 여부
    private val lastCheckedGroupNameValid = mutableStateOf(false)

    // 그룹이름 1자이상 에러메세지와 에러상태
    val groupNameLengthError = mutableStateOf("")
    val isGroupNameLengthError = mutableStateOf(false)

    // 시용자가 그룹명을 터치하기전까지 유효성체크 미루기
    val isGroupNameTouched = mutableStateOf(false)

    // 그룹코드 입력 요소
    val textFieldMakeGroupCodeValue = mutableStateOf("")

    // 그룹코드 중복 체크 결과
    val textMakeGroupCodeDuplicateResult = mutableStateOf("")

    // 그룹코드 중복 체크 결과 색상
    val textMakeGroupCodeDuplicateResultColor = mutableStateOf(Color.Unspecified)

    // 그룹코드 4자이상 에러메세지와 에러상태
    val groupCodeLengthError = mutableStateOf("")
    val isGroupCodeLengthError = mutableStateOf(false)

    // 마지막 중복확인 시의 그룹코드 저장
    private val lastCheckedGroupCode = mutableStateOf("")

    // 시용자가 그룹코드를 터치하기전까지 유효성체크 미루기
    val isGroupCodeTouched = mutableStateOf(false)

    // 그룹비밀번호1 입력 요소
    val textFieldMakeGroupPassword1Value = mutableStateOf("")

    // 그룹비밀번호2 입력 요소
    val textFieldMakeGroupPassword2Value = mutableStateOf("")

    // 마지막 비밀번호 일치확인시의 비밀번호유효화 여부
    private val lastCheckedGroupPasswordValid = mutableStateOf(false)

    // 비밀번호 에러 메시지와 에러 상태
    val passwordMismatchError = mutableStateOf("")
    val isPasswordError = mutableStateOf(false)

    // 프로그래스바
    val isLoading = MutableStateFlow(false) // 로딩 상태

    // 그룹명 1자이상인지 검사
    fun checkGroupNameLength() {
        if (textFieldMakeGroupNameValue.value.length < 2) {
            groupNameLengthError.value = "그룹명은 2자이상 입력해주세요"
            isGroupNameLengthError.value = true
            lastCheckedGroupNameValid.value = false
        } else {
            groupNameLengthError.value = ""
            isGroupNameLengthError.value = false
            lastCheckedGroupNameValid.value = true
        }
    }

    // 그룹코드 중복여부 확인
    fun checkGroupCodeDuplicate() {
        if (textFieldMakeGroupCodeValue.value.length > 3) {
            viewModelScope.launch {
                isLoading.value = true

                val currentCode = textFieldMakeGroupCodeValue.value

                val result = groupService.checkDuplicationGroupCode(currentCode)

                if (!result) {
                    textMakeGroupCodeDuplicateResult.value = "❌ 중복된 그룹코드가 있습니다"
                    textMakeGroupCodeDuplicateResultColor.value = Color(0xFFD50000)
                } else {
                    textMakeGroupCodeDuplicateResult.value = "✅ 중복 확인 완료"
                    textMakeGroupCodeDuplicateResultColor.value = Color(0xFF64DD17)

                    // 마지막 중복 확인된 값 저장
                    lastCheckedGroupCode.value = currentCode
                }

                isLoading.value = false
            }
        }
    }

    // 그룹코드 4자 이상인지 확인
    fun checkGroupCodeLength() {
        if (textFieldMakeGroupCodeValue.value.length < 4) {
            groupCodeLengthError.value = "그룹 코드는 4자이상 입력해주세요"
            isGroupCodeLengthError.value = true
        } else {
            groupCodeLengthError.value = ""
            isGroupCodeLengthError.value = false
        }
    }

    // 그룹코드가 변경되었는지 확인 (중복확인 이후에 바뀐 경우)
    fun checkGroupCodeChange() {
        val currentCode = textFieldMakeGroupCodeValue.value
        if (lastCheckedGroupCode.value != "" && currentCode != lastCheckedGroupCode.value) {
            textMakeGroupCodeDuplicateResult.value = "⚠\uFE0F 다시 중복확인을 해주세요."
            textMakeGroupCodeDuplicateResultColor.value = Color(0xFFD50000)
            lastCheckedGroupCode.value = "" // 무효화
        }
    }

    // 비밀번호 동일한지 검사
    fun checkPasswordMatch() {
        if (textFieldMakeGroupPassword1Value.value != textFieldMakeGroupPassword2Value.value) {
            passwordMismatchError.value = "비밀번호가 일치하지 않습니다"
            isPasswordError.value = true
            lastCheckedGroupPasswordValid.value = false
        } else {
            passwordMismatchError.value = ""
            isPasswordError.value = false
            lastCheckedGroupPasswordValid.value = true
        }
    }

    //최종적으로 확인해야할것
    //lastCheckedGroupPasswordValid
    //lastCheckedGroupCode
    //lastCheckedGroupNameValid

    fun isFormValid(): Boolean {
        return lastCheckedGroupPasswordValid.value &&
                lastCheckedGroupCode.value.isNotEmpty() &&
                lastCheckedGroupNameValid.value
    }

    val showSuccessDialog = mutableStateOf(false)
    val showFailDialog = mutableStateOf(false)
    fun makeGroup() {
        if (isFormValid()) {
            showSuccessDialog.value = true
        }else{
            showFailDialog.value=true
        }
    }

    // db 저장 & 프로그래스바 띄우기 작업
    fun makeGroupProcessLoading(onFinish: () -> Unit){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val groupModel = GroupModel(
                    groupName = textFieldMakeGroupNameValue.value,
                    groupCode = textFieldMakeGroupCodeValue.value,
                    groupPw = textFieldMakeGroupPassword1Value.value,
                    groupUserDocumentID = mutableListOf(friendsApplication.loginUserModel.userDocumentId)
                )
                val groupModelFromDB = groupService.makeGroup(groupModel = groupModel)

                groupService.updateUserGroupDocumentId(
                    userDocumentId = friendsApplication.loginUserModel.userDocumentId,
                    groupDocumentId = groupModelFromDB.groupDocumentId
                )

                friendsApplication.loginUserModel.userGroupDocumentID = groupModelFromDB.groupDocumentId

                // 저장 성공
                isLoading.value = false
                onFinish()
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading.value = false
                showFailDialog.value = true
            }
        }

    }

}