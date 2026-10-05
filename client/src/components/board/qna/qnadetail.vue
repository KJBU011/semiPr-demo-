<template>
  <div class="detail-page">
    <br />
    <h2>Q&A</h2>
    <h3>상세보기</h3>

    <div v-if="qna" class="detail-box">
      <div v-if="!editMode" class="detail-title">{{ qna.title }}</div>
      <div v-if="editMode" style="width: 800px; padding-left: 18px;">
        <br/>
        <input class="form-control" style="width: 800px;" placeholder="제목 입력" v-model="qna.title"/>
      </div>
      <div class="detail-info" style="display: flex;">
        <span>작성자 : {{ qna.id }}</span>
        <span>작성일 : {{ formatDateTime(qna.wdate) }}</span>
        <span>조회수 : {{ qna.readcount || 0 }}</span>
      </div>
      <div v-if="!editMode" class="detail-content">{{ qna.content }}</div>
      <div v-if="editMode" class="detail-content">
        <textarea rows="15" class="form-control" placeholder="내용 입력" v-model="qna.content"></textarea>
      </div>
    </div>

    <div v-else class="empty">게시글을 찾을 수 없습니다.</div>

    <div v-if="qna" class="answer-section">
      <h3>답변</h3>
      <div v-if="answers.length > 0" class="answer-list">
        <div v-for="answer in answers" :key="answer.seq" class="answer-box">
          <div class="answer-label">관리자 답변</div>
          <div class="answer-title">{{ answer.title }}</div>
          <div class="answer-info">
            <span>작성자 : {{ answer.id }}</span>
            <span>작성일 : {{ formatDateTime(answer.wdate) }}</span>
          </div>
          <div class="answer-content">{{ answer.content }}</div>
        </div>
      </div>
      <div v-else class="no-answer">아직 등록된 답변이 없습니다.</div>
    </div>

    <div class="button-wrap">
      <button @click="goToList">목록</button>
      <button v-if="isAdmin && qna && qna.depth === 0" class="btn-answer" @click="goToAnswer">답변 작성</button>
      <div v-if="loginMember !== null">
        <span v-if="loginMember.id == qna.id">
          <button v-if="!editMode" class="btn-answer" @click="changeEdit">수정</button>
          <button v-if="editMode" class="btn-answer" @click="updataQna">수정완료</button>
        </span>
        &nbsp;
        <span v-if="loginMember.auth == 1 || loginMember.id == qna.id">
          <button class="btn-answer" @click="deleteQna">삭제</button>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "QnaDetail",
  data() {
    return {
      seq: sessionStorage.getItem('qna'),
      qna: null,
      answers: [],
      loginMember: null,

      editMode:false,

      title:'',
      content:'',

    };
  },
  computed: {
    isAdmin() {
      return this.loginMember && this.loginMember.auth === 1;
    },
  },
  created() {
    const loginInfo = sessionStorage.getItem("login");
    if (loginInfo) { this.loginMember = JSON.parse(loginInfo); }
    this.getQnaDetail();
  },
  methods: {
    getQnaDetail() {
      axios.get("/qnadetail", { params: { seq: this.seq } })
        .then((resp) => {
          this.qna = resp.data || null;
          if (!this.qna) {
            alert("게시글을 찾을 수 없습니다.");
            this.$router.push({ name: "qna" });
            return;
          }
          if (this.qna.ref !== null && this.qna.ref !== undefined) {
            this.getQnaAnswers(this.qna.ref);
          } else {
            this.answers = [];
          }
        })
        .catch((err) => {
          console.log("QnA 상세조회 오류 :", err);
          this.qna = null;
          this.answers = [];
        });
    },
    getQnaAnswers(ref) {
      axios.get("/qnaanswers", { params: { ref: ref } })
        .then((resp) => { this.answers = resp.data || []; })
        .catch((err) => { console.log("QnA 답변 조회 오류 :", err); this.answers = []; });
    },
    goToList() {
      this.$router.push({ name: "qna" });
    },
    goToAnswer() {
      if (!this.isAdmin) { alert("관리자만 답변을 작성할 수 있습니다."); return; }
      if (!this.qna) { alert("게시글 정보를 불러올 수 없습니다."); return; }
      this.$router.push({ name: "qnaanswer", params: { seq: this.qna.seq } });
    },
    formatDateTime(dateStr) {
      if (!dateStr) return "-";
      const date = new Date(dateStr);
      if (Number.isNaN(date.getTime())) return dateStr;
      return date.toLocaleString("ko-KR", {
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit",
      });
    },
    changeEdit(){
      this.editMode = true;
    },
    updataQna(){
      let upCheck = confirm('정말 수정하시겠습니까?');
      if(upCheck){
        const param = {
          params:{
            seq:this.seq,
            title:this.qna.title,
            content:this.qna.content
          }
        }

        axios.post('/qnaupdate', null, param)
              .then(resp=>{
                if(resp.data){
                  alert('글이 수정되었습니다');
                  this.$router.push({name:'qnadetail'});
                } else{
                  alert('글이 수정되지 않았습니다');
                }
              })
              .catch(err=>{
                console.error(err);
              })
      }
      this.editMode = false;
    },
    deleteQna(){
      let delCheck = confirm('정말 삭제하시겠습니까?');
      if(delCheck){

        const param = {
          params:{
            seq:this.seq
          }
        }

        axios.post('/qnadelete', null, param)
              .then(resp=>{
                if(resp.data){
                  alert('글이 삭제되었습니다');
                  this.$router.push({name:'qna'});
                } else{
                  alert('글이 삭제되지 않았습니다');
                }
              })
              .catch(err=>{
                console.error(err);
              })
      }
    },
  },
};
</script>