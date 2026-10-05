<template>
  <div class="rooftop-container">
    <!-- 1. 메인 히어로 섹션 -->
    <section class="hero-section">
      <div class="hero-image-wrapper">
        <img 
          src="https://images.unsplash.com/photo-1598902108854-10e335adac99?auto=format&fit=crop&w=1600&q=80" 
          alt="옥상정원 메인 전경" 
          @error="handleImageError"
        />
        <div class="hero-overlay"></div>
      </div>
      <div class="hero-content">
        <span class="location-badge">5F ROOFTOP GARDEN</span>
        <h1 class="hero-title">도심 속 하늘 아래,<br />작은 쉼표 하나</h1>
        <p class="hero-desc">
          메디컬 타워 최상층에 위치한 힐링 스팟.<br />
          탁 트인 전망과 푸른 정원에서 편안한 휴식을 경험해보세요.
        </p>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-label">운영시간</span>
            <span class="stat-value">08:00 - 21:00</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-label">입장료</span>
            <span class="stat-value">무료 (자유이용)</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-label">주요시설</span>
            <span class="stat-value">산책로 / 쉼터 / 카페테라스</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 2. 매력 포인트 소개 섹션 -->
    <section class="spots-section">
      <div class="section-header">
        <h2>정원 속 스페셜 스팟</h2>
        <p>옥상정원에서 즐길 수 있는 다양한 휴식 공간을 살펴보세요.</p>
      </div>

      <div class="spot-list">
        <!-- 스팟 1: 산책로 -->
        <article class="spot-item">
          <div class="spot-img-box">
            <img 
              src="https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=800&q=80" 
              alt="스카이 산책로" 
              @error="handleImageError"
            />
          </div>
          <div class="spot-info">
            <span class="spot-num">01</span>
            <h3>스카이 산책로</h3>
            <p>
              계절마다 다채롭게 피어나는 자생화와 푸른 잔디를 따라 조성된 산책길입니다.
              진료 대기 시간이 지루할 때 가볍게 걸으며 마음의 여유를 찾으실 수 있습니다.
            </p>
            <div class="spot-tags">
              <span>#힐링산책</span>
              <span>#계절정원</span>
              <span>#자연친화</span>
            </div>
          </div>
        </article>

        <!-- 스팟 2: 파노라마 전망대 (도시 조망 전경) -->
        <article class="spot-item reverse">
          <div class="spot-img-box">
            <img 
              src="https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80" 
              alt="파노라마 전망대 - 도시 전경" 
              @error="handleImageError"
            />
          </div>
          <div class="spot-info">
            <span class="spot-num">02</span>
            <h3>파노라마 전망대</h3>
            <p>
              시티뷰가 한눈에 펼쳐지는 시원한 전망 공간입니다.
              해 질 녘에는 노을빛으로 물드는 도심의 멋진 풍경을 조망할 수 있습니다.
            </p>
            <div class="spot-tags">
              <span>#시티뷰</span>
              <span>#포토존</span>
              <span>#노을맛집</span>
            </div>
          </div>
        </article>

        <!-- 스팟 3: 야외 벤치 테라스 -->
        <article class="spot-item">
          <div class="spot-img-box">
            <img 
              src="https://images.unsplash.com/photo-1506836467174-27f1042aa48c?auto=format&fit=crop&w=800&q=80" 
              alt="그늘막 휴게 테라스 - 야외 벤치" 
              @error="handleImageError"
            />
          </div>
          <div class="spot-info">
            <span class="spot-num">03</span>
            <h3>그늘막 휴게 테라스</h3>
            <p>
              따사로운 햇살을 피할 수 있는 벤치와 파라솔 테라스가 완비되어 있습니다.
              1~2층에서 구매한 음료나 간식을 가지고 올라와 자유롭게 담소를 나누어 보세요.
            </p>
            <div class="spot-tags">
              <span>#야외테라스</span>
              <span>#휴식공간</span>
              <span>#야외티타임</span>
            </div>
          </div>
        </article>
      </div>
    </section>

    <!-- 3. 이용 가이드 및 수칙 -->
    <section class="guide-rules">
      <div class="rules-card">
        <h3>🌿 옥상정원 이용 에티켓</h3>
        <ul class="rules-list">
          <li>
            <strong>금연 구역:</strong> 정원 전체는 쾌적한 환경을 위해 <strong>전 구역 금연</strong>으로 운영됩니다.
          </li>
          <li>
            <strong>시설물 보호:</strong> 화단의 꽃과 나무를 아껴주시고 쓰레기는 지정된 수거함에 버려주세요.
          </li>
          <li>
            <strong>안전 주의:</strong> 난간 밖으로 몸을 밀거나 기대는 행위는 위험하오니 삼가 주시기 바랍니다.
          </li>
          <li>
            <strong>기상 상황:</strong> 강풍, 우천, 폭설 등 기상 악화 시 안전을 위해 출입이 제한될 수 있습니다.
          </li>
        </ul>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'store5F',
  data() {
    return {
      fallbackImage: 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="800" height="500" viewBox="0 0 800 500"><rect width="800" height="500" fill="%23e2e8f0"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" font-family="sans-serif" font-size="24" fill="%2364748b">5F 옥상정원</text></svg>'
    }
  },
  methods: {
    handleImageError(e) {
      e.target.src = this.fallbackImage;
    }
  }
}
</script>

