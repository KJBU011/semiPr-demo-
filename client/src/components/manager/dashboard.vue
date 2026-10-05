<template>
  <div class="dashboard-container">
    <div class="panel-header">
      <span class="panel-eyebrow">TODAY'S STATS</span>
      <h3 class="panel-title">통계화면</h3>
    </div>

    <div class="stat-card">
      <span class="stat-code">TODAY</span>
      <div>
        <span class="stat-value">{{ todayVisitCount }}</span
        ><span class="stat-unit">대</span>
      </div>
      <span class="stat-label">금일 방문 차량 수</span>
    </div>

    <!-- Highcharts가 그려질 자리 -->
    <div id="hourly-chart-container" class="chart-wrapper"></div>

    <h3 class="section-title">방문 차량 목록</h3>
    <table class="data-table">
      <thead>
        <tr>
          <th>번호</th>
          <th>차량번호</th>
          <th>입차시간</th>
          <th>출차시간</th>
          <th>차량위치</th>
          <th>할인지급시간</th>
        </tr>
      </thead>
      <tbody>
        <!-- recentCarList(현재 페이지 10건) 배열을 순회하며 행(tr) 생성 -->
        <tr v-for="(car, index) in recentCarList" :key="index">
          <td>{{ (currentPage - 1) * 10 + index + 1 }}</td>
          <!-- 차량번호 -->
          <td>{{ car.carNum }}</td>
          <!-- entTime(입차시간 문자열: 예 '2026-08-07 14:30:00')에서 
               11~12번째 인덱스(시), 14~15번째 인덱스(분)를 잘라내어 출력 -->
          <td>
            {{ car.entTime.substring(11, 13) }}시 {{ car.entTime.substring(14, 16) }}분
          </td>
          <!-- 출차시간(exTime)이 null이면 '-' 표시, 
               있으면 입차시간과 동일하게 '시', '분' 문자열만 잘라내어 출력 -->
          <td>
            {{
              car.exTime == null
                ? "-"
                : car.exTime.substring(11, 13) +
                  "시 " +
                  car.exTime.substring(14, 16) +
                  "분"
            }}
          </td>
          <!-- 차량 주차 위치/구역 번호 출력 -->
          <td>{{ car.spcNo }}</td>
          <!-- 할인지급시간 -->
          <td>
            {{
              car.discntAt == null
                ? "-"
                : car.discntAt.substring(11, 13) +
                  "시 " +
                  car.discntAt.substring(14, 16) +
                  "분"
            }}
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 페이지 번호 버튼 목록 영역 -->
    <div class="pagination">
      <!-- [추가] 이전 버튼: 현재 페이지 기준 1페이지 앞으로 이동 -->
      <button class="page-nav" :disabled="currentPage === 1" @click="prevPage">
        이전
      </button>

      <!-- [수정] pageNumbers(전체 페이지) → visiblePages(현재 묶음의 최대 3개 페이지 번호)로 변경 -->
      <!-- 클릭 시 showPage(page) 함수를 호출하여 해당 페이지 데이터를 로드 -->
      <!-- 현재 보고 있는 페이지(currentPage)의 버튼은 .active 클래스로 강조 -->
      <button
        v-for="page in visiblePages"
        :key="page"
        @click="showPage(page)"
        class="page-btn"
        :class="{ active: page === currentPage }"
      >
        {{ page }}
      </button>

      <!-- [추가] 다음 버튼: 현재 페이지 기준 1페이지 뒤로 이동 -->
      <button class="page-nav" :disabled="currentPage === totalPages" @click="nextPage">
        다음
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Highcharts from "highcharts";

