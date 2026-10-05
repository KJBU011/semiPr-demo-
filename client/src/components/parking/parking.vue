<template>
  <div class="parking-search">
    <h2>차량번호 검색</h2>

    <!-- 차량번호 입력창 + 검색 버튼 (가운데 정렬용 wrapper) -->
    <div class="search-bar">
      <input
        v-model="carNum"
        maxlength="4"
        placeholder="차량번호를 입력하세요 (예: 1234)"
        @keyup.enter="searchCar"
      />

      <!-- [검색 버튼] 클릭 시 searchCar() -->
      <button @click="searchCar">검색</button>
    </div>

    <hr />

    <!-- 검색 결과가 없을 때 화면-->
    <!-- 조건: 검색 버튼을 눌렀고(searched === true)
       결과 배열이 비어있을 때(resultList.length === 0) -->
    <div v-if="searched && resultList.length === 0" align="center">검색 결과가 없습니다.</div>

    <!-- 검색 결과가 있을 때 / resultList를 순회하며 개별 차량 정보(car)를 보여줌 -->
    <div v-for="car in resultList" :key="car.carNum" class="car-card">
      <!-- 주차장 좌석표: 검색하면 표시 -->
      <!-- <table v-if="searched" border="1" style="margin-top: 20px;">
        <tbody v-for="(row, index) in parkingRows" :key="index"
             :class="{ 'group-gap': index === 1 || index === 3 }">
          <tr>
            <td v-for="(spot, sIndex) in row"> :key="sIndex"
                class="park" 
            </td>
          </tr>
        </tbody>
      </table> -->

      <!-- 검색 버튼 클릭 시 나오는 화면 -->
      <div class="car-header">
        <div class="big-info">
          <h2 class="car-num">{{ car.carNum }}</h2>
          <!-- 차량번호 -->
          <h3 class="spc-no">{{ car.spcNo }}</h3>
          <!-- 주차구역 -->
        </div>

        <!-- 상세정보 열기/닫기 버튼
         - @click: 클릭 시 toggleDetail(car)을 호출하여 해당 차량의 showDetail 상태 전환
         - 삼항 연산자: car.showDetail이 true면 '상세정보 닫기', false면 '상세정보 보기' 텍스트 출력 -->
        <button @click="toggleDetail(car)">
          {{ car.showDetail ? "상세정보 닫기" : "상세정보 보기" }}
        </button>
      </div>

      <!-- 상세정보 보기 클릭 시 
       - v-if="car.showDetail": 클릭해서 showDetail이 true가 될 때만 표를 화면에 출력-->
      <table
        v-if="car.showDetail"
        class="detail-info"
        border="1"
        style="width: 70%; text-align: center; table-layout: fixed"
      >
        <thead>
          <tr>
            <th>요금</th>
            <th>할인 적용</th>
            <th>상태</th>
          </tr>
        </thead>

        <tbody>
          <tr>
            <td>{{ costCalc(car).toLocaleString() }}원</td>
            <!-- .toLocaleString() 천단위 콤마 기능-->

            <td>
              <span v-if="car.discntAt" class="discounted">- 2시간</span>
              <span v-else class="normal">할인 미적용</span>
            </td>

            <td>
              <span v-if="car.carStat === 0 || car.carStat === 1"
                >현재 주차 중입니다</span
              >
              <span v-else-if="car.carStat === 2">정산 후 출차해주세요</span>
              <span v-else-if="car.carStat === 3">정산 완료</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div>
      <parkingmap />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import parkingmap from "./parkingmap.vue";

export default {
  name: "Parking",

  components: {
    parkingmap,
  },

  // 변수들 초기값 설정
  data() {
    return {
      carNum: "", // 사용자가 입력창에 타이핑하는 차량번호
      resultList: [], // 검색 결과 (여러 대 나올 수 있으니 배열)
      searched: false, // 검색을 한 번이라도 했는지
    };
  },
  methods: {
    searchCar() {
      // 유효성 검사. (1) 비어있는지
      if (!this.carNum.trim()) {
        alert("차량번호를 입력해 주세요");
        return;
      }

      // (2) 4자리인지 체크
      if (this.carNum.length !== 4) {
        alert("차량번호 4자리 숫자를 입력해주세요");
        return;
      }

      // (3) 아스키코드로 각 글자가 숫자인지 반복문으로 검사
      for (let i = 0; i < this.carNum.length; i++) {
        const code = this.carNum.charCodeAt(i); // i번째 글자의 아스키코드
        if (code < 48 || code > 57) {
          // 48~57(숫자) 범위 벗어나면
          alert("차량번호 4자리 숫자를 입력해주세요");
          return;
        }
      }

      /* 백엔드 완성되면 아래 주석 풀고 위에 더미 데이터 부분은 지우기 */
      axios
        .get("/getcarnum", {
          params: { carNum: this.carNum },
        })
        .then((resp) => {
          // 백엔드 통신 성공 시
          this.resultList = resp.data; // 서버에서 전달받은 데이터로 목록 업데이트
          this.searched = true; // 검색 수행 상태 true 설정
          this.carNum = ""; // ← 여기(.then 안)에 넣어야 함
        })
        .catch((err) => {
          // 백엔드 통신 실패 시
          console.error(err);
        });
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
    toggleDetail(car) {
      car.showDetail = !car.showDetail;
    },
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/parking/CSS/parking.css"></style>
