<template>
  <div class="answer-page">
    <h2>QnA 답변 작성</h2>

    <hr />

    <!-- 원본 문의글 -->
    <div v-if="question" class="question-box">
      <h3>문의 내용</h3>

      <div class="question-title">
        {{ question.title }}
      </div>

      <div class="question-writer">
        작성자 : {{ question.id }}
      </div>

      <div class="question-content">
        {{ question.content }}
      </div>
    </div>

    <!-- 관리자 답변 -->
    <div class="answer-form">
      <div class="form-row">
        <label>답변자</label>

        <input
          type="text"
          :value="currentUserId"
          readonly
        />
      </div>

      <div class="form-row">
        <label>제목</label>

        <input
          type="text"
          v-model="title"
          maxlength="200"
        />
      </div>

      <div class="form-row content-row">
        <label>답변 내용</label>

        <textarea
          v-model="content"
          placeholder="답변 내용을 입력하세요."
        ></textarea>
      </div>
    </div>

    <div class="button-wrap">
      <button @click="goBack">
        취소
      </button>

      <button
        class="btn-answer"
        @click="submitAnswer"
      >
        답변 등록
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "QnaAnswer",

  data() {
    return {
      currentUserId: "",

      loginMember: null,

      question: null,

      title: "",

      content: "",
    };
  },

  created() {
    const loginInfo = sessionStorage.getItem("login");

    // 로그인 확인
    if (!loginInfo) {
      alert("로그인이 필요합니다.");

      this.$router.push({
        name: "login",
      });

      return;
    }

    this.loginMember = JSON.parse(loginInfo);

    // 관리자 여부 확인
    if (this.loginMember.auth !== 1) {
      alert("관리자만 답변을 작성할 수 있습니다.");

      this.$router.push({
        name: "qna",
      });

      return;
    }

    this.currentUserId = this.loginMember.id;

    // 원본 문의 조회
    this.getQuestion();
  },

  methods: {
    // ==================== 원본 문의 조회 ====================
    getQuestion() {
      const seq = this.$route.params.seq;

      axios
        .get("/qnadetail", {
          params: {
            seq: seq,
          },
        })
        .then((resp) => {
          this.question = resp.data || null;

          if (!this.question) {
            alert("문의글을 찾을 수 없습니다.");

            this.$router.push({
              name: "qna",
            });

            return;
          }

          // 답변 제목 기본값
          this.title = "RE: " + this.question.title;
        })
        .catch((err) => {
          console.log(err);
          alert("문의글을 불러오지 못했습니다.");
        });
    },

    // ==================== 답변 등록 ====================
    submitAnswer() {
      if (!this.title.trim()) {
        alert("답변 제목을 입력해주세요.");
        return;
      }

      if (!this.content.trim()) {
        alert("답변 내용을 입력해주세요.");
        return;
      }

      const params = new URLSearchParams();

      params.append("seq", this.$route.params.seq);
      params.append("id", this.currentUserId);
      params.append("title", this.title);
      params.append("content", this.content);

      axios
        .post(
          "/qnaanswer",
          params
        )
        .then((resp) => {
          if (resp.data) {
            alert("답변이 등록되었습니다.");

            this.$router.push({
              name: "qna",
            });
          } else {
            alert("답변 등록에 실패했습니다.");
          }
        })
        .catch((err) => {
          console.log(err);
          alert("답변 등록 중 오류가 발생했습니다.");
        });
    },

    // ==================== 취소 ====================
    goBack() {
      this.$router.push({
        name: "qnadetail",

        params: {
          seq: this.$route.params.seq,
        },
      });
    },
  },
};
</script>

<style scoped>
.answer-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 30px 20px;
}

.answer-page h2 {
  text-align: center;
}

/* 원본 문의 */
.question-box {
  margin-top: 25px;
  margin-bottom: 35px;
  border: 1px solid #ddd;
}

.question-box h3 {
  margin: 0;
  padding: 13px;
  background-color: #f5f5f5;
}

.question-title {
  padding: 15px;
  font-weight: bold;
  border-bottom: 1px solid #ddd;
}

.question-writer {
  padding: 10px 15px;
  font-size: 14px;
  color: #666;
  border-bottom: 1px solid #ddd;
}

.question-content {
  min-height: 120px;
  padding: 18px 15px;
  white-space: pre-wrap;
}

/* 답변 폼 */
.answer-form {
  border-top: 2px solid #333;
}

.form-row {
  display: flex;
  border-bottom: 1px solid #ddd;
}

.form-row label {
  width: 120px;
  padding: 15px;
  background-color: #f7f7f7;
  font-weight: bold;
}

.form-row input {
  flex: 1;
  margin: 7px;
  padding: 10px;
}

.content-row textarea {
  flex: 1;
  min-height: 250px;
  margin: 7px;
  padding: 12px;
  resize: vertical;
}

.button-wrap {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.button-wrap button {
  padding: 9px 22px;
  cursor: pointer;
}

.btn-answer {
  background-color: #333;
  color: white;
  border: none;
}
</style>