export default {
  data() {
    return {
      todayVisitCount: 0, // 오늘 방문한 총 차량 수
      todayCarList: [], // 서버에서 받아온 오늘 전체 차량 데이터 배열
      hourlyCounts: new Array(24).fill(0), // 0시~23시까지 시간대별 카운트 (24개 원소를 0으로 초기화)
      recentCarList: [], // 화면에 실제로 보여줄 현재 페이지 10건
      currentPage: 1, // 지금 몇 페이지 보고 있는지
      totalPages: 0, // 전체 페이지 수
      pageGroupSize: 3, // [수정] pageNumbers(전체 페이지 배열) 삭제 → 한 번에 보여줄 페이지 번호 개수(3)로 교체
    };
  },
  computed: {
    // [추가] 현재 페이지가 속한 그룹의 시작 번호 (예: 4페이지 -> 4, 5페이지 -> 4, 6페이지 -> 4, 7페이지 -> 7)
    pageGroupStart() {
      return (
        Math.floor((this.currentPage - 1) / this.pageGroupSize) * this.pageGroupSize + 1
      );
    },
    // [추가] 화면에 실제로 보여줄 페이지 번호 목록 (최대 pageGroupSize개)
    visiblePages() {
      const start = this.pageGroupStart;
      const end = Math.min(start + this.pageGroupSize - 1, this.totalPages);
      const list = [];
      for (let i = start; i <= end; i++) {
        list.push(i);
      }
      return list;
    },
  },
  methods: {
    // 서버에 데이터 요청하는 함수
    getTodayCars() {
      axios
        .get("/gettodayvisitcount", {
          params: { ownerId: JSON.parse(sessionStorage.getItem("login")).id },
        })
        .then((resp) => {
          // 응답 데이터를 전체 차량 리스트에 저장
          this.todayCarList = resp.data;
          // 전체 차량 개수 저장
          this.todayVisitCount = resp.data.length;

          this.calcHourlyCounts(); // 시간대별 집계 계산
          this.calcTotalPages(); // 전체 페이지 수 계산
          // [삭제] calcPageNumbers() 호출 제거 — visiblePages computed가 대체
          this.showPage(1); // 1페이지 데어 잘라서 보여주기
          this.drawChart(); // 차트생성
        })
        .catch((err) => {
          alert(err);
        });
    },
    // 시간대별(0~23시) 방문 차량 수 계산
    calcHourlyCounts() {
      // 0으로 채워진 크기 24의 새로운 배열 생성
      let counts = new Array(24).fill(0);
      for (let i = 0; i < this.todayCarList.length; i++) {
        // entTime 문자열에서 '시' 부분만 정수(parseInt)로 추출 (예: '14' -> 14)
        const hour = parseInt(this.todayCarList[i].entTime.substring(11, 13));
        // 해당 시간대의 카운트 1 증가
        counts[hour]++;
      }
      // 계산된 결과를 hourlyCounts 데이터 변수에 반영
      this.hourlyCounts = counts;
    },

    // 전체 개수로 총 필요헌 페이지 수 계산
    calcTotalPages() {
      let total = this.todayCarList.length;
      let pages = parseInt(total / 10); // 10개씩 나눈 몫 계산

      // 10으로 나누어 떨어지지 않으면 1페이지 추가
      if (total % 10 !== 0) {
        pages = pages + 1;
      }
      // 데이터가 0개여도 최소 1페이지는 유지
      if (pages === 0) {
        pages = 1;
      }

      this.totalPages = pages;
    },

    // [추가] 이전 페이지로 이동 (현재 페이지 기준 1페이지씩)
    // pageGroupStart는 currentPage를 기준으로 계산되므로, currentPage만 1씩 바꿔도
    // 보여지는 3개 번호 묶음은 자동으로 따라 움직임 (예: 3페이지→2페이지 이동 시 "1 2 3"으로 전환)
    prevPage() {
      if (this.currentPage > 1) {
        this.showPage(this.currentPage - 1);
      }
    },

    // [추가] 다음 페이지로 이동 (현재 페이지 기준 1페이지씩)
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.showPage(this.currentPage + 1);
      }
    },

    // 선택한 페이지에서 10개만 잘라서 보여주기
    showPage(page) {
      this.currentPage = page; // 현재 페이지 업데이트

      let start = (page - 1) * 10; // 시작 인덱스 (예: 1페이지면 0)
      let end = start + 10; // 끝 인덱스

      let result = [];
      // 전체 리스트의 범위를 벗어나지 않도록 조건 설정 후 10개 추출
      for (let i = start; i < end && i < this.todayCarList.length; i++) {
        result.push(this.todayCarList[i]);
      }

      // 화면 테이블에 연결된 배열 업데이트
      this.recentCarList = result;
    },

    drawChart() {
      // [추가] axios 응답이 늦게 도착했을 때, 그 사이 다른 탭(포인트 관리/차량 정보 조회)으로
      // 전환되어 이 컴포넌트가 이미 언마운트된 경우 container가 DOM에서 사라져 있음.
      // 그 상태로 Highcharts.chart()를 호출하면 "Highcharts error #13"이 발생하므로,
      // container가 실제로 존재할 때만 차트를 그리도록 방어 코드 추가.
      const container = document.getElementById("hourly-chart-container");
      if (!container) {
        return;
      }

      let categories = [];
      for (let i = 0; i < 24; i++) {
        categories.push(i + "시");
      }

      Highcharts.chart("hourly-chart-container", {
        chart: {
          type: "column",
        },
        title: {
          text: "시간대별 주차 등록 건수",
        },
        xAxis: {
          categories: categories,
          labels: {
            step: 1,
            rotation: 0,
            style: {
              fontSize: "10px",
            },
          },
        },
        yAxis: {
          title: {
            text: "건수",
            rotation: 0,
          },
          allowDecimals: false,
        },
        series: [
          {
            name: "방문 차량",
            data: this.hourlyCounts,
          },
        ],
      });
    },
  },
  mounted() {
    this.getTodayCars();
  },
};
</script>

<style src="@/components/basic/css/common.css"></style>
<style src="@/components/manager/css/data-table.css"></style>
<style src="@/components/manager/css/dashboard.css"></style>