<style scoped>
.rooftop-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 20px 60px;
  font-family: 'Pretendard', -apple-system, BlinkMacSystemFont, system-ui, sans-serif;
  color: #333;
}

/* 1. 히어로 섹션 */
.hero-section {
  position: relative;
  height: 480px;
  border-radius: 24px;
  overflow: hidden;
  margin-top: 20px;
  margin-bottom: 60px;
  display: flex;
  align-items: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.12);
}

.hero-image-wrapper {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.hero-image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.75) 0%, rgba(0, 0, 0, 0.3) 100%);
}

.hero-content {
  position: relative;
  z-index: 1;
  padding: 0 50px;
  color: #fff;
  max-width: 650px;
}

.location-badge {
  display: inline-block;
  background-color: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #a7f3d0;
  font-weight: 700;
  font-size: 13px;
  padding: 6px 14px;
  border-radius: 20px;
  margin-bottom: 16px;
  letter-spacing: 1px;
}

.hero-title {
  font-size: 38px;
  font-weight: 800;
  line-height: 1.3;
  margin-bottom: 16px;
  color:#fff;
}

.hero-desc {
  font-size: 16px;
  line-height: 1.6;
  opacity: 0.9;
  margin-bottom: 30px;
  color:#eee;
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 20px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  padding: 16px 24px;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.15);
}

.stat-item {
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 12px;
  color: #cbd5e1;
  margin-bottom: 2px;
}

.stat-value {
  font-size: 14px;
  font-weight: 700;
  color: #fff;
}

.stat-divider {
  width: 1px;
  height: 24px;
  background-color: rgba(255, 255, 255, 0.2);
}

/* 2. 스팟 섹션 */
.spots-section {
  margin-bottom: 60px;
}

.section-header {
  text-align: center;
  margin-bottom: 50px;
}

.section-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #111;
  margin-bottom: 8px;
}

.section-header p {
  font-size: 15px;
  color: #666;
}

.spot-list {
  display: flex;
  flex-direction: column;
  gap: 50px;
}

.spot-item {
  display: flex;
  align-items: center;
  gap: 40px;
  background-color: #fff;
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
  border: 1px solid #f0f0f0;
}

.spot-item.reverse {
  flex-direction: row-reverse;
}

.spot-img-box {
  width: 45%;
  height: 280px;
  border-radius: 16px;
  overflow: hidden;
  flex-shrink: 0;
  background-color: #f1f5f9;
}

.spot-img-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.spot-item:hover .spot-img-box img {
  transform: scale(1.05);
}

.spot-info {
  width: 55%;
  padding: 10px 20px;
}

.spot-num {
  font-size: 24px;
  font-weight: 900;
  color: #10b981;
  display: block;
  margin-bottom: 8px;
}

.spot-info h3 {
  font-size: 22px;
  font-weight: 700;
  color: #222;
  margin-bottom: 12px;
}

.spot-info p {
  font-size: 15px;
  color: #555;
  line-height: 1.6;
  margin-bottom: 20px;
}

.spot-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.spot-tags span {
  background-color: #ecfdf5;
  color: #059669;
  font-size: 13px;
  font-weight: 600;
  padding: 6px 12px;
  border-radius: 8px;
}

/* 3. 이용 수칙 섹션 */
.guide-rules {
  margin-top: 40px;
}

.rules-card {
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 30px 40px;
}

.rules-card h3 {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 16px;
}

.rules-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(380px, 1fr));
  gap: 12px 24px;
}

.rules-list li {
  font-size: 14px;
  color: #475569;
  line-height: 1.5;
  position: relative;
  padding-left: 14px;
}

.rules-list li::before {
  content: "•";
  position: absolute;
  left: 0;
  color: #10b981;
  font-weight: bold;
}

/* 반응형 UI */
@media (max-width: 768px) {
  .hero-section {
    height: auto;
    padding: 60px 0;
  }

  .hero-content {
    padding: 0 24px;
  }

  .hero-title {
    font-size: 28px;
  }

  .hero-stats {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .stat-divider {
    display: none;
  }

  .spot-item, .spot-item.reverse {
    flex-direction: column;
    padding: 16px;
  }

  .spot-img-box, .spot-info {
    width: 100%;
  }

  .spot-img-box {
    height: 200px;
  }

  .rules-list {
    grid-template-columns: 1fr;
  }

  .rules-card {
    padding: 24px;
  }
}
</style>