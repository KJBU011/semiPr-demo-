<template>
  <div class="facility-guide-container">
    <!-- 헤더 섹션 -->
    <header class="guide-header">
      <span class="badge">FACILITY GUIDE</span>
      <h1>메디컬 타워 층별 시설 안내</h1>
      <p class="subtitle">방문하시는 고객님의 편의를 위한 층별 상세 안내입니다.</p>
    </header>

    <!-- 층별 안내 리스트 -->
    <section class="floor-list">
      <!-- 5층 -->
      <article class="floor-card" @click="goFloor(3)">
        <div class="floor-image">
          <img src="https://images.unsplash.com/photo-1598902108854-10e335adac99?q=80&w=800&auto=format&fit=crop" alt="옥상 정원" />
          <span class="floor-badge">5F</span>
        </div>
        <div class="floor-info">
          <h2>5층 · 옥상 정원</h2>
          <p class="floor-desc">
            도심 속 휴식과 힐링을 위한 야외 옥상 정원입니다. 산책로와 쉼터가 조성되어 있어 진료 대기 중이나 방문 시 편안한 휴식을 취하실 수 있습니다.
          </p>
          <ul class="tag-list">
            <li># 야외정원</li>
            <li># 휴게쉼터</li>
            <li># 산책로</li>
            <li># 힐링존</li>
          </ul>
        </div>
      </article>

      <!-- 3층 & 4층 -->
      <article class="floor-card" @click="goFloor(2)">
        <div class="floor-image">
          <img src="https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=800&auto=format&fit=crop" alt="메디컬 센터" />
          <span class="floor-badge">3F - 4F</span>
        </div>
        <div class="floor-info">
          <h2>3층 - 4층 · 메디컬 센터</h2>
          <p class="floor-desc">
            분야별 전문 의료진과 첨단 의료 장비를 갖춘 메디컬 전문층입니다. 내과, 정형외과, 치과, 피부과 등 다양한 병·의원이 입점하여 체계적인 진료를 제공합니다.
          </p>
          <ul class="tag-list">
            <li># 종합내과</li>
            <li># 정형외과</li>
            <li># 치과</li>
            <li># 피부과</li>
            <li># 건강검진센터</li>
          </ul>
        </div>
      </article>

      <!-- 1층 & 2층 -->
      <article class="floor-card" @click="goFloor(1)">
        <div class="floor-image">
          <img src="https://images.unsplash.com/photo-1555396273-367ea4eb4db5?q=80&w=800&auto=format&fit=crop" alt="편의 시설 및 식당가" />
          <span class="floor-badge">1F - 2F</span>
        </div>
        <div class="floor-info">
          <h2>1층 - 2층 · 근린생활 & 편의시설</h2>
          <p class="floor-desc">
            방문객 및 입주민을 위한 다양한 다이닝, 카페, 편의점 및 처방 약국이 위치해 있습니다. 진료 전후 편안하게 이용하실 수 있습니다.
          </p>
          <ul class="tag-list">
            <li># 전문약국</li>
            <li># 브런치카페</li>
            <li># 한식/일식/양식</li>
            <li># 24시 편의점</li>
            <li># 은행/ATM</li>
          </ul>
        </div>
      </article>
    </section>
  </div>
</template>

<script>
export default {
  name: 'FacilityGuide',
  methods: {
    goFloor(num) {
      if(num === 1){
        this.$router.push("/store1f2f");
      } else if(num === 2){
        this.$router.push("/store3f4f");
      } else if(num === 3){
        this.$router.push("/store5f");
      }
    }
  },
}
</script>

<style scoped>
.facility-guide-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: 'Pretendard', -apple-system, BlinkMacSystemFont, system-ui, Roboto, sans-serif;
  color: #333;
}

/* 헤더 스타일 */
.guide-header {
  text-align: center;
  margin-bottom: 50px;
}

.badge {
  display: inline-block;
  background-color: #e8f3ff;
  color: #0066ff;
  font-weight: 700;
  font-size: 12px;
  padding: 6px 14px;
  border-radius: 20px;
  margin-bottom: 12px;
  letter-spacing: 1px;
}

.guide-header h1 {
  font-size: 32px;
  font-weight: 700;
  color: #111;
  margin-bottom: 10px;
  max-width: none;
}

.subtitle {
  font-size: 16px;
  color: #666;
}

/* 층별 카드 스타일 */
.floor-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.floor-card {
  display: flex;
  background-color: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  border: 1px solid #f0f0f0;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.floor-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
}

.floor-image {
  position: relative;
  width: 40%;
  min-width: 300px;
  overflow: hidden;
}

.floor-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.floor-card:hover .floor-image img {
  transform: scale(1.05);
}

.floor-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  background: rgba(0, 0, 0, 0.75);
  backdrop-filter: blur(4px);
  color: #fff;
  font-weight: 800;
  font-size: 16px;
  padding: 6px 14px;
  border-radius: 8px;
}

.floor-info {
  padding: 32px;
  width: 60%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.floor-info h2 {
  font-size: 22px;
  font-weight: 700;
  color: #222;
  margin-bottom: 12px;
}

.floor-desc {
  font-size: 15px;
  line-height: 1.6;
  color: #555;
  margin-bottom: 20px;
}

/* 태그 리스트 */
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  list-style: none;
  padding: 0;
  margin: 0;
}

.tag-list li {
  background-color: #f5f7fa;
  color: #556070;
  font-size: 13px;
  font-weight: 500;
  padding: 6px 12px;
  border-radius: 6px;
}

/* 반응형 모바일 UI */
@media (max-width: 768px) {
  .floor-card {
    flex-direction: column;
  }

  .floor-image {
    width: 100%;
    height: 220px;
  }

  .floor-info {
    width: 100%;
    padding: 24px;
  }

  .guide-header h1 {
    font-size: 24px;
  }
}
</style>