<template>
  <footer>
    <br/>
    <h1>MEDICAL TOWER PARKING MANAGEMENT SYSTEM</h1>
    <p class="footer-tagline">편리한 방문, 스마트한 주차</p>

    <ul class="footer-menu">
      <li v-for="(item, index) in menuItems" :key="index">
        <span>{{ item }}</span>
      </li>
    </ul>

    <p class="footer-copyright">
      © 2026 MEDICAL TOWER PARKING MANAGEMENT SYSTEM. ALL RIGHTS RESERVED.
    </p>
    <br/>
  </footer>
</template>

<script>
export default {
  name: "Footer",
  data() {
    return {
      menuItems: ["주차 이용안내", "주차요금 안내", "고객센터", "개인정보처리방침"],
    };
  },
};
</script>
