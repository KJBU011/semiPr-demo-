<template>
  <div id="manager-page">
    <!-- [삭제] 좌측 사이드바 전체 → 메뉴는 nav.vue의 "점주 페이지" hover 드롭다운으로 이동
    <div class="sidebar">
      <ul class="menu-list">
        <li :class="{ active: currentMenu === 'car' }" @click="currentMenu = 'car'">차량 관리</li>
        <li :class="{ active: currentMenu === 'point' }" @click="currentMenu = 'point'">포인트 관리</li>
        <li :class="{ active: currentMenu === 'dashboard' }" @click="currentMenu = 'dashboard'">통계</li>
      </ul>
    </div>
    -->
    <!-- [삭제] "점주 OOO님, 환영합니다." 문구는 Header.vue 우측 상단으로 이동 -->

    <!-- [수정] page-body 안에 content만 남음 (사이드바 제거로 flex 2단 구성 → 단일 구성) -->
    <div class="page-body">
      <div class="content">
        <dashboard v-if="currentMenu === 'dashboard'" />
        <carmanage v-else-if="currentMenu === 'car'" />
        <pointmanage v-else-if="currentMenu === 'point'" />
      </div>
    </div>
  </div>
</template>

<script>
import manager from "../../routers/manager/manager";
import carmanage from "./carmanage.vue";
import pointmanage from "./pointmanage.vue";
import dashboard from "./dashboard.vue";

export default {
  components: {
    carmanage,
    pointmanage,
    dashboard,
  },
  data() {
    return {
      currentMenu: "dashboard", // 기본으로 보여줄 메뉴
    };
  },
  mounted() {
    // [삭제] 로그인 정보(mngName) 읽기 → 환영 문구가 Header.vue로 이동하면서 더 이상 필요 없음
    let auth = JSON.parse(sessionStorage.getItem('login')).auth;
    if(auth !== 2){
      this.$router.push('/');
    }

    // [추가] nav의 점주 페이지 드롭다운에서 넘어온 ?tab= 값으로 시작 메뉴 결정
    // (사이드바 클릭 대신, nav 드롭다운 클릭 → /manager?tab=car 식으로 이동해서 여기서 값을 읽음)
    this.currentMenu = this.$route.query.tab || "dashboard";
  },
  watch: {
    // [추가] 이미 /manager에 있는 상태에서 드롭다운의 다른 항목을 클릭했을 때도 반영
    // (사이드바가 없어졌으니, 페이지 안 li 클릭 대신 URL 쿼리 변화를 감지해야 함)
    "$route.query.tab"(tab) {
      this.currentMenu = tab || "dashboard";
    },
  },
};
</script>

<style src="@/components/basic/css/common.css"></style>
<style src="@/components/manager/css/manager.css"></style>
