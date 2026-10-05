<template>
  <div class="qna-page">

    <div class="qna-header">
      <h2>Q&A 게시판</h2>
      <p class="qna-description">주차장 이용과 관련된 문의사항을 남겨주세요</p>
      <div class="write-wrap">
        <button v-if="loginMember" class="btn-write" @click="goToWrite">글쓰기</button>
      </div>
    </div>

    <hr />

    <!-- ✅ 수정: 검색창을 테이블 위로 이동 -->
    <div class="search-wrap">
      <select v-model="category">
        <option value="">선택</option>
        <option value="title">제목</option>
        <option value="content">내용</option>
        <option value="writer">작성자</option>
      </select>
      <input type="text" v-model="keyword" placeholder="검색어를 입력하세요" @keyup.enter="searchQna" />
      <button class="btn-search" @click="searchQna">검색</button>
    </div>

    <table class="qna-table">
      <thead>
        <tr>
          <th class="col-no">번호</th>
          <th>제목</th>
          <th class="col-writer">작성자</th>
          <th class="col-date">작성일</th>
          <th class="col-read">조회수</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="qna in qnaList"
          :key="qna.seq"
          class="qna-row"
          @click="goToDetail(qna.seq)"
        >
          <td>
            <span v-if="qna.depth > 0">답변</span>
            <span v-else>{{ getDisplayNumber(qna) }}</span>
          </td>
          <td class="title-cell">
            <span v-if="qna.depth > 0" class="answer-indent">└ [답변]</span>
            <span :class="{ 'answer-title': qna.depth > 0 }">{{ qna.title }}</span>
          </td>
          <td>{{ qna.id }}</td>
          <td>{{ formatDate(qna.wdate) }}</td>
          <td>{{ qna.readcount || 0 }}</td>
        </tr>
        <tr v-if="qnaList.length === 0">
          <td colspan="5" class="empty">등록된 QnA가 없습니다.</td>
        </tr>
      </tbody>
    </table>

    <div class="pagination-wrap">
      <div class="pagination">
        <button class="page-btn" :disabled="pageNumber === 0" @click="prevPage">이전</button>
        <button
          v-for="page in pageNumbers"
          :key="page"
          class="page-number"
          :class="{ active: pageNumber === page }"
          @click="goToPage(page)"
        >{{ page + 1 }}</button>
        <button class="page-btn" :disabled="pageNumber >= totalPages - 1" @click="nextPage">다음</button>
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "QnaBoard",
  data() {
    return {
      qnaList: [],
      category: "",
      keyword: "",
      pageNumber: 0,
      totalCount: 0,
      pageSize: 10,
      loginMember: null,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.totalCount / this.pageSize);
    },
    pageNumbers() {
      const pages = [];
      for (let i = 0; i < this.totalPages; i++) {
        pages.push(i);
      }
      return pages;
    },
    questionList() {
      return this.qnaList.filter((item) => item.depth === 0);
    },
  },
  created() {
    const loginInfo = sessionStorage.getItem("login");
    if (loginInfo) {
      this.loginMember = JSON.parse(loginInfo);
    }
    this.getQnaList();
    this.getQnaCount();
  },
  methods: {
    getQnaList() {
      axios.get("/qnalist", {
        params: { pageNumber: this.pageNumber, category: this.category, keyword: this.keyword },
      })
      .then((resp) => { this.qnaList = resp.data || []; })
      .catch((err) => { console.log("QnA 목록 조회 오류 :", err); this.qnaList = []; });
    },
    getQnaCount() {
      axios.get("/qnacount", {
        params: { category: this.category, keyword: this.keyword },
      })
      .then((resp) => { this.totalCount = resp.data || 0; })
      .catch((err) => { console.log("QnA 개수 조회 오류 :", err); this.totalCount = 0; });
    },
    getDisplayNumber(qna) {
      const index = this.questionList.findIndex((item) => item.seq === qna.seq);
      if (index === -1) return "";
      return index + 1;
    },
    searchQna() {
      this.pageNumber = 0;
      this.getQnaList();
      this.getQnaCount();
    },
    goToDetail(seq) {
      sessionStorage.setItem('qna', seq);
      this.$router.push({ name: "qnadetail"});
    },
    goToWrite() {
      if (!this.loginMember) {
        alert("로그인이 필요합니다.");
        this.$router.push({ name: "login" });
        return;
      }
      this.$router.push({ name: "qnawrite" });
    },
    prevPage() {
      if (this.pageNumber > 0) { this.pageNumber--; this.getQnaList(); }
    },
    nextPage() {
      if (this.pageNumber < this.totalPages - 1) { this.pageNumber++; this.getQnaList(); }
    },
    goToPage(page) {
      this.pageNumber = page;
      this.getQnaList();
    },
    formatDate(dateStr) {
      if (!dateStr) return "-";
      const date = new Date(dateStr);
      if (Number.isNaN(date.getTime())) return dateStr;
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      return year + "-" + month + "-" + day;
    },
  },
};
</script>