<script setup>
import axios from "axios";
</script>

<template>
  <div id="admincarhistory-page">
    <!-- 검색 영역 -->
    <div class="adminsrch">
      <select v-model="category" class="form-select">
        <option
          v-for="(option, index) in options"
          v-bind:value="option.value"
          v-bind:key="index"
        >
          {{ option.text }}
        </option>
      </select>

      <div class="search-input-area">
        <input
          v-model="keyword"
          size="45"
          placeholder="검색어 입력"
          class="form-control"
        />
      </div>

      <button @click="searchBtn()" class="btn btn-primary">검색</button>
    </div>

    <!-- 테이블 -->
    <div class="table-scroll">
      <table border="1">
        <colgroup>
          <col width="50" />
          <col width="100" />
          <col width="70" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="150" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
        </colgroup>

        <thead>
          <tr>
            <th>번호</th>
            <th>차량번호</th>
            <th>주차위치</th>
            <th>차량유형</th>
            <th>차량상태</th>
            <th>입차시간</th>
            <th>출차시간</th>
            <th>할인여부</th>
            <th>요금</th>
            <th>차주</th>
            <th>차주아이디</th>
            <th>입차날짜</th>
            <th>출차날짜</th>
          </tr>
        </thead>

        <tbody v-if="carlist != []" v-for="(car, index) in carlist" :key="index">
          <tr>
            <th>
              {{ index + (pageNum - 1) * 10 + 1 }}
            </th>

            <td>
              {{ car.carNum }}
            </td>

            <td>
              {{ car.spcNo }}
            </td>

            <td v-if="car.carType == 1">전기 차량</td>

            <td v-if="car.carType == 0">일반 차량</td>

            <td>
              {{ statText(car.carStat) }}
            </td>

            <td>
              {{ timeText(car.entTime) }}
            </td>

            <td>
              {{ timeText(car.exTime) }}
            </td>

            <td v-if="car.discntAt != null && car.discntAt.trim() != ''">적용</td>

            <td v-if="car.discntAt == null || car.discntAt.trim() == ''">미적용</td>

            <td>
              {{ costText(car.cost) }}
            </td>

            <td>
              {{ car.name }}
            </td>

            <td>
              {{ car.id }}
            </td>

            <td>
              {{ dateText(car.entTime) }}
            </td>

            <td>
              {{ dateText(car.exTime) }}
            </td>
          </tr>
        </tbody>

        <tbody v-if="carlist == ''">
          <tr>
            <td colspan="13">검색 결과가 없습니다</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 페이지 번호 -->
    <!-- [수정] 기존 div.pagination → 이전/다음 버튼 추가, v-for 대상을 pages → visiblePages로 변경 (한 번에 3페이지씩만 노출) -->
    <div class="pagination">
      <!-- [추가] 이전 페이지 묶음 버튼 -->
      <button class="page-nav" :disabled="pageNum === 1" @click="prevPage">이전</button>

      <a v-if="carlist != ''" v-for="n in visiblePages" :key="n">
        <strong v-if="pageNum == n" class="pageSel">
          {{ n }}
        </strong>

        <strong v-if="pageNum != n" @click="pageClick(n)" class="pageDis">
          {{ n }}
        </strong>
      </a>

      <!-- [추가] 다음 페이지 묶음 버튼 -->
      <button class="page-nav" :disabled="pageNum === pages" @click="nextPage">
        다음
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "admincarhistory",

  data() {
    return {
      pageNum: 1,

      carlist: [],

      category: "start",

      keyword: "",

      options: [
        {
          text: "선택",
          value: "start",
        },
        {
          text: "차량번호",
          value: "carNum",
        },
        {
          text: "이름",
          value: "name",
        },
        {
          text: "아이디",
          value: "id",
        },
      ],

      pages: 0,
      pageGroupSize: 3, // [추가] 한 번에 보여줄 페이지 번호 개수
    };
  },

  mounted() {
    this.getList();
  },

  computed: {
    // [추가] 현재 페이지가 속한 그룹의 시작 번호
    pageGroupStart() {
      return Math.floor((this.pageNum - 1) / this.pageGroupSize) * this.pageGroupSize + 1;
    },
    // [추가] 화면에 실제로 보여줄 페이지 번호 목록 (최대 pageGroupSize개)
    visiblePages() {
      const start = this.pageGroupStart;
      const end = Math.min(start + this.pageGroupSize - 1, this.pages);
      const list = [];
      for (let i = start; i <= end; i++) {
        list.push(i);
      }
      return list;
    },
  },

  methods: {
    getList() {
      const param = {
        params: {
          pageNum: this.pageNum,

          category: this.category,

          keyword: this.keyword,
        },
      };

      axios
        .get("/exitcarlist", param)
        .then((resp) => {
          this.carlist = resp.data;
        })
        .catch((err) => {
          alert(err);
        });

      axios
        .get("/exitcarcount", param)
        .then((resp) => {
          this.pages = Math.ceil(resp.data / 10) || 1;
        })
        .catch((err) => {
          alert(err);
        });
    },

    pageClick(page) {
      this.pageNum = page;

      this.getList();
    },

    // [수정] 그룹 단위 이동 → 페이지 단위 이동(한 페이지씩)으로 변경.
    // pageGroupStart는 pageNum을 기준으로 계산되므로, pageNum만 1씩 바꿔도
    // 보여지는 3개 번호 묶음은 자동으로 따라 움직임
    prevPage() {
      if (this.pageNum > 1) {
        this.pageNum -= 1;
        this.getList();
      }
    },

    nextPage() {
      if (this.pageNum < this.pages) {
        this.pageNum += 1;
        this.getList();
      }
    },

    searchBtn() {
      this.pageNum = 1;

      this.getList();
    },

    statText(stat) {
      let statStr = "";

      if (stat === 2) {
        statStr = "출차";
      }

      return statStr;
    },

    timeText(time) {
      let hourStr = time.substring(11, 13);

      let minuteStr = time.substring(14, 16);

      return hourStr + ":" + minuteStr;
    },

    costText(cost) {
      if (cost == -1) {
        return "면제(관리자)";
      } else if (cost == -2) {
        return "면제(점주)";
      }

      return cost.toLocaleString() + "원";
    },

    dateText(time) {
      return time.substring(0, 10);
    },
  },
};
</script>

<style src="@/components/basic/css/common.css"></style>
<style src="@/components/admin/css/admintable.css"></style>
