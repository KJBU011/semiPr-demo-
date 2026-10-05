<script setup>
import { useCookies } from "vue3-cookies"; // cookie import
import axios from "axios";
</script>

<template>
  <div id="login-page">
    <h1>login</h1>
    <table border="1">
      <colgroup>
        <col width="100px" />
        <col width="200px" />
      </colgroup>
      <thead></thead>
      <tbody>
        <tr>
          <th>아이디</th>
          <td><input v-model="id" size="24" @input="IptSave" /></td>
        </tr>
        <tr>
          <th></th>
          <td><input type="checkbox" v-model="sid" @click="clkSave" /> 아이디 저장</td>
        </tr>
        <tr>
          <th>비밀번호</th>
          <td><input type="password" v-model="pw" size="24" @keyup.enter="login" /></td>
        </tr>
        <tr>
          <td colspan="2"><button @click="login">로그인</button></td>
        </tr>
        <tr>
          <td colspan="2"><a href="/regi">회원가입</a></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
const { cookies } = useCookies();

export default {
  // 변수
  data() {
    return {
      id: "",
      pw: "",
      sid: false,
    };
  },

  // 페이지가 처음 로딩되었을 때
  mounted() {
    // 쿠키에서 유저아이디 받아오기
    let userId = cookies.get("userId");

    // 쿠키에 저장된 유저 아이디가 있을 경우
    if (userId !== null) {
      this.sid = true; // 체크박스 체크
      this.id = userId; // 아이디 입력창에 넣어주기
    }

    // 쿠키에 저장된 유저 아이디가 없을 경우
    else {
      this.sid = false; // 체크박스 해제(없어도 무관)
      this.id = ""; // 아이디 입력창 비워주기(없어도 무관)
    }
  },

  // 함수
  methods: {
    // 아이디 저장 체크박스를 클릭했을 경우
    clkSave() {
      // 체크박스를 체크한 경우
      if (this.sid === false && this.id.trim() !== "") {
        cookies.set("userId", this.id);
      }

      // 체크박스를 해제했을 경우
      else {
        cookies.remove("userId");
      }
    },

    // 아이디 저장이 체크되어있는 상태에서 아이디를 수정했을 경우
    IptSave() {
      // 체크박스가 체크되어있고, 아이디가 비어있지 않은 경우
      if (this.sid == true && this.id.trim() !== "") {
        cookies.set("userId", this.id);
      }
      // 체크박스가 해제되어있거나, 아이디가 비어있는 경우
      else {
        cookies.remove("userId");
      }
    },

    // 로그인 버튼을 눌렀을 경우
    login() {
      // 전송할 파라미터
      let param = {
        params: {
          id: this.id, // 아이디
          pw: this.pw, // 패스워드
        },
      };

      // back end 접근
      axios
        .post("/login", null, param)
        .then((resp) => {
          // 작동 확인용 -> 사용 후 주석
          // alert('Success');

          // 데이터 받아오기
          let mem = resp.data;

          // 받아온 데이터가 없는 경우
          if (mem.id === undefined) {
            alert("Id 나 Password 를 확인해주십시오");
            return;
          }

          // 가져온 데이터를 세션에 String 형태로 저장
          sessionStorage.setItem("login", JSON.stringify(resp.data));

          ///////////////////////////////////////////////////////////
          // location 값이 저장된 상태로 로그인 페이지로 넘어온 경우 복귀
          let lo = sessionStorage.getItem("location");

          // cookie 에 저장된 location 값이 없을 경우 -> home.vue 로 이동
          if (lo === null || lo === "") {
            lo = "/";
          }

          if (resp.data.auth === 1) {
            lo = "/admin";
          }

          location.href = lo;

          ///////////////////////////////////////////////////////////
        })
        .catch((err) => {
          alert(err);
        });
    },
  },
};
</script>

<style src="@/components/basic/css/common.css"></style>
<style src="@/components/member/css/login.css"></style>
