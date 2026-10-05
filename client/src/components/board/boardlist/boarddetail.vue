<template>
  <div>
    <div class="center">

      <!-- ✅ 추가: 제목 영역 -->
      <h1>자유게시판</h1>
      <hr/>

      <table class="table table-bordered">
        <colgroup>
          <col width="100" />
          <col width="500" />
        </colgroup>
        <tbody>
          <tr>
            <th>작성자</th>
            <td>{{ id }}</td>
          </tr>
          <tr>
            <th>작성일</th>
            <td>{{ regDate }}</td>
          </tr>
          <tr>
            <th>조회수</th>
            <td>{{ viewCnt }}</td>
          </tr>
          <!-- ✅ 수정: 제목 영역 (수정 모드와 일반 조회 모드 분기) -->
          <tr>
            <th>제목</th>
            <td>
              <input v-if="isEditing" v-model="title" class="form-control" />
              <span v-else style="font-weight: 600;">{{ title }}</span>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <textarea rows="15" class="form-control" v-model="content" :readonly="!isEditing"></textarea>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- ✅ 수정: 버튼 가운데 정렬 -->
      <div class="board-btn-wrap">
        <a href="/boardlist" class="btn btn-outline-primary">목록으로</a>
        <div v-if="isShow" class="board-edit-btns">
          <button v-if="!isEditing" @click="isEditing = true" class="btn btn-primary">글수정</button>
          <button v-if="isEditing" @click="boardUpdate" class="btn btn-primary">수정완료</button>
          <button v-if="isEditing" @click="cancelEdit" class="btn btn-secondary">취소</button>
          <button v-if="!isEditing" @click="boardDelete" class="btn btn-danger">글삭제</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      boardNo: sessionStorage.getItem('boardNo'),
      id: "",
      title: "",
      content: "",
      regDate: "",
      viewCnt: "",
      isShow: false,
      isEditing: false,
      originalTitle: "",
      originalContent: "",
    };
  },
  mounted() {
    this.getBoard();
  },
  methods: {
    getBoard() {
      axios.get("/getboard", {
        params: { boardNo: this.boardNo },
      })
      .then((resp) => {
        const board = resp.data;
        this.id = board.id;
        // ✅ 수정: 백엔드 필드명이 title, boardTitle, subject 중 다르게 들어올 경우 안전하게 바인딩
        this.title = board.title || board.boardTitle || board.subject || "";
        this.content = board.content;
        this.regDate = board.regDate;
        this.viewCnt = board.viewCnt;
        this.originalTitle = this.title;
        this.originalContent = board.content;
        let login = JSON.parse(sessionStorage.getItem("login"));
        if (login && (login.id === this.id || login.auth === 1)) {
          this.isShow = true;
        }
      })
      .catch((err) => { console.log(err); });
    },
    boardUpdate() {
      const trimmedTitle = this.title.trim();
      const trimmedContent = this.content.trim();
      if (!trimmedTitle || !trimmedContent) {
        alert("제목과 내용을 입력해 주세요");
        return;
      }
      const param = {
        params: { boardNo: this.boardNo, title: trimmedTitle, content: trimmedContent, id: this.id },
      };
      axios.post("/updateboard", null, param)
      .then((resp) => {
        if (resp.data === true) {
          alert("수정되었습니다");
          this.isEditing = false;
          this.getBoard();
        } else {
          alert("수정에 실패했습니다");
        }
      })
      .catch((err) => { console.error(err); });
    },
    cancelEdit() {
      this.title = this.originalTitle;
      this.content = this.originalContent;
      this.isEditing = false;
    },
    boardDelete() {
      if (!confirm("정말 삭제하시겠습니까?")) return;
      axios.post("/deleteboard", null, {
        params: { boardNo: this.boardNo },
      })
      .then((resp) => {
        if (resp.data === true) {
          alert("삭제되었습니다");
          this.$router.push({ name: "boardlist" });
        } else {
          alert("삭제에 실패했습니다");
        }
      })
      .catch((err) => { console.error(err); });
    },
  },
};
</script>
