<template>
  <div class="write-page">
    <br/>
    <br/>
    
    <h2>Q&A 작성</h2>

    <hr />

    <div class="write-form">
      <div class="form-row">
        <label>작성자</label>
        <input type="text" :value="currentUserId" readonly />
      </div>

      <div class="form-row">
        <label>제목</label>
        <input type="text" v-model="title" maxlength="200" placeholder="제목을 입력하세요." />
      </div>

      <div class="form-row content-row">
        <label>내용</label>
        <textarea v-model="content" placeholder="문의 내용을 입력하세요."></textarea>
      </div>
    </div>

    <div class="button-wrap">
      <button @click="goToList">목록</button>
      <button class="btn-submit" @click="writeQna">작성완료</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "QnaWrite",
  data() {
    return {
      currentUserId: "",
      title: "",
      content: "",
    };
  },
  created() {
    const loginInfo = sessionStorage.getItem("login");
    if (!loginInfo) {
      alert("로그인이 필요한 페이지입니다.");
      this.$router.push({ name: "login" });
      return;
    }
    const loginMember = JSON.parse(loginInfo);
    this.currentUserId = loginMember.id;
  },
  methods: {
    writeQna() {
      if (!this.title.trim()) { alert("제목을 입력해주세요."); return; }
      if (!this.content.trim()) { alert("내용을 입력해주세요."); return; }
      const params = new URLSearchParams();
      params.append("id", this.currentUserId);
      params.append("title", this.title);
      params.append("content", this.content);
      axios.post("/qnawrite", params)
        .then((resp) => {
          if (resp.data) {
            alert("문의글이 등록되었습니다.");
            this.$router.push({ name: "qna" });
          } else {
            alert("문의글 등록에 실패했습니다.");
          }
        })
        .catch((err) => { console.log(err); alert("문의글 등록 중 오류가 발생했습니다."); });
    },
    goToList() {
      this.$router.push({ name: "qna" });
    },
  },
};
</script>