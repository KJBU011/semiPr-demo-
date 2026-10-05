+++
<script setup>
import axios from "axios";
</script>

<template>
  <div class="member-page">
    <!-- ==================== 1. 기본 회원 정보 섹션 ==================== -->
    <section class="basic-info">
      <h2>기본정보</h2>
      <hr />
      <br />

      <!-- 회원 기본 정보 항목들을 나열하는 정렬용 컨테이너 -->
      <!-- 바인딩(binding)란? 화면과 데이터를 연결한다. 라는 뜻. -->
      <!-- 단방향일 경우 : 데이터가 바뀌면 화면이 자동으로 따라 바뀜. -->
      <!-- 양방향일 경우 : 화면(입력창)에서 값을 바꾸면 데이터도 바뀌고, 데이터가 바뀌면 화면도 바뀐다. -->
      <div class="info-list">
        <!-- 마스킹 처리된 회원 ID 출력 (computed 속성인 maskedId 연산 결과 바인딩) -->
        <p class="info-row">
          <span class="info-label">아이디</span
          ><span class="info-value">{{ maskedId }}</span>
        </p>

        <!-- 회원 이름 출력 (백엔드에서 받은 member 객체의 name 필드 바인딩) -->
        <p class="info-row">
          <span class="info-label">이름</span
          ><span class="info-value">{{ member.name }}</span>
        </p>

        <!-- 회원 전화번호 출력 (백엔드에서 받은 member 객체의 phone 필드 바인딩) -->
        <p class="info-row">
          <span class="info-label">전화번호</span
          ><span class="info-value">{{ member.phone }}</span>
        </p>

        <!-- 회원 이메일 출력 (백엔드에서 받은 member 객체의 email 필드 바인딩) -->
        <!-- 이메일이 있을 경우 이메일 주고 노출되고, 없을 경우 "-" 로 표시. -->
        <p class="info-row">
          <span class="info-label">이메일(선택)</span
          ><span class="info-value">{{ member.email || "-" }}</span>
        </p>

        <!-- 회원 차종 출력 (carType: 0=일반차, 1=전기차 -> 한글 텍스트로 변환) -->
        <p class="info-row">
          <span class="info-label">차종</span
          ><span class="info-value">{{ getCarTypeText(member.carType) }}</span>
        </p>

        <!-- 회원 차량번호 출력 -->
        <p class="info-row">
          <span class="info-label">차량번호</span
          ><span class="info-value">{{ member.carNum }}</span>
        </p>
      </div>

      <!-- [추가] 회원정보 변경 버튼: 클릭 시 회원정보 수정 화면(MemberEdit)으로 전환 -->
      <!-- 라우터로 이동하며, 현재 로그인 사용자 id를 query로 함께 넘겨 수정 화면에서 재조회에 사용 -->
      <div class="edit-move">
        <button class="btn-edit-move" @click="goToMemberEdit">회원 정보 변경</button>
      </div>
      <br />
      <hr />

      <!-- ==================== 2. 현재 주차 상태 및 당일 이용 내역 ==================== -->
      <!-- [추가] 현재 차량이 주차되어 있는 구역(층수 + 자리번호)을 텍스트로 표시 -->
      <!-- 주차 중이 아니면(parkingInfo가 null) '-'로 표시됨 -->
      <div class="parking-location">
        <h3>
          <strong>현재 주차 구역 :</strong> {{ getParkingLocationText(parkingInfo) }}
        </h3>
      </div>
      <hr />

      <!-- 섹션 서브 타이틀 -->
      <h3>현재 주차 상태 및 이용 요금</h3>

      <!-- 현재 주차 상태를 표 형식으로 보여주기 위한 테이블 -->
      <table class="table table-bordered" border="1">
        <thead>
          <tr>
            <th>입차 일시</th>
            <!-- [추가] 경과 시간: entryTime 기준으로 1분마다 실시간 갱신되는 컬럼 (초 단위는 표시 안 함) -->
            <th>이용 시간</th>
            <!-- [수정] 이 컬럼은 실제 출차 시각이 아니라 현재 주차 상태(입차/주차/정산)를 보여주므로
                 헤더도 '출차 시간'이 아니라 '상태'로 표기하는 것이 실제 내용과 맞음 -->
            <th>상태</th>
            <!-- [수정] '상태' 컬럼이 이미 입차/주차/정산 여부를 보여주고 있어서, 옆 컬럼에서
                 "정산 전/정산 완료"를 또 반복하면 같은 정보가 중복됨. 이 컬럼은 순수하게
                 요금(금액) 정보만 담당하도록 '정산 상태' -> '요금'으로 헤더명 변경 -->
            <th>요금</th>
          </tr>
        </thead>

        <tbody>
          <!-- 현재 주차/입차 중인 내역이 존재할 때 -->
          <!-- (getCarStatus에서 출차(car_stat===2) 상태면 parkingInfo를 이미 null로 처리하므로, 여기선 존재 여부만 확인하면 됨) -->
          <tr v-if="parkingInfo">
            <!-- 입차 일시 (예: "8/4 16:48"). 날짜+시간을 한 셀에 같이 표시 -->
            <td>{{ formatShortDateTime(parkingInfo.entryTime) }}</td>

            <!-- [추가] 이용 시간 (HH:mm). elapsedTimeText computed가 nowTick(1분마다 갱신)을 참조하므로
                 화면에 표시되는 동안 자동으로 계속 올라감 -->
            <td>{{ elapsedTimeText }}</td>

            <!-- '상태' 컬럼: 현재 주차 상태 표시 (car_status는 백엔드가 이미 한글로 내려줌) -->
            <!-- [수정] 순수 텍스트로만 출력하도록 변경 -->
            <td>
              {{ parkingInfo.statusText }}
            </td>

            <!-- '요금' 컬럼: 정산 전/후를 "정산 전(N원)/정산 완료" 같은 문장으로 반복하지 않고,
                 '예상'/'확정'이라는 단어 하나로만 구분해서 금액을 보여줌 (정산 여부 자체는 '상태' 텍스트가 이미 담당) -->
            <td>
              {{ getParkingCostText(parkingInfo.isSettled, costCalc(parkingInfo)) }}<br />

              <!-- [수정] "2시간(-4,000원)"처럼 구체적인 시간/금액을 못 박아서 보여주고 있었는데,
                DB에는 "할인을 받았는지 아닌지"만 저장돼 있고(discnt_at) 실제로 매장이 여러 곳일 경우 
                정확한 할인 시간/횟수는 알 수 없음. 부정확한 숫자를 확정적으로 보여주는 대신,
                할인이 있었다는 사실만 담백하게 표시하도록 변경 -->
              <span v-if="parkingInfo.discountApplied"> (할인 2시간 적용) </span>
            </td>
          </tr>

          <!-- 입차 중인 차량이 없을 때 (car_stat === 2, 출차 상태) -->
          <tr v-else>
            <!-- 4개의 컬럼이 하나로 합쳐지면서 문구 노출. -->
            <td colspan="4" class="empty">현재 주차 중인 차량이 없습니다.</td>
          </tr>
        </tbody>
      </table>

      <!-- [8/7 추가] 정산 버튼: 주차 중(정산 전)일 때만 노출 -->
      <div v-if="parkingInfo && !parkingInfo.isSettled" class="settle-move">
        <button class="btn-settle" @click="settleParking">정산하기</button>
      </div>

      <!-- [추가] 정산 완료 안내 모달 -->
      <div v-if="settleModal.show" class="modal-overlay">
        <div class="modal-box">
          <p>
            정산이 완료되었습니다.<br />
            정산이 완료된 후 5분 이내에 출차하세요.<br />
            5분 이내 출차하지 않으시면 추가 요금이 부과됩니다.
          </p>
          <button @click="settleModal.show = false">확인</button>
        </div>
      </div>
    </section>

    <br />
    <hr />
    <br />

    <!-- ==================== 3. DB 기반 과거 주차 이용 내역 ==================== -->
    <section class="history">
      <!-- 섹션 서브 타이틀 -->
      <h3>과거 이용 내역</h3>

      <!-- 과거 이용 내역 리스트를 보여줄 구분선이 있는 테이블 -->
      <table class="table table-striped" border="1">
        <!-- 테이블 헤더 영역 -->
        <thead>
          <tr>
            <th>이용 날짜</th>
            <th>입차 시간</th>
            <th>출차 시간</th>
            <th>결제 금액</th>
          </tr>
        </thead>

        <!-- 테이블 본문 영역 -->
        <tbody>
          <!-- [수정] 진짜 서버 페이징으로 전환. 서버가 이미 해당 페이지의 3건만 내려주므로,
               프론트에서 또 자를 필요 없이 history를 그대로 순회 -->
          <!-- 반복 렌더링(v-for): history 배열의 각 요소(h)를 순회하며 출차 완료된 과거 기록을 행으로 출력 -->
          <!-- h : history, idx : index -->
          <tr v-for="(h, idx) in history" :key="idx">
            <!-- 이용 날짜 출력 (백엔드에 date 필드가 따로 없어 entTime에서 날짜만 추출) -->
            <!-- [수정] 백엔드(CarStatusDto)가 camelCase(entTime)로 내려주는데 snake_case(ent_time)로
                 읽고 있어서 항상 undefined -> "-"만 표시되던 문제 수정 -->
            <td>{{ formatDate(h.entTime) }}</td>

            <!-- 입차 시각 출력 (24시간제 HH:mm 형식 포맷팅) -->
            <td>{{ formatTime(h.entTime) }}</td>

            <!-- 출차 시각 출력 (출차 기록이 있으면 HH:mm 포맷팅, 없으면 하이픈 '-' 표시) -->
            <td>{{ getExitTimeText(h) }}</td>

            <!-- 결제 금액 출력 (숫자 3자리마다 쉼표 추가 및 '원' 단위 추가, 없으면 '0원') -->
            <td>{{ getCostText(h) }}</td>
          </tr>

          <!-- 조건부 렌더링(v-if): 과거 이용 내역 배열이 비어있을 경우 노출 -->
          <tr v-if="history.length === 0">
            <!-- 4개 컬럼을 하나로 합쳐서 안내 메시지 출력 -->
            <td colspan="4" class="empty">과거 이용 내역이 없습니다.</td>
          </tr>
        </tbody>
      </table>

      <!-- [추가] 과거 이용 내역이 3개(historyPageSize)를 넘어가면 페이지네이션 컨트롤 노출 -->
      <!-- 3개(historyPageSize) 이하면 페이지 넘길 필요 없으니 아예 표시 안 함 -->
      <!-- pagination-wrap(가운데 정렬) + pagination(inline-flex)로 버튼-숫자-버튼이 항상 붙어보이게 함 -->
      <div v-if="totalHistoryPages > 1" class="pagination-wrap">
        <div class="pagination">
          <!-- 첫 페이지에서는 '이전' 버튼 비활성화 -->
          <button
            class="page-btn"
            :disabled="historyPage === 1"
            @click="goToHistoryPrevPage"
          >
            이전
          </button>

          <!-- 현재 페이지 / 전체 페이지 수 표시 -->
          <span class="page-info">{{ historyPage }} / {{ totalHistoryPages }}</span>

          <!-- 마지막 페이지에서는 '다음' 버튼 비활성화 -->
          <button
            class="page-btn"
            :disabled="historyPage === totalHistoryPages"
            @click="goToHistoryNextPage"
          >
            다음
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  // 컴포넌트의 이름을 정의.
  name: "MyMember",

  // 컴포넌트 내부에서 상태를 관리할 반응형 데이터(data)를 반환하는 함수
  data() {
    return {
      // [수정] 더 이상 "user1"로 고정하지 않고, 로그인 시 login.vue가 sessionStorage에 저장해둔
      // 실제 로그인 사용자 id로 채움. created()에서 세팅되기 전까지는 빈 문자열로 시작.
      currentUserId: "",

      // 백엔드에서 불러온 회원 기본 정보를 저장할 객체
      member: {
        id: "",
        name: "",
        phone: "",
        email: "",
        carType: 0,
        carNum: "",
      },

      // 현재 진행 중인 입차/주차 상태 정보를 저장할 객체 (없으면 null)
      parkingInfo: null,

      // 과거 출차 완료된 주차 내역 리스트를 저장할 배열
      history: [],

      // ===== [추가] 과거 이용 내역 페이지네이션 관련 =====
      historyPage: 1, // 현재 보고 있는 페이지 번호 (1부터 시작)
      // [수정] 백엔드 LIMIT과 값을 맞춰야 함 (Member.xml의 getParkingHistory가 3건씩 내려줌)
      historyPageSize: 3, // 한 페이지에 보여줄 개수 (3개 넘어가면 다음 페이지로)
      // [추가] 진짜 서버 페이징 전환: 서버에서 받아온 전체 이용 내역 건수 (전체 페이지 수 계산용)
      historyTotalCount: 0,

      // ===== [추가] 현재 주차 상태의 경과 시간 카운트업 / 요금 주기 갱신 관련 =====
      // Date.now() : "지금 몇 시야?"라고 물어보면 그 순간의 시각을 알려주는 시계
      // nowTick : 그 대답을 받아 적어두는 메모장
      nowTick: Date.now(), // 1분마다 갱신되는 현재 시각. elapsedTimeText가 이 값을 참조해서 자동으로 다시 계산됨.
      elapsedTimerId: null, // 이용 시간 갱신용 타이머 ID (my.vue 화면 자체가 화면에서 없어지는 순간 타이머를 강제로 멈춰서 더이상 돌지 않게함. )
      costRefreshTimerId: null, // 요금 재조회 타이머 ID (my.vue 화면 자체가 화면에서 없어지는 순간 타이머를 강제로 멈춰서 더이상 돌지 않게함.)

      // ===== [8/7 추가] 정산 관련 =====
      settleModal: { show: false }, // 정산 완료 안내 모달 노출 여부
      autoExitTimerId: null, // 5분 후 자동 출차 예약(setTimeout) ID
    };
  },

  // 의존된 반응형 데이터가 변경될 때 자동 계산되는 연산 속성(computed)
  computed: {
    // [아이디 마스킹 기능] 회원 ID의 앞 3자리만 노출하고 나머지는 '*' 기호로 변환
    maskedId() {
      // member 객체에 id가 없으면 currentUserId를 사용
      const id = this.member.id || this.currentUserId;
      // id 값이 존재하지 않는 경우 기본 하이픈 반환
      if (!id) return "-";
      // 앞 3자리를 자르고, 나머지는 글자 수만큼 '*'을 붙여서 결합 반환
      return id.slice(0, 3) + "*".repeat(Math.max(0, id.length - 3));
    },

    // [수정] 진짜 서버 페이징으로 전환.
    // 서버에서 따로 받아온 historyTotalCount로 전체 페이지 수 계산.
    // historyTotalCount : 전체 건수 | historyPageSize : 한 페이지당 개수
    // Math.ceil : 올림 처리 (예: 7건 / 3개씩 = 2.33 -> 3페이지 필요)
    totalHistoryPages() {
      return Math.ceil(this.historyTotalCount / this.historyPageSize);
    },

    // [수정] 초 단위까지는 필요 없다고 판단. 'HH:mm'으로 변경.
    // 입차 시각(entryTime)부터 지금(nowTick)까지 경과 시간을 분 단위로 계산.
    // nowTick이 1분마다 바뀌므로, 이 computed도 1분마다 자동으로 다시 계산되어 화면이 갱신됨.
    elapsedTimeText() {
      // 주차 중인 정보 자체가 없으면 계산할 필요 없이 하이픈 반환
      if (!this.parkingInfo || !this.parkingInfo.entryTime) {
        return "-";
      }

      const startTime = new Date(this.parkingInfo.entryTime).getTime();

      // new Date()는 파싱에 실패해도 에러 없이 Invalid Date를 반환하고,
      // 이때 getTime()은 NaN이 됨. entryTime 값은 있지만 형식이 이상해서
      // 날짜로 못 읽는 경우를 걸러내, "NaN시간 NaN분" 같은 깨진 값이
      // 화면에 뜨는 대신 "-"로 안전하게 처리하기 위한 검사.
      if (Number.isNaN(startTime)) {
        return "-";
      }

      // this.nowTick - startTime) / 60000
      // > 지금시각에서 입차 시각을 뺀 값. 1분=60,000밀리초 밀리초를 60,000으로 나누어 분 단위로 변환.
      // Math.floor : 나눈 결과에 소수점이 생기면(예: 72.8분) 버림 처리해서 정수로 만든다.
      let diffMinutes = Math.floor((this.nowTick - startTime) / 60000);

      // 시계 오차 등으로 계산 결과가 음수로 나오면 강제로 0으로 처리.
      if (diffMinutes < 0) {
        diffMinutes = 0;
      }

      const hours = Math.floor(diffMinutes / 60); //전체 경과 분을 60으로 나누고 버림해서, **"몇 시간째인지"**만 뽑아냄.
      const minutes = diffMinutes % 60; //%는 나머지 연산자. 전체 분에서 시간으로 딱 떨어지지 않고 남은 분만 구함.

      // 숫자를 항상 2자리로 맞춰주는 내부 함수 (예: 5 -> "05")
      // 시계 표시할 때 1:5가 아니라 01:05처럼 보이게 하려고 필요
      const pad = (n) => String(n).padStart(2, "0");

      //시간과 분을 각각 두 자리로 맞춘 뒤, 가운데 :(콜론)으로 이어붙여서 최종 문자열을 만듦.
      return pad(hours) + ":" + pad(minutes);
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

  mounted() {
    // [수정] created()에서 로그인 확인에 실패해 페이지 이동이 걸린 경우에는
    // currentUserId가 비어있으므로, 그 상태에서는 불필요한 API 호출을 하지 않음.
    if (!this.currentUserId) {
      return;
    }
    this.fetchData();

    // [추가] 이용 시간 카운트업(1분 간격) 타이머 시작.
    // entryTime을 알아야 다음 경계 시점을 계산할 수 있으므로,
    // getCarStatus() 응답이 도착한 뒤 scheduleNextCostRefresh()에서 알아서 첫 예약을 걸어줌.
    this.startElapsedTimer();
  },

  // [추가] 컴포넌트가 화면에서 사라지기 직전 자동 실행됨.
  // 이 페이지를 벗어나기 직전, Vue가 자동으로 실행해줌
  beforeUnmount() {
    // 타이머는 화면이 사라져도 저절로 안 꺼지므로, 직접 꺼줘야 함
    this.stopElapsedTimer(); // 이용 시간 갱신 타이머 정지
    this.stopCostRefreshTimer(); // 요금 재조회 타이머 정지
    this.stopAutoExitTimer(); // [8/7 추가] 정산 후 타이머 정지.
  },

  // 컴포넌트 내부에서 사용할 메소드(함수) 모음
  methods: {
    // [메인 데이터 조회 함수] 화면이 나타날 때 필요한 모든 데이터를 요청.
    fetchData() {
      // let으로 base url을 먼저 선언
      let url = "";
      // 각각의 API를 개별 함수로 분리해서 요청.
      this.getMember(url);
      this.getCarStatus(url);
      this.getParkingHistory(url);
      // [추가] 진짜 서버 페이징 전환: 전체 페이지 수 계산에 필요한 전체 건수도 같이 조회
      this.getParkingHistoryCount(url);
    },

    // [회원 정보 조회 함수] 회원의 기본 정보를 서버에서 가져옴.
    getMember(url) {
      let getUrl = url + "/getmember";
      const param = { params: { id: this.currentUserId } };

      axios
        .get(getUrl, param)
        .then((resp) => {
          // 서버에서 받은 응답이 없으면 빈 객체로 초기화
          const memberDto = resp.data || {};

          // 서버가 carType, carNum 필드로 내려주므로 그대로 저장
          this.member = memberDto;

          //alert("member: " + JSON.stringify(this.member));
        })
        .catch((err) => {
          // 에러가 나면 alert로 바로 확인
          alert(err);
        });
    },

    // [주차 상태 조회 함수] 현재 차량의 상태를 서버에서 가져옴.
    // ※ 백엔드가 이미 car_status에 한글 텍스트("입차"/"주차"/"출차"/"정산")를 담아서 보내주므로,
    //   프론트엔드에서 별도로 코드를 한글로 변환할 필요가 없음.
    //   car_stat(0~3 숫자)은 출차/정산 여부 판단(carStat===2, ===3) 등 로직 분기에만 사용.
    getCarStatus(url) {
      let getUrl = url + "/getcarstatus";
      const param = { params: { id: this.currentUserId } };

      axios
        .get(getUrl, param)
        .then((resp) => {
          const dto = resp.data || {};

          // [수정] 백엔드(CarStatusDto)는 자바 필드명 그대로 camelCase로 내려줌
          // (carStat, entTime, exTime, carStatus, currentCost 등). snake_case 키는
          // 응답에 존재하지 않아 전부 undefined가 되면서 날짜/시간/상태가 "-"로만
          // 보이던 문제였음. 실제 응답 키에 맞게 수정.

          // carStat === 2 는 출차 상태 -> 현재 주차 중인 정보 없음
          if (dto.carStat === 2) {
            this.parkingInfo = null;
            // 출차했으니 더 이상 안 쓸 요금 재조회 타이머를 꺼둠
            this.stopCostRefreshTimer();
            return;
          }

          // 정산 전(입차/주차 중)에는 예상 금액(currentCost), 정산 후에는 확정 금액(cost)을 사용
          let displayCost = dto.currentCost;
          if (dto.carStat === 3) {
            displayCost = dto.currentCost;
          }

          // 현재 주차 정보를 parkingInfo에 저장
          this.parkingInfo = {
            entryTime: dto.entTime,
            statusText: dto.carStatus, // 이미 한글 텍스트라 그대로 사용
            isSettled: dto.carStat === 3, // 정산(3) 상태인지 여부
            cost: displayCost,
            discntAt: dto.discntAt,
            parkingMinutes:dto.parkingMinutes,
            // [추가] 현재 주차 구역 표시를 위한 층수/자리번호
            floor: dto.floor,
            spcNo: dto.spcNo,
            // [추가] 매장 할인(2시간) 적용 여부. discntAt이 null이 아니면 할인이 적용된 것.
            // discntAt이 있으면 할인 적용된 것. 할인은 항상 2시간 고정이라 계산 없이 표시 가능
            discountApplied: dto.discntAt !== null && dto.discntAt !== undefined,
          };

          // [수정] 5분마다 무조건 재조회하던 방식 대신, 요금이 실제로 바뀌는 다음 시점(정시)을
          // 계산해서 그 시점에 딱 맞춰 한 번만 재조회하도록 예약.
          // 정산 완료면 요금이 더 안 바뀌니 예약 불필요. 아니면 다음 요금 변경 시점에 재조회 예약
          if (dto.carStat !== 3) {
            this.scheduleNextCostRefresh();
          }

          //alert("parkingInfo: " + JSON.stringify(this.parkingInfo));
        })
        .catch((err) => {
          // 에러가 나면 alert로 바로 확인
          alert(err);
          this.parkingInfo = null;
        });
    },

    // ===================== [8/7 추가] 정산 / 자동 출차 =====================
    // [8/7 추가] '정산하기' 버튼 클릭 시에만 실행되어야 하는 코드
    settleParking() {
      axios
        .post(
          // "정산하기" 버튼을 누르면, 백엔드의 updateCarStat API를 carStat= 3(정산)으로 호출.
          "/updatecarstat",
          new URLSearchParams({ id: this.currentUserId, carStat: 3 })
        )
        .then((resp) => {
          if (resp.data) {
            this.getCarStatus(""); // getCarStatus 재조회
            this.settleModal.show = true; // 안내 모달 띄어줌.
            this.startAutoExitTimer(); // 5분뒤 자동 출차 카운트 다운을 여기서 시작.
          } else {
            alert("정산에 실패했습니다. 다시 시도해주세요.");
          }
        })
        .catch((err) => {
          alert(err);
        });
    },

    // [8/7 추가] 정산 완료 시점부터 5분 뒤, 자동으로 출차 처리
    startAutoExitTimer() {
      // 5분 뒤 자동 출차 예약
      // 이전에 걸어둔 타이머가 남아있는 상태에서 또 예약을 걸면, 타이머가 중복으로 여러 개 쌓일 수 있음.
      // 중복 예약 방지 습관.
      this.stopAutoExitTimer();

      this.autoExitTimerId = setTimeout(() => {
        axios
          .post(
            "/updatecarstat",
            new URLSearchParams({ id: this.currentUserId, carStat: 2 })
          )
          .then(() => {
            // 출차 성공 후 아래 세가지를 다시 불러오는 이유
            this.getCarStatus(""); // "현재 주차 상태"표에서 이제 이 기록이 사라져야 하니까
            this.getParkingHistory(""); // 방금 출차한 기록이 "과거 이용 내역"표에 새로 나타나야 하니까, 그 목록과 전체 건수도 같이 갱신
            this.getParkingHistoryCount(""); // 상동
          })
          .catch((err) => {
            alert(err);
          });
      }, 10 * 1000); // 10 초 뒤에 updateCarStat을 이번엔 carStat=2(출차)로 호출
    },

    // [추가] 예약해둔 자동 출차 타이머 정리
    stopAutoExitTimer() {
      if (this.autoExitTimerId) {
        clearTimeout(this.autoExitTimerId);
        this.autoExitTimerId = null;
      }
    },

    // [과거 이용 내역 조회 함수] 주차 기록 목록을 서버에서 가져옴.
    // [수정] 진짜 서버 페이징으로 전환. historyPage(몇 페이지인지)를 서버에 넘겨서 그 페이지에 해당하는 3건만 받아옴.
    getParkingHistory(url) {
      let getUrl = url + "/getparkinghistory";
      const param = { params: { id: this.currentUserId, pageNum: this.historyPage } };

      axios
        .get(getUrl, param)
        .then((resp) => {
          // 받은 데이터가 없으면 빈 배열로 초기화. 이제 여기 들어있는 건 항상
          // "해당 페이지의" 데이터뿐이라, 프론트에서 추가로 자를 필요가 없음.
          this.history = resp.data || [];

          // alert("history: " + JSON.stringify(this.history));
        })
        .catch((err) => {
          // 에러가 나면 alert로 바로 확인
          alert(err);
          this.history = [];
        });
    },

    // [추가] 과거 이용 내역 전체 건수를 서버에서 가져옴. totalHistoryPages 계산에 사용.
    getParkingHistoryCount(url) {
      let getUrl = url + "/getparkinghistorycount";
      const param = { params: { id: this.currentUserId } };

      axios
        .get(getUrl, param)
        .then((resp) => {
          this.historyTotalCount = resp.data || 0;
        })
        .catch((err) => {
          alert(err);
          this.historyTotalCount = 0;
        });
    },

    // [추가] 현재 주차 구역 텍스트 함수. 층수 + 자리번호를 "1층 A-03" 형태로 조합.
    // 주차 중이 아니거나(parkingInfo가 없음) 자리 정보가 없으면 '-' 반환.
    getParkingLocationText(info) {
      if (!info || !info.floor || !info.spcNo) {
        return "-";
      }
      return info.floor + "층 " + info.spcNo;
    },

    // ===================== [추가] 경과 시간 카운트업 / 요금 주기 갱신 =====================
    // 분 단위(HH:mm)까지만 표시하므로 1초 대신 1분마다 갱신 (불필요한 리렌더링 감소)
    startElapsedTimer() {
      // Date.now() = "지금 몇 시야?"라고 물어보면 그 순간의 시각을 알려주는 시계
      // nowTick : 매번 새로 갱신되는 값을 받아 적는 종이일 뿐.
      // setInterval : "1분마다 시계한테 지금이 몇 시인지 다시 확인해서 nowTick에 새로 넣어줘"라는 걸 반복 실행
      this.elapsedTimerId = setInterval(() => {
        this.nowTick = Date.now();
      }, 60 * 1000); // 1분 = 60 * 1000ms
    },

    // [추가] 경과 시간 타이머 정리. beforeUnmount에서 호출해 메모리 누수를 방지.
    stopElapsedTimer() {
      if (this.elapsedTimerId) {
        clearInterval(this.elapsedTimerId);
        this.elapsedTimerId = null;
      }
    },

    // [수정] // 5분마다 무조건 재조회하던 방식 대신, 요금이 바뀌는 다음 시점을 계산해서
    // 그때 딱 한 번만 재조회하도록 예약 (요금 계산 자체는 여전히 서버가 담당)
    scheduleNextCostRefresh() {
      // 남아있는 이전 예약 정리 (중복 방지)
      this.stopCostRefreshTimer();

      // 주차 정보나 입차 시각이 없으면 계산 불가하니 예약하지 않고 종료
      if (!this.parkingInfo || !this.parkingInfo.entryTime) {
        return;
      }

      // 입차 시각(entryTime)을 밀리초 숫자로 변환
      const entryMs = new Date(this.parkingInfo.entryTime).getTime();

      // 변환 실패 시 NaN이 나오므로, 이 경우 계산 불가하니 예약하지 않고 종료.
      // (new Date()는 이상한 값을 넣어도 에러 없이 조용히 Invalid Date를 반환하고,
      //  그 getTime()이 NaN이 됨 → 이걸로 "변환이 잘 됐는지"를 판단)
      // Number.isNaN(entryMs)가 "날짜 변환이 제대로 됐는지" 검사하는 역할
      if (Number.isNaN(entryMs)) {
        return;
      }

      // 입차 후 지금까지 경과한 시간(분)
      const elapsedMin = (Date.now() - entryMs) / 60000;

      // 다음에 요금이 바뀌는 경계 시점(분) 계산
      let nextBoundaryMin;
      if (elapsedMin < 15) {
        // 아직 무료 구간(15분 이내) -> 15분이 되는 순간이 첫 번째 경계 (0원 -> 2,000원)
        nextBoundaryMin = 15;
      } else {
        // 예) 20분 경과 → 다음 경계 75분 / 80분 경과 → 다음 경계 135분
        // 15분 이후부터는 매 60분(1시간)마다 2,000원씩 오름
        // (elapsedMin - 15) / 60) + 1 : (경과 - 15)를 60으로 나눠 "몇 번째 시간 구간"인지 구하고(+1은 다음 구간 기준),
        const hoursPassedSinceGrace = Math.floor((elapsedMin - 15) / 60) + 1;
        // 15 + hoursPassedSinceGrace * 60; : 다시 60을 곱해 무료 15분을 더하면 "전체 기준 다음 경계 시각(분)"이 나옴
        nextBoundaryMin = 15 + hoursPassedSinceGrace * 60;
      }

      // 1) 다음 경계(정시) 시점이 몇 분 뒤인지 계산
      // nextBoundaryMin - elapsedMin : 다음 경계 시점(분) - 지금까지 지난 시간(분) = "다음 경계까지 남은 시간"(분 단위)
      // * 60000 : 밀리초 단위로 변환 (setTimeout은 밀리초 단위)
      // Math.max(..., 0) : 계산 결과가 혹시 음수로 나오면 강제로 0으로 바꿔줌
      const delayMs = Math.max((nextBoundaryMin - elapsedMin) * 60000, 0);

      // 2) 그 시점에 딱 맞춰 타이머(setTimeout)를 걺
      this.costRefreshTimerId = setTimeout(() => {
        // 정시가 되면 서버에서 최신 요금을 다시 받아옴
        this.getCarStatus("");
        // 이 타이머는 한 번 울리면 끝. "그 다음 정시" 타이머는
        // getCarStatus()의 응답을 받은 뒤 scheduleNextCostRefresh()가
        // 다시 실행되면서 자동으로 새로 걸어줌 (울릴 때마다 다음 타이머를 스스로 예약)
      }, delayMs);
    },

    // [추가] // 예약해둔 요금 재조회 타이머를 정리 (페이지 이탈 시, 출차 시 호출)
    stopCostRefreshTimer() {
      //왜 if로 먼저 확인하는지 : 취소할 타이머가 실제로 있는지 확인
      if (this.costRefreshTimerId) {
        //값이 들어있는지 확인, 지울 타이머가 실제로 있는지" 미리 확인
        clearTimeout(this.costRefreshTimerId); //타이머를 멈추는 핵심 동작
        this.costRefreshTimerId = null; //타이머를 껐으니, ID 값도 비워서 초기 상태로 되돌림.
      }
    },

    // ===================== [추가] 과거 이용 내역 페이지네이션 =====================
    // [수정] 서버가 페이지별로 데이터를 내려주므로 페이지가 바뀔 때마다 다시 조회해야 함.
    // '이전' 버튼 클릭 시 실행. 1페이지보다 앞으로는 못 가도록 방어.
    goToHistoryPrevPage() {
      if (this.historyPage > 1) {
        this.historyPage = this.historyPage - 1;
        this.getParkingHistory("");
      }
    },

    // '다음' 버튼 클릭 시 실행. 마지막 페이지보다 뒤로는 못 가도록 방어.
    goToHistoryNextPage() {
      if (this.historyPage < this.totalHistoryPages) {
        this.historyPage = this.historyPage + 1;
        this.getParkingHistory("");
      }
    },

    // [차종 변환 함수] carType(0=일반차, 1=전기차)을 한글 텍스트로 변환.
    getCarTypeText(carType) {
      if (carType === 1) {
        return "전기차";
      } else {
        return "일반차";
      }
    },

    // [추가] 입차 일시를 "M/D HH:mm" 형태로 합쳐서 보여주는 함수.
    // formatDate("YYYY년 M월 D일")는 너무 길어서 "입차 일시" 컬럼엔 안 맞으므로 짧은 월/일 형식으로 새로 작성.
    formatShortDateTime(dateStr) {
      if (!dateStr) return "-";
      const d = new Date(dateStr);

      // new Date()는 이상한 값이 와도 에러 없이 조용히 Invalid Date를 반환함.
      // 그 getTime()은 숫자가 아니라 NaN이 되므로, 이걸로 "제대로 변환됐는지" 확인
      // NaN = "Not a Number", 즉 "숫자가 아님"을 뜻하는 특수한 값.
      // Number.isNaN(값): 그 값이 진짜 NaN인지 확인해주는 함수예요. 맞으면 true, 아니면 false
      // d.getTime()(날짜를 숫자로 바꾼 값)이 NaN이라면
      // → **"애초에 날짜 변환 자체가 실패했다"**는 뜻이라, 그걸 걸러내는 용도로 쓰임.
      if (Number.isNaN(d.getTime())) {
        return dateStr; // 파싱 실패 → "NaN년 NaN월 NaN일" 대신 원본 문자열 그대로 보여줌
      }
      const month = d.getMonth() + 1;
      const day = d.getDate();
      const pad = (n) => String(n).padStart(2, "0"); // 숫자를 문자로 바꾼 뒤, 두 자리가 안 되면 앞에 0을 채워서 항상 두 자리로 맞춰주는 함수
      const hours = pad(d.getHours());
      const minutes = pad(d.getMinutes());
      return month + "/" + day + " " + hours + ":" + minutes;
    },

    // [날짜 포맷팅 함수] 'YYYY-MM-DD' 형태를 'YYYY년 M월 D일' 텍스트 형식으로 변환
    formatDate(dateStr) {
      if (!dateStr) return "-";
      const d = new Date(dateStr);
      if (Number.isNaN(d.getTime())) {
        return dateStr;
      } else {
        return d.getFullYear() + "년 " + (d.getMonth() + 1) + "월 " + d.getDate() + "일";
      }
    },

    // [시각 포맷팅 함수] ISO 시각 문자열을 24시간제 'HH:mm' 형식으로 변환.
    formatTime(timeStr) {
      if (!timeStr) return "-";
      const d = new Date(timeStr);
      if (Number.isNaN(d.getTime())) {
        return timeStr;
      }
      return d.toLocaleTimeString("ko-KR", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
      });
    },

    // [출차 시각 텍스트] 출차 시간이 있으면 시간으로, 없으면 '-' 표시
    // [수정] 백엔드가 camelCase(exTime)로 내려주므로 snake_case(ex_time) 대신 exTime 사용
    getExitTimeText(historyItem) {
      if (historyItem.exTime) {
        return this.formatTime(historyItem.exTime);
      } else {
        return "-";
      }
    },
    costCalc(car){
      if(car.discntAt === null || car.discntAt === ''){
      return Math.round((car.parkingMinutes - 15) / 60 * 20) * 100;
      } else {
        let totalCost = Math.round((car.parkingMinutes - 120 - 15) / 60 * 20) * 100;
        if(totalCost > 0){
          return totalCost;
        } else{
          return 0;
        }
      }
    },
    // [요금 텍스트]
    // [수정] // 정산 여부는 '상태' 컬럼이 이미 보여주므로, 여기선 '예상'/'확정' 단어로만 금액 구분
    getParkingCostText(isSettled, cost) {
      // isSettled : 정산 여부  | cost : 금액(숫자)
      if (!isSettled) {
        // cost가 없으면(undefined, 0원) 0으로 대체, toLocaleString()으로 천 단위 쉼표 추가
        return "예상 " + (cost || 0).toLocaleString() + "원";
      } else {
        return "확정 " + (cost || 0).toLocaleString() + "원";
      }
    },

    // [결제 금액 텍스트] 금액이 있으면 쉼표와 '원'을 붙여서 표시, 없으면 '0원' 처리
    getCostText(historyItem) {
      if (historyItem.cost) {
        return historyItem.cost.toLocaleString() + "원";
      } else {
        return "0원";
      }
    },

    // [추가] 회원정보 수정 화면으로 이동하는 함수.
    // - (회원정보 변경 버튼 클릭 시 수정화면으로 전환) 구현부.
    goToMemberEdit() {
      this.$router.push({ name: "myupdate" });
    },
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/member/CSS/my.css"></style>
