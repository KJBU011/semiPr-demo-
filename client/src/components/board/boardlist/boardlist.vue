<template>
  <div id="boardlist">

    <h1>자유게시판</h1>
    <h3>회원님들의 자유로운 공간입니다</h3>

    <div class="board-write-wrap">
      <a href="/boardwrite" class="board-write-btn" v-if="login != null">글쓰기</a>
    </div>

    <hr />

    <!-- 검색창 -->
    <div class="search-wrap">
      <input v-model="keyword" placeholder="통합 검색" @keyup.enter="onSearch" />
      <button class="btn-search" @click="onSearch">검색</button>
    </div>

    <table class="table table-hover">
      <colgroup>
        <col width="80" />
        <col />
        <col width="120" />
        <col width="160" />
        <col width="80" />
      </colgroup>
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일</th>
          <th>조회수</th>
        </tr>
      </thead>
      <tbody v-if="boardList.length != 0" v-for="(board, index) in boardList" v-bind:key="index">
        <tr>
          <td style="text-align: center;">{{ (currentPage - 1) * pageSize + index + 1 }}</td>
          <td @click="boarddetail(board.boardNo)" class="text-underline-hover">
            {{ dot3(board.title) }}
          </td>
          <td>{{ board.id }}</td>
          <td>{{ board.regDate }}</td>
          <td>{{ board.viewCnt }}</td>
        </tr>
      </tbody>
      <tbody v-if="boardList.length == 0">
        <tr>
          <td colspan="5" class="empty">검색 결과가 없습니다.</td>
        </tr>
      </tbody>
    </table>

    <div v-if="boardList.length != 0" class="pagination-wrap">
      <div class="pagination">
        <button class="page-btn" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">이전</button>
        <!-- ✅ 수정: 현재 페이지부터 +2 표시 -->
        <button v-for="page in totalPage" :key="page" v-show="page >= pageGroupStart && page <= pageGroupStart + 2"
          class="page-number" :class="{ active: currentPage === page }" @click="changePage(page)">{{ page }}</button>
        <button class="page-btn" :disabled="currentPage === totalPage" @click="changePage(currentPage + 1)">다음</button>
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      login:JSON.parse(sessionStorage.getItem('login')),
      boardList: [],
      currentPage: 1,
      pageSize: 10,
      totalPage: 0,
      pageGroupStart: 1,
    };
  },
  mounted() {
    this.getBoardList();
    this.getBoardCount();
  },
  methods: {
    getBoardList() {
      axios
        .get("/getboardlist", {
          params: {
            // 현재 페이지 번호를 Spring Boot로 전달
            page: this.currentPage,
            keyword: this.keyword,
          },
        })
        .then((resp) => {
          // Spring Boot에서 받아온 게시글 목록
          this.boardList = resp.data;
        })
        .catch((err) => {
          console.error(err);
        });
    },

    // 전체 게시글 개수 조회
    getBoardCount() {
      axios
        .get("/getboardcount", {
          params: {
            // 검색어를 Spring Boot로 전달
            keyword: this.keyword,
          },
        })
        .then((resp) => {
          // 전체 게시글 개수
          const boardCount = resp.data;

          // 전체 게시글 수를 이용해서 총 페이지 수 계산
          this.totalPage = Math.ceil(boardCount / this.pageSize);
        })
        .catch((err) => {
          console.error(err);
        });
    },
    changePage(page) {
      this.currentPage = page;
      // ✅ 수정: 현재 페이지부터 +2 표시, 마지막 근처면 조정
      this.pageGroupStart = Math.floor((page - 1) / 3) * 3 + 1;
      this.getBoardList();
    },
    dot3(title) {
      if (title.length >= 45) return title.substring(0, 35) + "...";
      return title.trim();
    },
    boarddetail(boardNo) {
      sessionStorage.setItem('boardNo', boardNo);
      this.$router.push({ name: "boarddetail", params: { boardNo: boardNo } });
    },
    // 검색 버튼 클릭 또는 엔터 시 실행
    onSearch() {
      // 앞뒤 공백만 있는 경우 검색어 없는 것으로 처리
      const trimmedKeyword = this.keyword.trim();

      this.currentPage = 1;
      this.getBoardList();
      this.getBoardCount();
    },
  },
};
</script>
