<template>
  <div id="car-manager" class="manager-panel">
    <div class="panel-header">
      <span class="panel-eyebrow">CAR SEARCH</span>
      <h3 class="panel-title">차량 정보 조회</h3>
    </div>

    <!-- 검색창 -->
    <div class="search-bar">
      <input
        type="text"
        class="search-input"
        v-model="carNum"
        placeholder="차량번호를 입력하세요 (예: 1234)"
        maxlength="4"
        @keyup.enter="searchCar"
      />
      <button class="search-btn" @click="searchCar">검색</button>
    </div>

    <!-- 검색 결과가 없을 때 -->
    <p class="empty-msg" v-if="searched && resultList.length === 0">
      검색 결과가 없습니다
    </p>

    <!-- 검색 결과 리스트 -->
    <table class="data-table" v-if="resultList.length > 0">
      <thead>
        <tr>
          <th>차량 번호</th>
          <th>차량 위치</th>
          <th>입차 시각</th>
          <th>주차 경과시간</th>
          <th>할인</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="car in resultList" :key="car.carNum">
          <td>{{ car.carNum }}</td>
          <!-- 차량번호 -->
          <td>
            <span class="spc-tag">{{ car.spcNo }}</span>
          </td>
          <!-- 차량위치 -->
          <td>
            {{ car.entTime.substring(11, 13) }}시 {{ car.entTime.substring(14, 16) }}분
          </td>
          <!-- 입차시각 -->
          <td>{{ car.parkingMinutes }}분</td>
          <!-- 주차경과(누적)시간 -->
          <td>
            <button
              class="discount-btn"
              v-if="car.discntAt == null"
              @click="discount(car.carId)"
            >
              할인
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      carNum: "", // 입력창에 입력하는 뒤 4자리
      searched: false, // 검색 버튼을 눌렀는지 여부
      resultList: [], // 검색된 차량 (검색 결과 배열)
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

      axios
        .get("/getcarnum", { params: { carNum: this.carNum } })
        .then((resp) => {
          //alert('success');
          //alert(JSON.stringify(resp.data));
          this.resultList = resp.data;
        })
        .catch((err) => {
          alert(err);
        });

      // // 임시 더미 데이터
      // const dummyCars = [
      //     {
      //         carNum: "12가3456",
      //         spcNo: "B2-15",
      //         ent_time: "2026-08-03 10:05:45.003518",
      //         car_stat: 1,
      //         discnt_at: null,
      //         parkingMinutes:35
      //     },
      //     {
      //         carNum: "36가3456",
      //         spcNo: "B3-15",
      //         ent_time: "2026-08-03 10:05:45.003518",
      //         car_stat: 1,
      //         discnt_at: '2026-08-03 10:06:45.003518',
      //         parkingMinutes:45
      //     }
      // ];

      this.searched = true;
      this.carNum = ""; // 검색 후 입력창 초기화
    },
    discount(cid) {
      // alert(JSON.parse(sessionStorage.getItem('login')).id);
      let param = {
        params: {
          carId: cid,
          ownerId: JSON.parse(sessionStorage.getItem("login")).id,
        },
      };

      axios
        .post("/applydiscount", null, param)
        .then((resp) => {
          // alert('success');
          // alert(resp.data);
          if (resp.data) {
            alert("할인이 적용되었습니다");
            location.reload();
          } else {
            alert("할인 적용에 실패했습니다");
          }
        })
        .catch((err) => {
          alert(err);
        });
    },

    /*
        // 여기부터 더미 데이터 대신 axios로 교체
        axios.get(`/car/search`, {
            params: { carNum: this.carNum }
        })
        .then((res) => {
            // 백엔드 통신 성공 시
            this.resultList = res.data;   // 서버에서 준 데이터로 교체
            this.searched = true;
            this.carNum = "";
        })
        .catch((err) => {
            // 백엔드 통신 실패 시
            console.error("차량 검색 실패:", err);
            alert("검색 도중 오류가 발생했습니다.");
        });
        */
  },
};
</script>

<style src="@/components/basic/css/common.css"></style>
<style src="@/components/manager/css/data-table.css"></style>
<style src="@/components/manager/css/carmanage.css"></style>
