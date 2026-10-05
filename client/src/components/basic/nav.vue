<template>
  <div>
    <ul class="nav">
      <li>
        <p class="navLink" @click="goHome">홈</p>
      </li>
      <li class="nav-dropdown">
        <p class="navLink" :class="{ active: isActive('/store') }" @click="goStr">
          시설 안내
        </p>
        <ul class="dropdown-menu">
          <li><p class="dropdown-item" @click="goStrMenu(3)">5F</p></li>
          <li><p class="dropdown-item" @click="goStrMenu(2)">3F ~ 4F</p></li>
          <li><p class="dropdown-item" @click="goStrMenu(1)">1F ~ 2F</p></li>
        </ul>
      </li>
      <li class="nav-dropdown">
        <p class="navLink" :class="{ active: isActive('/board') }" @click="goBrd">
          게시판
        </p>
        <ul class="dropdown-menu">
          <li><p class="dropdown-item" @click="goBrdMenu('list')">자유 게시판</p></li>
          <li><p class="dropdown-item" @click="goBrdMenu('faq')">FAQ 게시판</p></li>
          <li><p class="dropdown-item" @click="goBrdMenu('qna')">Q&A 게시판</p></li>
        </ul>
      </li>
      <li>
        <p class="navLink" :class="{ active: isActive('/parking') }" @click="goPark">
          주차 안내
        </p>
      </li>
      <li v-if="login.auth > 0 && login.auth < 4">
        <p class="navLink" :class="{ active: isActive('/my') }" @click="goMy">
          마이페이지
        </p>
      </li>
      <li v-if="login.auth == 2" class="nav-dropdown">
        <p class="navLink" :class="{ active: isActive('/manager') }" @click="goMng">
          점주 페이지
        </p>
        <ul class="dropdown-menu">
          <li><p class="dropdown-item" @click="goMngMenu('dashboard')">통계</p></li>
          <li><p class="dropdown-item" @click="goMngMenu('car')">차량 정보 조회</p></li>
          <li><p class="dropdown-item" @click="goMngMenu('point')">포인트 관리</p></li>
        </ul>
      </li>
      <li v-if="login.auth == 1">
        <p class="navLink" :class="{ active: isActive('/admin') }" @click="goAd">
          관리자
        </p>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "Nav",
  data() {
    return {
      login: [],
      id: "",
    };
  },
  mounted() {
    if (sessionStorage.getItem("login") !== null) {
      this.login = JSON.parse(sessionStorage.getItem("login"));
      // alert(JSON.stringify(this.login));
    }
  },
  methods: {
    // [추가] 현재 라우트(this.$route.path)가 인자로 받은 path로 시작하는지 확인.
    // "/"(홈)만 정확히 일치해야 하고, 나머지는 하위 경로(예: /board?menu=faq)도
    // 같은 메뉴로 취급되도록 startsWith로 비교. (this.$route는 반응형이라
    // 라우트가 바뀌면 이 메소드가 다시 평가되어 active 클래스가 자동으로 갱신됨)
    isActive(path) {
      if (path === "/") {
        return this.$route.path === "/";
      }
      return this.$route.path.startsWith(path);
    },
    goHome() {
      this.$router.push("/");
    },
    goStr(){
      this.$router.push("/store");
    },
    goStrMenu(num){
      if(num === 1){
        this.$router.push("/store1f2f");
      } else if(num === 2){
        this.$router.push("/store3f4f");
      } else if(num === 3){
        this.$router.push("/store5f");
      }
    },
    goBrd() {
      this.$router.push("/board");
    },
    goBrdMenu(menu) {
      this.$router.push({ path: "/board", query: { menu } });
    },
    goPark() {
      this.$router.push("/parking");
    },
    goMy() {
      this.$router.push("/my");
    },
    goMng() {
      this.$router.push("/manager");
    },
    // 드롭다운에서 특정 메뉴를 눌렀을 때 -> 쿼리스트링으로 어떤 메뉴인지 같이 넘김
    goMngMenu(menu) {
      this.$router.push({ path: "/manager", query: { tab: menu } });
    },
    goAd() {
      this.$router.push("/admin");
    },
  },
};
</script>
