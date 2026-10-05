<template>
  <div class="member-edit-page">
    <!-- 페이지 제목 -->
    <h2>회원 정보 수정 페이지</h2>
    <hr />
    <br />

    <!-- [수정] 기존에 있던 "비밀번호 먼저 입력해야 진입 가능한 전체 화면 잠금(게이트)"는 제거
         회원정보를 불러오는 동안에는 안내 문구만 보여줌. -->
    <p v-if="!memberLoaded">회원 정보를 불러오는 중입니다...</p>

    <div v-else class="info-list">
      <!-- 아이디 (수정 불가, 전체 노출) -->
      <p class="static-row">
        <strong>아이디</strong><span>{{ member.id }}</span>
      </p>

      <!-- 이름 (수정 불가, 전체 노출) -->
      <p class="static-row">
        <strong>이름</strong><span>{{ member.name }}</span>
      </p>

      <!-- ==================== 전화번호 영역 ==================== -->
      <!-- [수정] 버튼으로 편집모드를 토글하던 방식 제거. 기존 값이 항상 입력창에 채워진 채로 노출되고
           바로 수정 가능하도록 변경. 실제 서버 반영은 화면 최하단의 '수정완료' 버튼을 눌러야 일괄 처리됨. -->
      <div class="edit-row">
        <p><strong>전화번호</strong></p>
        <div class="edit-box">
          <div class="phone-inputs">
            <span class="phone-fixed">010</span>
            <span class="phone-dash">-</span>
            <!-- 가운데 4자리 입력칸: 숫자만 입력 가능, 4자리 채우면 다음 칸으로 자동 이동 -->
            <input
              ref="phoneMiddleInput"
              v-model="phoneMiddle"
              maxlength="4"
              class="phone-input"
              @compositionend="onPhoneCompositionEnd"
              @input="onPhoneMiddleInput"
            />
            <span class="phone-dash">-</span>
            <!-- 뒷 4자리 입력칸: 숫자만 입력 가능 -->
            <input
              ref="phoneLastInput"
              v-model="phoneLast"
              maxlength="4"
              class="phone-input"
              @compositionend="onPhoneCompositionEnd"
              @input="onPhoneLastInput"
            />
          </div>
          <!-- [수정] 필드별 완료 버튼 삭제. 에러 문구는 최하단 '수정완료' 클릭 시점에 검증해서 노출 -->
          <p v-if="phoneErrorMsg" class="msg-red">{{ phoneErrorMsg }}</p>
        </div>
      </div>

      <br />

      <!-- ==================== 이메일 영역 ==================== -->
      <div class="edit-row">
        <p><strong>이메일(선택)</strong></p>
        <div class="edit-box">
          <div class="email-inputs">
            <!-- 이메일 아이디 부분 입력창 (기존 값으로 초기화되어 노출) -->
            <input
              v-model="emailId"
              maxlength="30"
              class="email-id-input"
              @input="onEmailIdInput"
              placeholder="아이디를 입력해주세요."
            />
            <span class="email-at">@</span>

            <!-- 도메인 선택 드롭다운 -->
            <select v-model="emailDomainSelect" class="email-domain-select">
              <option v-for="(opt, idx) in emailDomainOptions" :key="idx" :value="opt">
                {{ opt }}
              </option>
            </select>

            <!-- 도메인 직접 입력창: 드롭다운에서 '직접입력'을 선택했을 때만 노출 -->
            <input
              v-if="emailDomainSelect === '직접입력'"
              v-model="emailDomainCustom"
              maxlength="30"
              class="email-domain-input"
              @input="onEmailDomainCustomInput"
              placeholder="도메인 입력"
            />
          </div>

          <!-- [수정] 필드별 완료 버튼 삭제, 에러 문구는 최하단 '수정완료' 클릭 시점에 노출 -->
          <p v-if="emailErrorMsg" class="msg-red">{{ emailErrorMsg }}</p>
        </div>
      </div>

      <br />

      <!-- ==================== 비밀번호 영역 ==================== -->
      <!-- [수정] '변경' 클릭 시 기존 비밀번호 입력창 노출 -> /login으로 일치 확인 후
     통과해야 새 비밀번호 입력창 활성화. (전용 확인 API가 없어 로그인 API로 검증) -->
      <div class="edit-row">
        <p><strong>비밀번호</strong></p>
        <div class="pw-summary">
          <span class="pw-masked">********</span>
          <button v-if="!editingPw" @click="startEditingPw">변경</button>
          <button v-else @click="cancelEditingPw">취소</button>
        </div>
      </div>

      <div v-if="editingPw" class="edit-row">
      <p><strong>비밀번호 변경</strong></p>
        <div v-if="editingPw" class="edit-box pw-edit-box">
          <!-- [추가] 1단계: 기존 비밀번호 확인 -->
          <template v-if="!pwVerified">
            <input
              v-model="currentPwInput"
              type="password"
              placeholder="현재 비밀번호 입력"
              @keyup.enter="verifyCurrentPw"
            />
            &nbsp;
            <button @click="verifyCurrentPw" :disabled="pwVerifying">확인</button>
            <p v-if="currentPwErrorMsg1" class="msg-red">{{ currentPwErrorMsg1 }}<br/>{{ currentPwErrorMsg2 }}</p>
          </template>

          <!-- [추가] 2단계: 기존 비밀번호가 확인된 경우에만 새 비밀번호 입력 필드 노출 -->
          <template v-else>
            <p class="pw-guide">
              비밀번호는 7글자 이상,<br/>대문자/소문자/숫자를<br />각각 1개 이상 포함해야 하며,<br />
              영문과 숫자만 입력 가능합니다.
            </p>

            <!-- 새 비밀번호 입력창: showNewPw로 표시/숨김 토글 -->
            <!-- 새 비밀번호 입력창 2개, 재확인 입력창 2개 모두 maxlength="20" 추가 -->
            <input
              v-if="!showNewPw"
              v-model="newPw"
              type="password"
              maxlength="20"
              placeholder="변경할 비밀번호 입력"
              @input="onNewPwInput"
            />
            <input
              v-else
              v-model="newPw"
              type="text"
              maxlength="20"
              placeholder="변경할 비밀번호 입력"
              @input="onNewPwInput"
            />
            <button v-if="!showNewPw" @click="showNewPw = true">표시</button>
            <button v-else @click="showNewPw = false">숨김</button>

            <!-- 새 비밀번호 조건 미충족 안내문구 -->
            <p v-if="newPwConditionMsg" class="msg-red">{{ newPwConditionMsg }}</p>
            <!-- [수정] 기존에는 member.pw(평문)와 비교했으나, 비밀번호가 암호화되어 저장되므로
                  방금 검증에 성공한 currentPwInput(현재 비밀번호 평문)과 비교하도록 변경 -->
            <p v-if="newPwSameAsOldMsg" class="msg-red">{{ newPwSameAsOldMsg }}</p>

            <br />

            <!-- 새 비밀번호 재확인 입력창 -->
            <input
              v-if="!showNewPwConfirm"
              v-model="newPwConfirm"
              type="password"
              maxlength="20"
              placeholder="변경할 비밀번호 재입력"
              @input="onNewPwConfirmInput"
            />
            <input
              v-else
              v-model="newPwConfirm"
              type="text"
              maxlength="20"
              placeholder="변경할 비밀번호 재입력"
              @input="onNewPwConfirmInput"
            />
            <button v-if="!showNewPwConfirm" @click="showNewPwConfirm = true">표시</button>
            <button v-else @click="showNewPwConfirm = false">숨김</button>

            <!-- 재확인 실시간 안내문구 -->
            <p
              v-if="newPwConfirmMsg"
              :class="{ 'msg-blue': newPwConfirmOk, 'msg-red': !newPwConfirmOk }"
            >
              {{ newPwConfirmMsg }}
            </p>
            <!-- [수정] 필드 자체의 '완료' 버튼은 삭제. 실제 저장은 화면 최하단 '수정완료' 버튼에서 한 번에 처리 -->
          </template>
        </div>
      </div>

      <br />

      <!-- ==================== 차종 영역 ==================== -->
      <!-- [수정] 체크박스 + 변경/완료/취소 토글 방식에서, 라디오 버튼으로 값을 바로 선택하는 방식으로 변경 -->
      <div class="edit-row">
        <p><strong>차종</strong></p>
        <div class="edit-box">
          <!-- [추가] 라디오 버튼만으론 현재 차종이 뭔지 바로 안 보여서, 텍스트로 먼저 표시 -->
          <p class="car-type-current">현재 {{ getCarTypeText(member.carType) }}</p>
          <div class="car-type-radios">
            <label>
              <input type="radio" value="0" v-model.number="carTypeSelected" />
              일반차
            </label>
            <label style="margin-left: 12px">
              <input type="radio" value="1" v-model.number="carTypeSelected" />
              전기차
            </label>
          </div>
        </div>
      </div>

      <br />

      <!-- ==================== 차량번호 영역 ==================== -->
      <!-- [수정] 변경/취소 토글 없이 기존 차량번호가 채워진 입력창을 바로 노출 -->
      <div class="edit-row">
        <p><strong>차량번호</strong></p>
        <div class="edit-box">
          <input
            v-model="carNumInput"
            maxlength="8"
            @input="onCarNumInput"
            placeholder="전체 차량번호 입력"
          />
          <p v-if="carNumErrorMsg" class="msg-red">{{ carNumErrorMsg }}</p>
        </div>
      </div>
    </div>

    <br />
    <hr />
    <br />

    <!-- ==================== [추가] 하단 취소 / 수정완료 버튼 ==================== -->
    <div v-if="memberLoaded" class="bottom-actions">
      <button class="btn-cancel" @click="onClickCancel">취소</button>
      <button class="btn-complete" @click="onClickComplete">수정완료</button>
    </div>

    <!-- ==================== [추가] 수정완료 모달 ==================== -->
    <div v-if="completeModal.show" class="modal-overlay">
      <div class="modal-box">
        <!-- X 버튼: 모달만 닫고 수정 화면에 계속 머무름 (이미 저장은 완료된 상태) -->
        <button class="modal-close" @click="closeCompleteModal(false)">X</button>
        <p>회원정보 수정이 완료되었습니다.</p>
        <!-- 확인 버튼: 마이페이지로 이동 -->
        <button class="modal-confirm" @click="closeCompleteModal(true)">확인</button>
      </div>
    </div>

    <!-- ==================== [추가] 수정취소 모달 ==================== -->
    <div v-if="cancelModal.show" class="modal-overlay">
      <div class="modal-box">
        <!-- X 버튼: 모달만 닫고, 입력하던 내용을 그대로 유지한 채 수정 화면에 남음 -->
        <button class="modal-close" @click="closeCancelModal(false)">X</button>
        <p>회원정보 수정이 취소되었습니다.</p>
        <!-- 확인 버튼: 변경사항을 버리고 마이페이지로 이동 -->
        <button class="modal-confirm" @click="closeCancelModal(true)">확인</button>
      </div>
    </div>

    <!-- ==================== 공통: 페이지 이탈(뒤로가기 등) 경고 모달 ==================== -->
    <!-- 취소/완료 버튼을 누르지 않고 다른 페이지로 이동하려 할 때만 노출되는 별도 모달 -->
    <div v-if="showLeaveModal" class="modal-overlay">
      <div class="modal-box">
        <p>
          저장하지 않은 변경 사항이 있습니다.<br />
          완료 버튼을 누르지 않을 경우 변경 하신 정보가 반영되지 않습니다.
        </p>
        <button class="modal-confirm" @click="confirmLeave">확인</button
        >&nbsp;&nbsp;&nbsp;
        <button @click="cancelLeave">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  // 컴포넌트의 이름을 정의. (Vue 개발자도구 등에서 이 이름으로 컴포넌트가 표시됨)
  name: "MemberEdit",

  // 컴포넌트 내부에서 관리할 반응형 데이터(data)를 반환하는 함수
  data() {
    return {
      // [수정] 고정값("user1") 대신 login.vue가 sessionStorage에 저장해둔 실제 로그인 id 사용.
      // created()에서 세팅되기 전까지는 빈 문자열.
      currentUserId: "",

      // 화면에 표시 중인 회원 정보 (mounted 시점에 fetchMember()로 서버에서 조회해서 채워짐)
      member: {
        id: "", // 아이디 (수정 불가, 조회 전용)
        name: "", // 이름 (수정 불가, 조회 전용)
        phone: "", // 전화번호
        email: "", // 이메일
        carType: 0, // 차종 (0=일반차, 1=전기차)
        carNum: "", // 차량번호
      },

      // [추가] fetchMember() 응답이 도착했는지 여부 (도착 전엔 입력 필드들이 안 보이도록 처리)
      memberLoaded: false,

      // [추가] 서버에서 처음 받아온 값의 스냅샷.
      // 수정완료 시 어떤 필드가 실제로 바뀌었는지 비교하고, 페이지 이탈 경고(hasUnsavedChanges)에도 사용함.
      original: {
        phone: "",
        email: "",
        carType: 0,
        carNum: "",
      },

      // ===== 전화번호 변경 관련 =====
      phoneMiddle: "", // 전화번호 가운데 4자리 입력값 (fetchMember 후 기존 값으로 초기화)
      phoneLast: "", // 전화번호 뒷 4자리 입력값 (fetchMember 후 기존 값으로 초기화)
      phoneErrorMsg: "", // 전화번호 관련 에러 안내문구

      // ===== 이메일 변경 관련 =====
      emailId: "", // 이메일 아이디(@ 앞부분) 입력값 (fetchMember 후 기존 값으로 초기화)
      emailDomainSelect: "선택", // 도메인 드롭다운에서 현재 선택된 값
      emailDomainCustom: "", // '직접입력' 선택 시 사용자가 직접 입력한 도메인 값
      emailDomainOptions: ["선택", "naver.com", "gmail.com", "daum.net", "직접입력"], // 드롭다운 옵션 목록
      emailErrorMsg: "", // 이메일 관련 에러 안내문구

      // ===== 비밀번호 변경 관련 =====
      editingPw: false, // [추가] '비밀번호 변경' 버튼을 눌러 비밀번호 수정 영역이 열려있는지 여부
      currentPwInput: "", // [추가] 1단계에서 입력하는 현재 비밀번호
      currentPwErrorMsg1: "", // [추가] 현재 비밀번호 불일치 등 에러 안내문구
      currentPwErrorMsg2: "",
      pwVerifying: false, // [추가] 서버에 현재 비밀번호 확인 요청 중인지 여부 (중복 클릭 방지)
      pwVerified: false, // [추가] 현재 비밀번호 확인(서버 검증)에 성공했는지 여부 -> true여야 2단계(새 비밀번호) 노출
      newPw: "", // 사용자가 입력한 "새" 비밀번호
      newPwConfirm: "", // 새 비밀번호 재확인 입력값
      newPwConfirmMsg: "", // 재확인 일치/불일치 실시간 안내문구
      newPwConfirmOk: false, // 재확인 일치 여부 (true면 파란색, false면 빨간색 문구)
      showNewPw: false, // 새 비밀번호 입력창 표시(text)/숨김(password) 토글 상태
      showNewPwConfirm: false, // 재확인 입력창 표시/숨김 토글 상태

      // ===== 차종 변경 관련 =====
      carTypeSelected: 0, // [수정] 체크박스 대신 라디오 버튼과 바인딩되는 값 (0=일반차, 1=전기차)

      // ===== 차량번호 변경 관련 =====
      carNumInput: "", // 새로 입력한 차량번호 (fetchMember 후 기존 값으로 초기화)
      carNumErrorMsg: "", // [추가] 차량번호 글자수(7~8자) 안내문구

      // ===== [추가] 완료/취소 확인 모달 =====
      completeModal: { show: false }, // 수정완료 모달 노출 여부
      cancelModal: { show: false }, // 수정취소 모달 노출 여부

      // ===== 공통: 페이지 이탈 경고 모달 =====
      showLeaveModal: false, // 저장 안 된 변경사항이 있을 때 페이지를 벗어나려 하면 뜨는 경고 모달 노출 여부
      pendingNext: null, // 라우터가 준 이동 실행 함수를 잠깐 보관해뒀다가, 모달에서 확인/취소 누르면 그때 실행
    };
  },

  // 반응형 데이터가 바뀔 때 자동으로 다시 계산되는 연산 속성(computed)
  computed: {
    // [새 비밀번호 조건 미충족 안내문구] (7글자 이상 + 대/소문자 각 1개 이상 + 영문/숫자만 조건)
    newPwConditionMsg() {
      // 사용자가 입력한 새 비밀번호(newPw)가 비밀번호 조건을 만족하는지 실시간으로 검사해서, 안 맞으면 경고 문구를 돌려줌
      // isValidNewPw : 새 비밀번호가 유효한지 검사하는 함수.
      if (this.newPw !== "" && !this.isValidNewPw(this.newPw)) {
        return "비밀번호 조건이 맞지 않습니다. 확인 후 다시 입력해주세요.";
      }
      return "";
    },

    // [기존 비밀번호와 동일 여부 안내문구]
    // [수정] 방금 확인 성공한 currentPwInput과 비교
    newPwSameAsOldMsg() {
      // 새로 입력한 비밀번호가 방금 확인 절차에서 입력한 현재 비밀번호(currentPwInput)와 똑같은지 검사
      if (this.newPw !== "" && this.newPw === this.currentPwInput) {
        return "기존 비밀번호와 동일합니다.";
      }
      return "";
    },

    // [추가] 비밀번호 변경 영역이 최종 저장 가능한 상태인지 여부
    isPwReady() {
      // is : "true 또는 false를 돌려준다"는 함수의 강한 신호로 흔히 쓰인다.
      //비밀번호 변경에 필요한 모든 조건이 전부 충족됐는지를 한 번에 판단하는 "종합 판정" 함수. 4가지를 AND로 다 만족해야 true
      return (
        this.pwVerified && //현재 비밀번호 확인(1단계)을 통과했는가
        this.isValidNewPw(this.newPw) && //새 비밀번호가 형식 조건에 맞는가(검사하는 함수 isValidNewPw)
        this.newPw !== this.currentPwInput && //새 비밀번호가 기존 것과 다른가
        this.newPw === this.newPwConfirm //새 비밀번호와 재확인 입력이 일치하는가
      );
    },

    // [추가] 저장하지 않은 변경 사항이 있는지 여부. 페이지 이탈 경고와 취소 버튼 동작 판단에 사용.
    hasUnsavedChanges() {
      // 저장 안 한 변경사항이 하나라도 있는지 판단 (전화번호/이메일/차종/차량번호/비밀번호 변경 여부)
      // 전화번호: 대시 유무 상관없이 비교하려고 숫자만 뽑아서(filterDigits) 비교.
      // (그대로 문자열 비교하면, DB에 대시 없이 저장된 회원은 안 바꿔도 "변경됨"으로 잘못 판단됨)
      let phoneTouched;
      if (this.phoneMiddle !== "" || this.phoneLast !== "") {
        //filterDigits : 숫자만 뽑아내는 함수.
        const currentPhoneDigits = this.filterDigits(
          "010" + this.phoneMiddle + this.phoneLast
        );
        const originalPhoneDigits = this.filterDigits(this.original.phone || "");
        phoneTouched = currentPhoneDigits !== originalPhoneDigits;
      } else {
        phoneTouched = false;
      }
      // 이메일: 입력창 값이 아니라 "저장된 원본과 다른가"로 판단 (안 그러면 저장 성공 후에도 계속 "변경됨"으로 오판됨)
      // buildEmailValue() : 이메일 입력창 값들을 합쳐서 "아이디@도메인" 형태로 만들어주는 함수.
      const emailTouched = this.buildEmailValue() !== this.original.email;
      const carTouched =
        //Number(this.carTypeSelected) : (라디오 버튼과 연결된 값을)숫자로 변환.
        // > v-model.number가 이미 숫자로 만들어주지만, 혹시 모를 타입 불일치를 방지하려고 한 번 더 명시적으로 변환
        Number(this.carTypeSelected) !== this.original.carType ||
        this.carNumInput !== this.original.carNum; // 차종이 바뀌었거나, 차량번호가 바뀌었거나, 둘 중 하나라도 해당하면 "true"
      return phoneTouched || emailTouched || carTouched || this.editingPw;
      // 4개 중 1개 변경 중이면 전체 결과가 "true" > 저장 안 한 변경 사항이 있다고 최종 판단.
    },
  },

  // [추가] created()는 mounted()보다 먼저 실행되므로, 여기서 로그인 여부를 확인하고
  // currentUserId를 세팅해둔 뒤에 mounted()에서 API를 호출하도록 순서를 보장함.
  created() {
    // login.vue의 login() 메소드가 로그인 성공 시 sessionStorage에 'login' 키로
    // 회원 정보(JSON 문자열)를 저장해둠. 이 값이 없으면 로그인하지 않은 상태.
    const loginInfo = sessionStorage.getItem("login");

    // 로그인 정보가 없으면 로그인 페이지로 이동시키고, 이 화면의 API 호출은 하지 않음.
    if (!loginInfo) {
      alert("로그인이 필요한 페이지입니다.");
      this.$router.push({ name: "login" });
      return;
    }

    // 저장된 로그인 정보(JSON 문자열)를 객체로 변환해서 id만 꺼내 사용.
    const loginMember = JSON.parse(loginInfo);
    this.currentUserId = loginMember.id;
  },

  // 컴포넌트가 화면에 장착된 직후 자동으로 실행됨 (데이터 최초 조회 시점)
  mounted() {
    // [수정] created()에서 로그인 확인에 실패해 페이지 이동이 걸린 경우에는
    // currentUserId가 비어있으므로, 그 상태에서는 불필요한 API 호출을 하지 않음.
    if (!this.currentUserId) {
      return;
    }
    this.fetchMember();
  },

  // beforeRouteLeave()함수란? "네비게이션 가드"라는 기능
  // 다른페이지로 이동하기 직전에 가로채서 원하는 로직을 실행할 수 있게 해주는 함수.
  // [수정] editingField(토글 방식) 대신 hasUnsavedChanges computed로 판단하도록 변경
  beforeRouteLeave(to, from, next) {
    if (this.hasUnsavedChanges) {
      this.showLeaveModal = true;
      this.pendingNext = next;
    } else {
      next();
    }
  },

  // 컴포넌트 내부에서 사용할 메소드(함수) 모음
  methods: {
    // [회원 정보 조회 함수] 화면에 보여줄 현재 회원 정보를 서버에서 가져옴.
    fetchMember() {
      let url = "/getmember";
      let param = { params: { id: this.currentUserId } };

      axios
        .get(url, param)
        .then((resp) => {
          // 서버 응답 데이터로 member를 통째로 교체 (응답이 없으면 기존 member 유지)
          this.member = resp.data || this.member;

          // [추가] 전화번호/이메일/차종/차량번호를 입력창에 바로 채워넣기 위한 초기화
          this.initEditableFields();

          // 회원 정보 로딩이 끝났음을 표시
          this.memberLoaded = true;
        })
        .catch((err) => {
          alert(err);
        });
    },

    // [추가] 조회된 member 값으로 각 편집용 입력값과 original 스냅샷을 채워주는 함수.
    // 스냅샷이란? "이 순간의 값을 사진 찍듯 그대로 복사해서, 원본과는 별개로 저장해둔 것".
    // 그래서 이후에 사용자가 입력창을 아무리 바꿔도, 스냅샷(original)은 그대로 남아있어, 뭐가 달라졌는지 비교해서 판단할 수 있는 기준점 역할을 함.
    // 복사해서 따로 저장해야하는 이유는? 입력창용 데이터에만 반영되기에 값이 그대로 남아있다.
    initEditableFields() {
      // [수정] "-" split은 대시 없는 형식("01022222222")에서 파싱 실패해 입력창이 항상
      // 비어버리는 버그가 있었음. 숫자만 뽑아서(filterDigits) 자리수로 자르는 방식으로 변경.
      const phoneDigits = this.filterDigits(this.member.phone || "");

      // 010(3자리) + 가운데 4자리 + 뒷 4자리 = 총 11자리일 때만 정상적으로 파싱
      if (phoneDigits.length === 11) {
        this.phoneMiddle = phoneDigits.substring(3, 7);
        this.phoneLast = phoneDigits.substring(7, 11);
      } else {
        this.phoneMiddle = "";
        this.phoneLast = "";
      }

      // 이메일을 "아이디@도메인" 형태로 파싱
      const email = this.member.email || "";
      const atIndex = email.indexOf("@");
      if (atIndex > 0) {
        this.emailId = email.substring(0, atIndex);
        const domain = email.substring(atIndex + 1);
        // 미리 정의된 도메인 목록에 있으면 그대로 선택, 없으면 '직접입력'으로 처리
        if (this.emailDomainOptions.includes(domain)) {
          this.emailDomainSelect = domain;
          this.emailDomainCustom = "";
        } else {
          this.emailDomainSelect = "직접입력";
          this.emailDomainCustom = domain;
        }
      } else {
        this.emailId = "";
        this.emailDomainSelect = "선택";
        this.emailDomainCustom = "";
      }

      // 차종/차량번호는 그대로 복사
      this.carTypeSelected = this.member.carType;
      this.carNumInput = this.member.carNum || "";

      // 변경 여부 비교용 스냅샷 저장
      //fetchMember()로 서버에서 회원 정보를 받아온 순간,
      //this.member의 값을 그대로 복사해서
      // this.original이라는 별도의 자리에 따로 저장함.
      this.original = {
        phone: this.member.phone || "",
        email: this.member.email || "",
        carType: this.member.carType,
        carNum: this.member.carNum || "",
      };
    },

    // [추가] 차종 변환 함수. carType(0=일반차, 1=전기차)을 한글 텍스트로 변환.
    // - 라디오 버튼 위에 "현재 차종" 텍스트를 보여주기 위해 추가함.
    getCarTypeText(carType) {
      if (Number(carType) === 1) {
        return "전기차";
      } else {
        return "일반차";
      }
    },

    // [추가] 한글 완성형 음절 + 숫자만 남기는 함수 (차량번호 입력용 - 영문/특수문자 제거)
    filterKoreanAndDigits(str) {
      let result = "";
      for (let i = 0; i < str.length; i++) {
        let ch = str.charAt(i);
        let code = ch.charCodeAt(0);
        // 가(0xAC00) ~ 힣(0xD7A3): 한글 완성형 음절 범위
        if ((code >= 0xac00 && code <= 0xd7a3) || (ch >= "0" && ch <= "9")) {
          result = result + ch;
        }
      }
      return result;
    },

    // [숫자만 남기는 함수] 문자열에서 0~9 숫자가 아닌 문자를 전부 제거.
    filterDigits(str) {
      let result = "";
      for (let i = 0; i < str.length; i++) {
        let ch = str.charAt(i);
        if (ch >= "0" && ch <= "9") {
          result = result + ch;
        }
      }
      return result;
    },
    // [추가] 영문/숫자만 남기는 함수 (이메일 아이디, 비밀번호 입력용 - 한글/특수문자 제거)
    filterAlnum(str) {
      let result = "";
      for (let i = 0; i < str.length; i++) {
        let ch = str.charAt(i);
        if (
          (ch >= "0" && ch <= "9") ||
          (ch >= "a" && ch <= "z") ||
          (ch >= "A" && ch <= "Z")
        ) {
          result = result + ch;
        }
      }
      return result;
    },

    // [추가] 영문/숫자/마침표(.)만 남기는 함수 (이메일 도메인 직접입력용 - 도메인엔 점이 꼭 필요해서 alnum과 분리)
    filterDomainChars(str) {
      let result = "";
      for (let i = 0; i < str.length; i++) {
        let ch = str.charAt(i);
        if (
          (ch >= "0" && ch <= "9") ||
          (ch >= "a" && ch <= "z") ||
          (ch >= "A" && ch <= "Z") ||
          ch === "."
        ) {
          result = result + ch;
        }
      }
      return result;
    },

    // ===================== 전화번호 =====================
    // [추가] compositionend는 "조합이 끝나서 실제로 데이터에 글자가 들어온 바로 그 순간"을 잡아서 필터링하는 것
    onPhoneCompositionEnd(e) {
      // 이벤트가 발생한 입력창을 직접 참조해서 필터링 (전화번호 가운데/뒷자리 둘 다 이 함수 하나로 처리)
      if (e.target === this.$refs.phoneMiddleInput) {
        // 조합이 끝나서 이제서야 phoneMiddle에 실제로 반영된 값을 다시 필터링
        this.phoneMiddle = this.filterDigits(this.phoneMiddle);
      } else if (e.target === this.$refs.phoneLastInput) {
        this.phoneLast = this.filterDigits(this.phoneLast);
      }
    },

    // [가운데 4자리 입력 처리] 숫자만 남기고, 4자리가 다 채워지면 다음 입력칸으로 커서 자동 이동.
    onPhoneMiddleInput() {
      this.phoneMiddle = this.filterDigits(this.phoneMiddle);
      if (this.phoneMiddle.length === 4) {
        this.$refs.phoneLastInput.focus();
      }
    },

    // [뒷 4자리 입력 처리] 숫자만 남김.
    onPhoneLastInput() {
      this.phoneLast = this.filterDigits(this.phoneLast);
    },

    // ===================== 이메일 =====================
    // 입력 필드(emailId/domain)를 조합해서 실제 이메일 문자열을 만드는 함수.
    // 아이디/도메인이 둘 다 비어있으면 "이메일 없음"으로 보고 빈 문자열 반환.
    // (건드린 적 없으면 항상 original.email과 같은 값이 나옴)
    buildEmailValue() {
      if (this.emailId === "" && this.emailDomainSelect === "선택") {
        return "";
      }

      // 도메인을 '직접입력'으로 선택했으면 직접 입력한 도메인 값을 사용하고,
      // 그 외에는 드롭다운에서 선택한 도메인 값을 그대로 사용.
      let domain;
      if (this.emailDomainSelect === "직접입력") {
        domain = this.emailDomainCustom;
      } else {
        domain = this.emailDomainSelect;
      }
      return this.emailId + "@" + domain;
    },

    // [도메인 형식 검사 함수] 점(.)이 앞뒤로 하나 이상 있는지, 공백은 없는지 확인.
    isValidDomain(domain) {
      if (!domain) {
        return false;
      }
      let dotIndex = domain.indexOf(".");
      if (dotIndex <= 0 || dotIndex === domain.length - 1) {
        return false;
      }
      for (let i = 0; i < domain.length; i++) {
        if (domain.charAt(i) === " ") {
          return false;
        }
      }
      return true;
    },
    // [추가] 이메일 아이디 입력 처리: 영문/숫자만 허용 + 최대 30자 제한
    onEmailIdInput() {
      this.emailId = this.filterAlnum(this.emailId).slice(0, 30);
    },

    // [추가] 이메일 도메인 직접입력 처리: 영문/숫자/마침표만 허용 + 최대 30자 제한
    onEmailDomainCustomInput() {
      this.emailDomainCustom = this.filterDomainChars(this.emailDomainCustom).slice(
        0,
        30
      );
    },

    // ===================== 비밀번호 =====================
    // [추가] '비밀번호 변경' 버튼 클릭 시 1단계(현재 비밀번호 확인) 영역 노출.
    startEditingPw() {
      this.editingPw = true;
      this.currentPwInput = "";
      this.currentPwErrorMsg = "";
      this.pwVerified = false;
      this.newPw = "";
      this.newPwConfirm = "";
      this.newPwConfirmMsg = "";
      this.newPwConfirmOk = false;
    },

    // [추가] 비밀번호 변경 영역을 닫고 입력값을 모두 초기화.
    cancelEditingPw() {
      this.editingPw = false; // 비밀번호 수정 영역을 다시 닫음
      this.currentPwInput = ""; // 입력했던 현재 비밀번호 지움
      this.currentPwErrorMsg = ""; // 에러 문구 지움
      this.pwVerified = false; // "현재 비밀번호 확인됨" 상태 취소
      this.newPw = ""; // 입력했던 새 비밀번호 지움
      this.newPwConfirm = ""; // 입력했던 재확인 비밀번호 지움
    },

    // [추가] 현재 비밀번호가 실제 비밀번호와 일치하는지 서버에 확인.
    // 백엔드에 별도의 '비밀번호 확인' 전용 API가 없으므로, 기존 로그인 API(/login)에
    // 아이디+현재 비밀번호를 그대로 보내 로그인 성공 여부로 일치 여부를 판단함.
    verifyCurrentPw() {
      if (!this.currentPwInput) {
        this.currentPwErrorMsg = "현재 비밀번호를 입력해주세요.";
        return;
      }

      this.pwVerifying = true;
      this.currentPwErrorMsg = "";

      let url = "/login";
      let param = new URLSearchParams({
        id: this.currentUserId,
        pw: this.currentPwInput,
      });

      axios
        .post(url, param)
        .then((resp) => {
          // 로그인 성공 시 회원 정보 객체가, 실패 시 null이 내려옴
          if (resp.data) {
            this.pwVerified = true;
            this.currentPwErrorMsg = "";
          } else {
            this.pwVerified = false;

            this.currentPwErrorMsg1 = '비밀번호가 일치하지 않습니다.';
            this.currentPwErrorMsg2 = '다시 확인해주세요.';
          }
        })
        .catch((err) => {
          alert(err);
        })
        .finally(() => {
          this.pwVerifying = false;
        });
    },

    // [새 비밀번호 조건 검사 함수] 7글자 이상, 대문자/소문자/숫자 각 1개 이상 필수.
    isValidNewPw(pw) {
      // 새 비밀번호 조건을 검사하는 함수.
      if (pw.length < 7) {
        return false;
      }

      let hasNum = false;
      let hasUpper = false;
      let hasLower = false;

      for (let i = 0; i < pw.length; i++) {
        let ch = pw.charAt(i);

        if (ch >= "0" && ch <= "9") {
          hasNum = true;
        } else if (ch >= "a" && ch <= "z") {
          hasLower = true;
        } else if (ch >= "A" && ch <= "Z") {
          hasUpper = true;
        } else {
          return false;
        }
      }

      return hasUpper && hasLower && hasNum;
    },

    // [새 비밀번호 입력 처리] 입력할 때마다 재확인 값과의 일치 여부를 다시 검사.
    // [수정] 한글 등 허용 안 되는 문자를 입력 즉시 걸러내도록 필터링 추가 (최대 20자 제한)
    onNewPwInput() {
      this.newPw = this.filterAlnum(this.newPw).slice(0, 20);
      this.onNewPwConfirmInput();
    },
    // [비밀번호 재확인 입력 처리] 실시간으로 일치 여부 안내문구를 갱신.
    // [수정] 재확인 입력창에도 동일하게 필터링 적용
    // filterAlnum() : 영문/숫자만 남기는 함수. (Alnum => Alphabet(알파벳) + Number(숫자)"를 합친 약어)
    // slice(0, 20) : 최대 20자 제한(앞에서부터 20글자만 남김.)
    onNewPwConfirmInput() {
      this.newPwConfirm = this.filterAlnum(this.newPwConfirm).slice(0, 20);

      if (this.newPwConfirm === "") {
        this.newPwConfirmMsg = "";
        return;
      }
      if (this.newPwConfirm === this.newPw) {
        this.newPwConfirmMsg = "비밀번호가 일치합니다.";
        this.newPwConfirmOk = true;
      } else {
        this.newPwConfirmMsg = "비밀번호가 일치하지 않습니다.";
        this.newPwConfirmOk = false;
      }
    },

    // ===================== [추가] 최종 수정완료 처리 =====================
    // [추가] 화면 최하단 '수정완료' 버튼 클릭 시 실행되는 함수.
    // 1) 입력값들을 검증하고, 2) 변경된 항목만 골라 각 백엔드 API를 호출하고,
    // 3) 모두 성공하면 완료 모달을 노출함.
    async onClickComplete() {
      // --- 1. 전화번호 검증 및 값 계산 ---
      if (this.phoneMiddle.length !== 4 || this.phoneLast.length !== 4) {
        this.phoneErrorMsg = "전화번호를 올바르게 입력해주세요.";
        alert("전화번호를 올바르게 입력해주세요.");
        return;
      }
      this.phoneErrorMsg = "";
      const newPhone = "010-" + this.phoneMiddle + "-" + this.phoneLast;

      // --- 2. 이메일 검증 및 값 계산 ---
      // [수정] 이메일은 선택 입력. 아예 안 건드렸으면 검증 없이 통과, 뭐라도 입력/선택했으면 형식 검증함.
      const emailTouched = this.emailId !== "" || this.emailDomainSelect !== "선택";
      let newEmail = "";

      if (emailTouched) {
        if (!this.emailId) {
          this.emailErrorMsg = "이메일 아이디를 입력해주세요.";
          alert("이메일 아이디를 입력해주세요.");
          return;
        }
        let domain = this.emailDomainCustom;
        if (this.emailDomainSelect !== "직접입력") {
          domain = this.emailDomainSelect;
        }
        if (this.emailDomainSelect === "선택" || !this.isValidDomain(domain)) {
          this.emailErrorMsg = "이메일 주소를 올바르게 입력해주세요.";
          alert("이메일 주소를 올바르게 입력해주세요.");
          return;
        }
        newEmail = this.emailId + "@" + domain;
      }
      this.emailErrorMsg = "";

      // --- 3. 비밀번호 검증 (변경을 시도한 경우에만) ---
      if (this.editingPw) {
        if (!this.pwVerified) {
          alert("현재 비밀번호 확인을 먼저 완료해주세요.");
          return;
        }
        if (!this.isPwReady) {
          alert("새 비밀번호 조건 및 재확인 값을 다시 확인해주세요.");
          return;
        }
      }

      // --- 3-1. 차량번호 길이 검증 (7~8자) ---
      if (this.carNumInput.length < 7 || this.carNumInput.length > 8) {
        this.carNumErrorMsg = "차량번호는 7~8자로 입력해주세요.";
        alert("차량번호는 7~8자로 입력해주세요.");
        return;
      }
      this.carNumErrorMsg = "";

      // --- 4. 변경된 항목만 판별 ---
      // [수정] original.phone에 대시가 없을 수 있어서(예: "01022222222") 그대로 비교하면
      // 안 바뀌어도 "바뀜"으로 오판됨. hasUnsavedChanges와 동일하게 숫자만 뽑아서 비교.
      // filterDigits() : 숫자만 뽑아내는 함수.
      const phoneChanged =
        this.filterDigits(newPhone) !== this.filterDigits(this.original.phone || "");
      const emailChanged = newEmail !== this.original.email;
      const carChanged =
        Number(this.carTypeSelected) !== this.original.carType ||
        this.carNumInput !== this.original.carNum;
      const pwChanged = this.editingPw && this.isPwReady; // isPwReady() : 비밀번호 변경 조건이 모두 충족됐는지 여부

      // 아무 것도 바뀐 게 없으면 서버 호출 없이 안내만 하고 종료
      if (!phoneChanged && !emailChanged && !carChanged && !pwChanged) {
        alert("변경된 항목이 없습니다.");
        return;
      }

      // --- 5. 변경된 항목에 대해서만 각 API 호출 ---
      const baseUrl = "";
      const requests = []; // requests : 여러 객체의 담을 그릇(배열) => 장바구니

      if (phoneChanged) {
        requests.push({
          //.push : 그릇(requests)에 새 요소를 하나 추가하는 동작 => 바구니에 "물건을 담아라"는 동작
          name: "phone",
          req: axios.post(
            //req : 그릇(requests) 안에 새로 들어가는 객체 하나가 가진 속성(키) 이름. => 바구니 안에 담기는 개별 상품 하나에 붙은 "가격표" 같은 것
            baseUrl + "/updatephone",
            new URLSearchParams({ id: this.currentUserId, phone: newPhone })
          ),
        });
      }

      if (emailChanged) {
        requests.push({
          // requests => 장바구니. push => 장바구니에 상품 담기
          name: "email",
          req: axios.post(
            // req => 장바구니 안에 담긴 개별 상품 하나에 붙은 "가격표" 같은 것
            baseUrl + "/updateemail",
            new URLSearchParams({ id: this.currentUserId, email: newEmail })
          ),
        });
      }

      if (carChanged) {
        requests.push({
          // requests => 장바구니. push => 장바구니에 상품 담기
          name: "car",
          req: axios.post(
            // req => 장바구니 안에 담긴 개별 상품 하나에 붙은 "가격표" 같은 것
            baseUrl + "/updatecar",
            new URLSearchParams({
              id: this.currentUserId,
              carNum: this.carNumInput,
              carType: Number(this.carTypeSelected),
            })
          ),
        });
      }

      // [수정] 비밀번호 변경 시에는 아이디 + 기존 비밀번호 + 새 비밀번호를 함께 전달
      if (pwChanged) {
        requests.push({
          // requests => 장바구니. push => 장바구니에 상품 담기
          name: "pw",
          req: axios.post(
            // req => 장바구니 안에 담긴 개별 상품 하나에 붙은 "가격표" 같은 것
            baseUrl + "/updatepw",
            new URLSearchParams({
              id: this.currentUserId,
              currentPw: this.currentPwInput,
              newPw: this.newPw,
            })
          ),
        });
      }

      try {
        const results = await Promise.all(requests.map((r) => r.req));

        // 하나라도 실패(false) 응답이면 실패 처리
        // 각 요청의 응답을 순서대로 확인해서,
        // 실패(false)한 항목의 이름만 failedNames 배열에 담음.
        const failedNames = [];
        for (let i = 0; i < requests.length; i++) {
          if (!results[i].data) {
            failedNames.push(requests[i].name);
          }
        }

        if (failedNames.length > 0) {
          alert("다음 항목 변경에 실패했습니다: " + failedNames.join(", "));
          return;
        }

        // --- 6. 화면 상태 및 스냅샷 갱신 ---
        this.member.phone = newPhone;
        this.member.email = newEmail;
        this.member.carType = Number(this.carTypeSelected);
        this.member.carNum = this.carNumInput;
        this.original = {
          phone: newPhone,
          email: newEmail,
          carType: Number(this.carTypeSelected),
          carNum: this.carNumInput,
        };
        if (pwChanged) {
          // 비밀번호 변경 영역 초기화 (다시 잠금 상태로)
          this.cancelEditingPw();
          // cancelEditingPw() : "비밀번호 관련 입력값을 전부 지우고, 수정 영역을 다시 닫는" 함수
          // 원래는 취소 버튼용이었지만, "저장 성공 후에도 결국 똑같이 초기화하고 영역을 닫아야 하니" 그대로 재사용함.
        }

        // 완료 모달 노출
        this.completeModal.show = true;
      } catch (err) {
        alert(err);
      }
    },

    // [추가] 화면 최하단 '취소' 버튼 클릭 시 실행. 취소 확인 모달을 띄움.
    onClickCancel() {
      this.cancelModal.show = true;
    },

    // [추가] 완료 모달의 확인/X 버튼 처리.
    // confirmed === true(확인) : 마이페이지로 이동
    // confirmed === false(X)   : 모달만 닫고 수정 화면에 머무름
    closeCompleteModal(confirmed) {
      this.completeModal.show = false;
      if (confirmed) {
        this.$router.push({ name: "my" });
      }
    },

    // [추가] 취소 모달의 확인/X 버튼 처리.
    // confirmed === true(확인) : 변경 중이던 입력값을 원래 값으로 되돌리고 마이페이지로 이동
    // confirmed === false(X)   : 모달만 닫고, 입력하던 값은 그대로 유지한 채 수정 화면에 머무름
    closeCancelModal(confirmed) {
      //"취소" 버튼을 누르고, 뜬 확인 모달에서 "확인"을 눌렀을 때 실행됨.
      this.cancelModal.show = false;
      if (confirmed) {
        // 입력값을 마지막으로 저장된 원본 값으로 되돌림
        this.initEditableFields(); //전화번호·이메일·차종·차량번호를 서버에서 받아온 원래 값으로 되돌리는 역할
        this.cancelEditingPw(); //비밀번호 입력창에 남아있던 내용을 지우고 잠긴 상태로 되돌리는 역할
        this.$router.push({ name: "my" });
      }
    },

    // ===================== 공통: 페이지 이탈 경고 =====================

    // [페이지 이탈 확인 모달 - 확인 버튼] 보류해둔 라우터 이동을 실행.
    confirmLeave() {
      this.showLeaveModal = false;
      if (this.pendingNext) {
        // "진짜 실행할 게 저장돼 있는지" 확인하는 안전장치
        this.pendingNext(); // 아무것도 안 넣고 호출 → "원래 가려던 곳으로 이동 진행해"
      }
    },

    // [페이지 이탈 확인 모달 - 취소 버튼] 라우터 이동을 취소하고 현재 페이지에 머무름.
    cancelLeave() {
      this.showLeaveModal = false;
      if (this.pendingNext) {
        this.pendingNext(false); // false를 넣고 호출 → "이동을 취소하고 원래 페이지에 그대로 있어"
      }
    },
    // [추가] 차량번호 입력 처리: 한글/숫자만 허용 + 최대 8자 제한
    // filterKoreanAndDigits() : 문자열에서 "한글 완성형 글자"와 "숫자"만 남기고 나머지는 다 지우는 함수
    onCarNumInput() {
      this.carNumInput = this.filterKoreanAndDigits(this.carNumInput).slice(0, 8);
    },
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/member/CSS/myupdate.css"></style>
