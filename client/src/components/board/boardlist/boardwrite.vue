<template>
  <div id="boardwrite">
    <br/>
    <br/>
    <h2>게시글 작성</h2>

    <hr/>

    <!-- ✅ 수정: table → form-row 방식으로 변경 (QnA 스타일) -->
    <div class="write-form">
      <div class="form-row">
        <label>아이디</label>
        <input type="text" v-model="id" readonly />
      </div>
      <div class="form-row">
        <label>제목</label>
        <input type="text" v-model="title" placeholder="제목을 입력하세요." />
      </div>
      <div class="form-row content-row">
        <label>내용</label>
        <textarea v-model="content" placeholder="내용을 입력하세요."></textarea>
      </div>
    </div>

    <div class="board-btn-wrap">
      <a href="/boardlist" class="btn btn-outline-primary">목록</a>
      <button @click="boardwrite" class="btn btn-primary">작성완료</button>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      id: "",
      title: "",
      content: "",
    };
  },
  async mounted() {
    let login = sessionStorage.getItem("login");
    if (login === "null" || login === "" || login === null) {
      alert("로그인이 필요한 페이지입니다");
      sessionStorage.setItem("location", "/boardwrite");
      await this.$router.push({ name: "login" });
      return;
    }
    let json = JSON.parse(login);
    this.id = json.id;
  },
  methods: {
    boardwrite() {
      if (!this.title.trim() || !this.content.trim()) {
        alert("제목과 내용을 입력해 주세요");
        return;
      }
      const param = {
        params: { id: this.id, title: this.title.trim(), content: this.content.trim() },
      };
      axios.post("/writeboard", null, param)
      .then((resp) => {
        if (resp.data === true) {
          this.$router.push({ name: "boardlist" });
        } else {
          alert("글이 추가되지 않았습니다");
        }
      })
      .catch((err) => { console.error(err); });
    },
  },
};
</script>
