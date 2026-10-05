<template>
  <div id="admincarbar-page" class="stat-container">
    <!-- 시간대별 보기일 때 뒤로가기 -->
    <div class="header-area">
      <button v-if="viewMode === 'hour'" class="back-btn" @click="backToDayView">
        ← 전체 일별 통계로 돌아가기
      </button>
    </div>

    <!-- 일별 차트 -->
    <div v-show="viewMode === 'day'" class="chart-wrapper">
      <h3>일자별 방문 차량 수</h3>

      <span class="guide-text">
        (날짜를 클릭하면 해당 날짜의 시간대별 통계를 확인합니다)
      </span>

      <div class="canvas-container">
        <canvas ref="dayCanvas"></canvas>
      </div>
    </div>

    <!-- 시간대별 차트 -->
    <div v-show="viewMode === 'hour'" class="chart-wrapper">
      <h3>[{{ selectedDate }}] 시간대별 방문 차량 수</h3>

      <div class="canvas-container">
        <canvas ref="hourCanvas"></canvas>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { Chart, registerables } from "chart.js";

Chart.register(...registerables);

export default {
  name: "admincarbar",

  data() {
    return {
      viewMode: "day",

      selectedDate: "",

      dayList: [],

      hourList: [],

      dayChartInstance: null,

      hourChartInstance: null,

      dayOfWeekNames: ["일", "월", "화", "수", "목", "금", "토"],
    };
  },

  mounted() {
    this.getDayList();
  },

  methods: {
    getDayList() {
      axios

        .get("/daystat")

        .then((resp) => {
          this.dayList = resp.data;

          // [수정] 데이터가 도착하자마자 바로 그리면, 부모 flex 레이아웃
          // (.dashboard-top-row)이 아직 자리잡기 전 크기를 Chart.js가
          // 참조해서 실제보다 크게 렌더링되는 경우가 있었음.
          // nextTick으로 한 틱 늦춰서 레이아웃이 안정된 뒤에 그리도록 함.
          this.$nextTick(() => {
            this.renderDayChart();
          });
        })

        .catch((err) => {
          console.error("일별 통계 실패:", err);
        });
    },

    renderDayChart() {
      const labels = this.dayList.map((item) => {
        const dowName = this.dayOfWeekNames[item.dow] || "";

        const monthDay = item.stat_date ? item.stat_date.slice(-5) : "";

        return `${monthDay} (${dowName})`;
      });

      const dataValues = this.dayList.map((item) => item.cnt);

      if (this.dayChartInstance) {
        this.dayChartInstance.destroy();
      }

      const ctx = this.$refs.dayCanvas.getContext("2d");

      this.dayChartInstance = new Chart(ctx, {
        type: "bar",

        data: {
          labels: labels,

          datasets: [
            {
              label: "일별 방문 차량 수 (대)",

              data: dataValues,

              backgroundColor: "rgba(75, 192, 192, 0.6)",

              borderColor: "rgba(75, 192, 192, 1)",

              borderWidth: 1,

              borderRadius: 4,

              barThickness: 24,

              maxBarThickness: 30,

              hoverBackgroundColor: "rgba(255, 99, 132, 0.8)",
            },
          ],
        },

        options: {
          responsive: true,

          maintainAspectRatio: false,

          onClick: (event, elements) => {
            if (elements.length > 0) {
              const clickedIndex = elements[0].index;

              const clickedData = this.dayList[clickedIndex];

              this.getHourListByDate(clickedData.stat_date);
            }
          },

          scales: {
            y: {
              beginAtZero: true,

              ticks: {
                stepSize: 1,
              },
            },
          },
        },
      });

      // [추가] 생성 직후 한 번 더 강제로 resize() 호출.
      // 최초 계산이 부모 크기를 잘못 참조했더라도, 이 시점엔 레이아웃이
      // 확정되어 있으므로 컨테이너(canvas-container 150px)에 맞게 다시 맞춰짐.
      this.dayChartInstance.resize();
    },

    getHourListByDate(date) {
      this.selectedDate = date;

      axios

        .get("/hourstat", {
          params: {
            date: date,
          },
        })

        .then((resp) => {
          this.hourList = resp.data;

          this.viewMode = "hour";

          this.$nextTick(() => {
            this.renderHourChart();
          });
        })

        .catch((err) => {
          console.error("시간대별 통계 실패:", err);
        });
    },

    renderHourChart() {
      const labels = this.hourList.map((item) => `${item.hour}시`);

      const dataValues = this.hourList.map((item) => item.cnt);

      if (this.hourChartInstance) {
        this.hourChartInstance.destroy();
      }

      const ctx = this.$refs.hourCanvas.getContext("2d");

      this.hourChartInstance = new Chart(ctx, {
        type: "bar",

        data: {
          labels: labels,

          datasets: [
            {
              label: `${this.selectedDate} 시간대별 주차 수 (대)`,

              data: dataValues,

              backgroundColor: "rgba(54, 162, 235, 0.6)",

              borderColor: "rgba(54, 162, 235, 1)",

              borderWidth: 1,

              borderRadius: 4,

              barThickness: 16,

              maxBarThickness: 22,
            },
          ],
        },

        options: {
          responsive: true,

          maintainAspectRatio: false,

          scales: {
            y: {
              beginAtZero: true,

              ticks: {
                stepSize: 1,
              },
            },
          },
        },
      });

      this.hourChartInstance.resize();
    },

    backToDayView() {
      this.viewMode = "day";

      this.$nextTick(() => {
        this.renderDayChart();
      });
    },
  },

  beforeUnmount() {
    if (this.dayChartInstance) {
      this.dayChartInstance.destroy();
    }

    if (this.hourChartInstance) {
      this.hourChartInstance.destroy();
    }
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/admin/CSS/admincarbar.css"></style>
