<template>
  <div class="site-header">
    <div class="brand" @click="goHome">
      <img :src="logoImage" alt="Medical Tower 주차관리시스템" class="brand-logo" />
    </div>

    <div class="header-right">
      <!-- [수정] 점주(auth==2)일 때만 노출되던 걸, 로그인한 모든 유형(관리자/점주/일반 회원)에 노출되도록 확장.
                 welcomeLabel이 로그인 유형에 따라 "관리자"/"점주"/"회원"으로 바뀜 -->
      <p v-if="mngName" class="welcome-msg">
        {{ welcomeLabel }} {{ mngName }} 님 환영합니다!
      </p>
      <button class="logBtn" :style="inDp" @click="login"><span>👤</span> 로그인</button>
      <button class="logBtn" :style="outDp" @click="logout">
        <span>👤</span> 로그아웃
      </button>
    </div>
  </div>
</template>

<script>
import logoImage from "../../assets/logo-crop.png";

export default {
  name: "Header",
  data() {
    return {
      logoImage: logoImage,
      inDp: "display:none",
      outDp: "display:none",
      mngName: "",
      welcomeLabel: "", // [추가] 로그인 유형에 따른 라벨 (관리자/점주/회원)
    };
  },
  mounted() {
    if (sessionStorage.getItem("login") === null) {
      this.inDp = "display:block";
    }
    if (sessionStorage.getItem("login") !== null) {
      this.outDp = "display:block";
      const login = JSON.parse(sessionStorage.getItem("login"));
      // [수정] 기존엔 auth==2(점주)일 때만 mngName을 채워서 환영 문구가 점주에게만 보였음.
      // 로그인한 모든 유형(관리자/점주/일반 회원)에서 이름 + 라벨이 함께 보이도록 변경.
      if (login.auth == 1) {
        this.welcomeLabel = "관리자";
      } else if (login.auth == 2) {
        this.welcomeLabel = "점주";
      } else {
        this.welcomeLabel = "회원";
      }
      this.mngName = login.name;
    }
  },
  methods: {
    goHome() {
      this.$router.push("/");
    },
    login() {
      this.outDp = "display:none";
      location.href = "/login";
    },
    logout() {
      sessionStorage.removeItem("login");
      this.outDp = "display:none";
      alert("로그아웃되었습니다");
      location.href = "/";
    },
  },
};
</script>

<style src="@/components/basic/CSS/common.css"></style>
