<template>
  <div id="admindash-page">
    <div class="panel-header">
      <span class="panel-eyebrow">PARKING OVERVIEW</span>
      <h2 class="panel-title">주차장 대시보드</h2>
    </div>

    <div v-if="totalLoaded" class="dash-stats">
      <span class="dash-stat"
        ><strong>{{ totalParking.total_spc }}</strong
        >대 · 총 주차면</span
      >
      <span class="dash-stat"
        ><strong>{{ totalParking.parking_car }}</strong
        >대 · 사용 중</span
      >
      <span class="dash-stat"
        ><strong>{{ totalParking.available_spc }}</strong
        >대 · 빈자리</span
      >
    </div>

    <hr class="dash-divider" />

    <div class="dash-grid">
      <div class="dash-cell">
        <h3 class="section-title">층별 유형별 주차 가능 대수</h3>

        <div class="bar-chart">
          <Bar v-if="floorLoaded" :data="floorChartData" :options="barOptions" />
        </div>
      </div>

      <div class="dash-cell doughnut-item">
        <h3>전체 주차 현황</h3>

        <div class="doughnut-chart">
          <Doughnut
            v-if="totalLoaded"
            :data="parkingChartData"
            :options="doughnutOptions"
          />
        </div>
      </div>

      <div class="dash-cell doughnut-item">
        <h3>전기차 주차구역 현황</h3>

        <div class="doughnut-chart">
          <Doughnut
            v-if="mapLoaded"
            :data="electricChartData"
            :options="doughnutOptions"
          />
        </div>
      </div>

      <div class="dash-cell doughnut-item">
        <h3>장애인 주차구역 현황</h3>

        <div class="doughnut-chart">
          <Doughnut
            v-if="mapLoaded"
            :data="disabledChartData"
            :options="doughnutOptions"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { Bar, Doughnut } from "vue-chartjs";

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Tooltip, Legend);

export default {
  name: "admin",
  components: {
    Bar,
    Doughnut,
  },

  data() {
    return {
      floorCount: [],

      parkingSpaces: [],

      totalParking: {
        total_spc: 0,
        parking_car: 0,
        available_spc: 0,
      },

      floorLoaded: false,
      totalLoaded: false,
      mapLoaded: false,

      barOptions: {
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

        plugins: {
          legend: {
            position: "bottom",
          },
        },
      },

      doughnutOptions: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: "68%",

        plugins: {
          legend: {
            position: "bottom",
          },
        },
      },
    };
  },

  computed: {
    floorChartData() {
      return {
        labels: this.floorCount.map((item) => {
          return item.floor > 0 ? item.floor + "F" : "B" + Math.abs(item.floor);
        }),

        datasets: [
          {
            label: "일반",
            data: this.floorCount.map((item) => item.normal_spc ?? 0),
            backgroundColor: "#2C7296" /* [수정] #1679AB보다 채도 살짝 낮춤 */,
            borderColor: "#2C7296",
            borderWidth: 1,
            borderRadius: 4,
          },
          {
            label: "전기차",
            data: this.floorCount.map((item) => item.elec_spc ?? 0),
            backgroundColor: "#5DEBD7",
            borderColor: "#5DEBD7",
            borderWidth: 1,
            borderRadius: 4,
          },
          {
            label: "장애인",
            data: this.floorCount.map((item) => item.dis_spc ?? 0),
            backgroundColor: "#C5FF95",
            borderColor: "#C5FF95",
            borderWidth: 1,
            borderRadius: 4,
          },
        ],
      };
    },

    parkingChartData() {
      return {
        labels: ["사용 중", "빈자리"],

        datasets: [
          {
            data: [
              this.totalParking.parking_car ?? 0,
              this.totalParking.available_spc ?? 0,
            ],
            backgroundColor: ["#748DAE", "#9ECAD6"],
            borderColor: "#ffffff",
            borderWidth: 2,
          },
        ],
      };
    },

    electricChartData() {
      const electricSpaces = this.parkingSpaces.filter((item) => {
        return Number(item.spc_type ?? item.spcType) === 1;
      });

      const usingCount = electricSpaces.filter((item) => {
        return Number(item.spc_stat ?? item.spcStat) === 1;
      }).length;

      const availableCount = electricSpaces.filter((item) => {
        return Number(item.spc_stat ?? item.spcStat) === 0;
      }).length;

      return {
        labels: ["사용 중", "빈자리"],

        datasets: [
          {
            data: [usingCount, availableCount],
            backgroundColor: ["#748DAE", "#9ECAD6"],
            borderColor: "#ffffff",
            borderWidth: 2,
          },
        ],
      };
    },

    disabledChartData() {
      const disabledSpaces = this.parkingSpaces.filter((item) => {
        return Number(item.spc_type ?? item.spcType) === 2;
      });

      const usingCount = disabledSpaces.filter((item) => {
        return Number(item.spc_stat ?? item.spcStat) === 1;
      }).length;

      const availableCount = disabledSpaces.filter((item) => {
        return Number(item.spc_stat ?? item.spcStat) === 0;
      }).length;

      return {
        labels: ["사용 중", "빈자리"],

        datasets: [
          {
            data: [usingCount, availableCount],
            backgroundColor: ["#748DAE", "#9ECAD6"],
            borderColor: "#ffffff",
            borderWidth: 2,
          },
        ],
      };
    },
  },

  mounted() {
    this.getFloorCount();
    this.getTotalParkingCount();
    this.getParkingMap();
  },

  methods: {
    getFloorCount() {
      axios
        .get("/getfloorcount")
        .then((response) => {
          this.floorCount = response.data;
          this.floorLoaded = true;
        })
        .catch((error) => {
          console.log(error);
        });
    },

    getTotalParkingCount() {
      axios
        .get("/gettotalparkingcount")
        .then((response) => {
          this.totalParking = response.data;
          this.totalLoaded = true;
        })
        .catch((error) => {
          console.log(error);
        });
    },

    getParkingMap() {
      axios
        .get("/parkingmap")
        .then((response) => {
          this.parkingSpaces = response.data;
          this.mapLoaded = true;
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/manager/CSS/data-table.css"></style>
<style src="@/components/admin/CSS/admindash.css"></style>
