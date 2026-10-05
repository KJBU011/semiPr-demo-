<template>
  <div class="home">
    <!-- 히어로: img 태그로 이미지 비율 그대로 표시 + 텍스트 오버레이 -->
    <section class="hero">
      <img :src="heroImage" alt="메디컬타워 주차관리시스템" class="hero-image" />
      <div class="hero-content">
        <p class="hero-tagline">편리한 방문, 스마트한 주차</p>
        <h1 class="hero-title">메디컬 타워</h1>
        <p class="hero-subtitle">더 편리한 의료 환경을 위한 스마트 주차 솔루션</p>

        <div class="hero-features">
          <div class="hero-feature" v-for="(item, index) in features" :key="index">
            <div class="feature-icon-wrap">
              <img :src="item.icon" :alt="item.label" class="feature-icon-img" />
            </div>
            <p class="feature-label">{{ item.label }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 하단 정보 바 -->
    <section class="bottom-bar">
      <div class="bottom-item" v-for="(item, index) in bottomFeatures" :key="index">
        <span>{{ item.icon }}</span>
        <span>{{ item.text }}</span>
      </div>
    </section>
  </div>
</template>

<script>
import heroImage from "../../assets/홈화면이미지-배경자름.png";
import iconCar from "../../assets/입출차.png";
import iconParking from "../../assets/주차현황.png";
import iconWon from "../../assets/요금결제.png";
import iconStats from "../../assets/통계관리.png";

export default {
  name: "Home",
  data() {
    return {
      heroImage: heroImage,
      features: [
        { icon: iconCar, label: "간편한\n입·출차" },
        { icon: iconParking, label: "실시간\n주차 현황" },
        { icon: iconWon, label: "편리한\n요금 결제" },
        { icon: iconStats, label: "주차 통계\n관리" },
      ],
      bottomFeatures: [
        { icon: "🛡️", text: "안전한 주차 환경" },
        { icon: "📢", text: "24시간 보안 시스템" },
        { icon: "🔌", text: "전기차 충전 지원" },
        { icon: "🌿", text: "친환경 스마트 타워" },
      ],
    };
  },
};
</script>
