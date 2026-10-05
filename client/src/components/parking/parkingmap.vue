<template>
  <!-- 전체 층을 옆으로 나란히 배치할 감싸는 부모 컨테이너 -->
  <div
    class="parking-map"
    style="
      display: flex;
      flex-direction: row;
      gap: 30px;
      justify-content: center;
      align-items: flex-start;
    "
  >
    <!-- 1층, 2층, 3층 루프 -->
    <div
      v-for="floor in floors"
      :key="floor"
      style="display: flex; flex-direction: column; align-items: center"
    >
      <!--지하 추가-->
      <h3>B{{ floor }}F</h3>

      <!-- 한 층에 표 3개 -->
      <table
        v-for="(parkingTable, tableIndex) in getFloorTables(floor)"
        :key="tableIndex"
        border="1"
        style="margin-bottom: 20px"
      >
        <tbody>
          <!-- 한 표에 행 2개 -->
          <tr v-for="(row, rowIndex) in parkingTable" :key="rowIndex">
            <!-- 한 행에 주차면 5개 -->
            <td
              v-for="space in row"
              :key="space.spcNo"
              :class="getSpaceClass(space.spcStat)"
            >
              <div>{{ space.spcNo }}</div>
              <div>{{ getSpaceType(space.spcType) }}</div>
              <div>{{ getSpaceStatus(space.spcStat) }}</div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "parkingmap",
  data() {
    return {
      floors: [1, 2, 3],
      parkingMap: [],
    };
  },

  mounted() {
    this.getParkingMap();
  },

  methods: {
    getParkingMap() {
      axios
        .get("/parkingmap")
        .then((response) => {
          this.parkingMap = response.data;
          console.log(this.parkingMap);
        })
        .catch((error) => {
          console.log(error);
        });
    },

    getFloorTables(floor) {
      let tableList = [];
      let rowList = [];
      let row = [];

      for (let i = 0; i < this.parkingMap.length; i++) {
        let space = this.parkingMap[i];

        if (space.floor === floor) {
          row.push(space);

          // 주차면 5개가 모이면 한 줄 완성
          if (row.length === 5) {
            rowList.push(row);
            row = [];
          }

          // 두 줄이 모이면 표 하나 완성
          if (rowList.length === 2) {
            tableList.push(rowList);
            rowList = [];
          }
        }
      }

      return tableList;
    },

    getSpaceType(spcType) {
      if (spcType === 0) {
        return "일반";
      } else if (spcType === 1) {
        return "전기차";
      } else if (spcType === 2) {
        return "장애인";
      } else {
        return "확인 불가";
      }
    },

    getSpaceStatus(spcStat) {
      if (spcStat === 0) {
        return "비어 있음";
      } else if (spcStat === 1) {
        return "사용 중";
      } else {
        return "확인 불가";
      }
    },

    getSpaceClass(spcStat) {
      if (spcStat === 1) {
        return "used";
      } else {
        return "empty";
      }
    },
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/parking/CSS/parkingmap.css"></style>
