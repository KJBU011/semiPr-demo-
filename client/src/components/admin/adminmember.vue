<template>
  <div id="adminmember-page">
    <h1>회원 관리 메뉴</h1>
    <!-- [삭제] 제목 아래 구분선(<hr />) 제거 -->

    <!-- 카테고리 -->
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
      <!-- 검색어 -->
      <input v-model="keyword" size="45" placeholder="검색어 입력" class="form-control" />
      <button @click="searchBtn()" class="btn btn-primary">검색</button>
    </div>

    <table border="1">
      <colgroup>
        <col width="50" />
        <col width="100" />
        <col width="100" />
        <col width="100" />
        <col width="100" />
        <col width="170" />
        <col width="170" />
        <col width="80" />
        <col width="80" />
      </colgroup>
      <thead>
        <tr>
          <th>번호</th>
          <th>아이디</th>
          <th>이름</th>
          <th>차량번호</th>
          <th>차량유형</th>
          <th>전화번호</th>
          <th>이메일</th>
          <th>회원 수정</th>
          <th>회원 삭제</th>
        </tr>
      </thead>
      <tbody v-if="memberlist != []" v-for="(member, index) in memberlist" :key="index">
        <tr class="admin-member">
          <th>{{ index + (pageNum - 1) * 10 + 1 }}</th>
          <td>{{ member.id }}</td>
          <td>{{ member.name }}</td>
          <td>{{ member.carNum }}</td>
          <td v-if="member.carType == 1">전기 차량</td>
          <td v-if="member.carType == 0">일반 차량</td>
          <td>{{ member.phone }}</td>
          <td v-if="member.email != null && member.email.trim() != ''">
            {{ member.email }}
          </td>
          <td v-if="member.email == null || member.email.trim() == ''">-</td>
          <td><button @click="updateDisplay(member)">수정</button></td>
          <td><button @click="deleteMember(member)">삭제</button></td>
        </tr>
      </tbody>
      <tbody v-if="memberlist == ''">
        <tr>
          <td colspan="9">검색 결과가 없습니다</td>
        </tr>
      </tbody>
    </table>

    <!-- [수정] 기존 span → class="pagination" 부여, 이전/다음 버튼 추가 (한 번에 3페이지씩만 노출) -->
    <span class="pagination">
      <!-- [수정] 그룹 단위 이동 → 페이지 단위 이동(한 페이지씩)으로 변경 -->
      <button class="page-nav" :disabled="pageNum === 1" @click="prevPage">이전</button>

      <!-- [수정] v-for 대상을 pages(전체 페이지 수) → visiblePages(현재 묶음의 페이지 번호만) 로 변경 -->
      <a v-if="memberlist != ''" v-for="n in visiblePages" :key="n">
        <strong v-if="this.pageNum == n" class="pageSel">
          {{ n }}
        </strong>

        <strong v-if="this.pageNum != n" @click="pageClick(n)" class="pageDis">{{
          n
        }}</strong>
      </a>

      <!-- [수정] 그룹 단위 이동 → 페이지 단위 이동(한 페이지씩)으로 변경 -->
      <button class="page-nav" :disabled="pageNum === pages" @click="nextPage">
        다음
      </button>
    </span>
    <hr v-if="memdata && memdata.id" />
    <div :style="updisplay" class="update-form">
      <h2>회원 정보 수정</h2>
      <!-- [수정 2026-08-12] <table border="1"> HTML 속성 제거. CSS(border-collapse + 셀 border)로
           이미 테두리를 그리고 있는데, HTML border 속성이 테이블 바깥쪽에 브라우저 기본 테두리를
           하나 더 그려서 겹침 → 특히 마지막 버튼 행(td[colspan="2"]{border:none})은 셀 테두리가
           없어서 그 남은 바깥쪽 테두리만 두드러지게 두꺼워 보이는 원인이었음. 속성 제거로 CSS
           테두리만 남도록 통일 -->
      <table>
        <colgroup>
          <col width="100px" />
          <col width="400px" />
        </colgroup>
        <tbody>
          <tr>
            <th>아이디</th>
            <td>{{ memdata.id }}</td>
          </tr>
          <tr>
            <th>이름</th>
            <td><input v-model="updatename" :placeholder="memdata.name" /></td>
          </tr>
          <tr>
            <th>차량번호</th>
            <td><input v-model="updatecarNum" :placeholder="memdata.carNum" /></td>
          </tr>
          <tr>
            <th>차량타입</th>
            <td><input type="checkbox" v-model="memberCarType" />전기차</td>
          </tr>
          <tr>
            <th>전화번호</th>
            <td><input v-model="updatephone" :placeholder="memdata.phone" /></td>
          </tr>
          <tr>
            <th>이메일</th>
            <td><input v-model="updateemail" :placeholder="memdata.email" /></td>
          </tr>
          <tr>
            <td colspan="2">
              <button @click="updateMember">수정</button>
              &nbsp;
              <button @click="cancelUpdate">취소</button>
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
  name: "adminmember",

  data() {
    return {
      pageNum: 1,
      memberlist: [],
      category: "start",
      keyword: "",
      options: [
        { text: "선택", value: "start" },
        { text: "아이디", value: "id" },
        { text: "이름", value: "name" },
        { text: "차량번호", value: "carNum" },
      ],
      pages: 0, // 페이지 수
      pageGroupSize: 3, // [추가] 한 번에 보여줄 페이지 번호 개수

      updisplay: "display:none",
      memdata: [],
      updatename: "",
      updatecarNum: "",
      memberCarType: false,
      updatecarType: 0,
      updatephone: "",
      updateemail: "",
    };
  },

  mounted() {
    this.getList();
  },
  computed: {
    // [추가] 현재 페이지가 속한 그룹의 시작 번호 (예: 4페이지 -> 4, 5페이지 -> 4, 6페이지 -> 4, 7페이지 -> 7)
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
        .get("/memberlist", param)
        .then((resp) => {
          // alert('success');
          this.memberlist = resp.data;
          // alert(this.memberlist);
        })
        .catch((err) => {
          alert(err);
        });

      axios
        .get("/membercount", param)
        .then((resp) => {
          // alert(resp.data);
          this.pages = Math.ceil(resp.data / 10) || 1;
        })
        .catch((err) => {
          alert(err);
        });
    },
    pageClick(page) {
      // alert(page);
      this.pageNum = page;
      this.getList();
    },
    // [수정] 그룹 단위 이동 → 페이지 단위 이동(한 페이지씩)으로 변경.
    // pageGroupStart는 pageNum을 기준으로 계산되므로, pageNum만 1씩 바꿔도
    // 보여지는 3개 번호 묶음은 자동으로 따라 움직임 (예: 3페이지→4페이지 이동 시 "4 5 6"으로 전환)
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
      // alert(this.category);
      this.pageNum = 1;
      this.getList();
    },
    updateDisplay(member) {
      this.memdata = member;
      // alert(member.carType);
      if (this.memdata.carType === 1) {
        this.memberCarType = true;
      } else {
        this.memberCarType = false;
      }

      this.updisplay = "display:block";
    },
    cancelUpdate() {
      this.memdata = [];
      this.updatename = "";
      this.updatecarNum = "";
      this.updatecarType = false;
      this.updatephone = "";
      this.updateemail = "";
      this.updisplay = "display:none";
    },
    updateMember() {
      let checkUpdate = confirm("정말 수정하시겠습니까?");

      if (checkUpdate) {
        if (this.updatename === null || this.updatename.trim() === "") {
          this.updatename = this.memdata.name;
        }

        if (this.updatecarNum === null || this.updatecarNum.trim() === "") {
          this.updatecarNum = this.memdata.carNum;
        }

        if (this.memberCarType === true) {
          this.updatecarType = 1;
        } else {
          this.updatecarType = 0;
        }

        if (this.updatephone === null || this.updatephone.trim() === "") {
          this.updatephone = this.memdata.phone;
        }

        if (this.updateemail === null || this.updateemail.trim() === "") {
          this.updateemail = this.memdata.email;
        }

        const param = {
          params: {
            id: this.memdata.id,
            name: this.updatename,
            carNum: this.updatecarNum,
            carType: this.updatecarType,
            phone: this.updatephone,
            email: this.updateemail,
          },
        };

        axios
          .post("/updatememberbyadmin", null, param)
          .then((resp) => {
            alert("수정이 완료되었습니다");
            this.getList();
            this.memdata = [];

            this.updatename = "";
            this.updatecarNum = "";
            this.updatecarType = false;
            this.updatephone = "";
            this.updateemail = "";

            this.updisplay = "display:none";
          })
          .catch((err) => {
            alert(err);
          });
      }
    },
    deleteMember(member) {
      let checkDelete = confirm("정말 삭제하시겠습니까?");

      if (checkDelete) {
        const param = {
          params: {
            id: member.id,
          },
        };

        axios
          .post("/deletemember", null, param)
          .then((resp) => {
            // alert(resp.data);

            if (resp.data) {
              alert(member.name + "(" + member.id + ") 회원이 삭제되었습니다");
              this.getList();
            } else {
              alert("회원삭제에 실패하였습니다");
            }
          })
          .catch((err) => {
            alert(err);
          });
      }
    },
  },
};
</script>
<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/admin/CSS/admintable.css"></style>
<style src="@/components/admin/CSS/adminmember.css"></style>
