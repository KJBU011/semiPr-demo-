<template>
  <div id="admincar-page">
    <div class="admincar-title-area">
      <h1>차량 관리 메뉴</h1>
    </div>

    <!-- 현재 주차 가능 대수 + 방문 차량 통계를 좌우로 배치 -->
    <div class="dashboard-top-row">
      <section class="parking-summary-card">
        <div class="parking-summary-title">현재 주차 가능 대수</div>
        <div class="parking-summary-number">
          <strong>{{ emptySpc }}</strong>
          <span>대</span>
        </div>
        <div class="parking-summary-total">전체 {{ totalSpc }}대</div>
      </section>

      <section class="admincar-section admincar-chart-section">
        <div class="admincar-section-title">
          <h2>방문 차량 통계</h2>
        </div>
        <admincarbar />
      </section>
    </div>

    <!-- 현재 입차 차량 현황 -->
    <section class="admincar-section">
      <div class="admincar-section-title">
        <h2>현재 입차 차량 현황</h2>
      </div>
      <admincarnow />
    </section>

    <!-- 과거 차량 방문 내역 -->
    <section class="admincar-section">
      <div class="admincar-section-title">
        <h2>과거 차량 방문 내역</h2>
      </div>
      <admincarhistory />
    </section>
  </div>
</template>

<script>
// import
import axios from "axios";
import admincarhistory from "./admincarhistory.vue";
import admincarnow from "./admincarnow.vue";
import admincarbar from "./admincarbar.vue";

export default {
  // name 설정 (admin.vue 에서 component 로 사용 위함)
  name: "admincar",
  // component 설정
  components: {
    admincarhistory,
    admincarnow,
    admincarbar,
  },

  // 변수 설정
  data() {
    return {
      totalSpc: 0, // 총 주차 공간
      usingSpc: 0, // 사용 중인 주차 공간
      emptySpc: 0, // 빈 주차 공간
    };
  },

  // 페이지 로딩시 실행
  mounted() {
    this.parkSpace(); // parking table 에서 현재 주차 상황 불러오기
    this.syncSpace();
  },

  // 메소드
  methods: {
    // 현재 주차 상황 불러오기
    parkSpace() {
      axios
        .get("/gettotalparkingcount")
        .then((resp) => {
          // alert(resp.data);
          this.totalSpc = resp.data.total_spc; // 총 주차 공간
          this.usingSpc = resp.data.parking_car; // 사용 중인 주차 공간
          this.emptySpc = resp.data.available_spc; // 빈 주차 공간
          //alert(this.totalSpc)
        })
        .catch((err) => {
          console.error(err);
        });
    },
    syncSpace(){
      axios.post('/sync')
            .then(resp=>{})
            .catch(err=>{
              console.error(err);
            })
    },
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/admin/CSS/admincar.css"></style>
