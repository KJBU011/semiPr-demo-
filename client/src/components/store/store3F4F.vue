<template>
  <div class="store-container">
    <!-- 헤더 섹션 -->
    <header class="store-header">
      <span class="badge">3F - 4F</span>
      <h1>메디컬 센터 & 전문 클리닉</h1>
      <p class="subtitle">분야별 전문 의료진과 최첨단 장비를 갖춘 메디컬 전문층입니다.</p>
    </header>

    <!-- 카테고리 필터 탭 -->
    <nav class="filter-tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab.value" 
        :class="['tab-btn', { active: activeTab === tab.value }]"
        @click="activeTab = tab.value"
      >
        {{ tab.label }}
      </button>
    </nav>

    <br/>

    <!-- 진료과 리스트 그리드 -->
    <section class="store-grid">
      <article 
        v-for="clinic in filteredClinics" 
        :key="clinic.id" 
        class="store-card"
      >
        <div class="card-image">
          <img 
            :src="clinic.image" 
            :alt="clinic.name" 
            @error="handleImageError"
          />
          <span class="floor-tag" :class="clinic.floorClass">{{ clinic.floor }}</span>
        </div>
        <div class="card-body">
          <div class="card-header">
            <span class="category">{{ clinic.categoryText }}</span>
            <span class="location"><i class="loc-icon">📍</i> {{ clinic.location }}</span>
          </div>
          <h2 class="store-name">{{ clinic.name }}</h2>
          <p class="store-desc">{{ clinic.description }}</p>
          <div class="card-footer">
            <span class="time">⏰ {{ clinic.hours }}</span>
            <span class="tel">📞 {{ clinic.tel }}</span>
          </div>
        </div>
      </article>
    </section>
  </div>
</template>

<script>
export default {
  name: 'store3F4F',
  data() {
    return {
      activeTab: 'all',
      // 이미지 로딩 실패 시 대체할 SVG Data URI
      fallbackImage: 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="600" height="400" viewBox="0 0 600 400"><rect width="600" height="400" fill="%23f1f5f9"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" font-family="sans-serif" font-size="20" fill="%2394a3b8">의료진 진료안내</text></svg>',
      tabs: [
        { label: '전체보기', value: 'all' },
        { label: '내과', value: 'internal' },
        { label: '외과·정형', value: 'surgery' },
        { label: '특수·전문진료', value: 'special' }
      ],
      clinics: [
        // --- 3층 진료과 (6개) ---
        {
          id: 1,
          name: '서울연세 내과의원',
          category: 'internal',
          categoryText: '내과',
          floor: '3F',
          floorClass: 'f3',
          location: '301호',
          hours: '09:00 - 18:00',
          tel: '032-123-3001',
          description: '소화기내과 전문의 진료, 5대 암 검진 및 위·대장 내시경 전담 센터.',
          image: 'https://images.unsplash.com/photo-1629909613654-28e377c37b09?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 2,
          name: '바른이 소아청소년과',
          category: 'internal',
          categoryText: '소아과',
          floor: '3F',
          floorClass: 'f3',
          location: '302호',
          hours: '09:00 - 18:30',
          tel: '032-123-3002',
          description: '영유아 건강검진, 예방접종 및 소아 호흡기 질환 전문 진료.',
          image: 'https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 3,
          name: '연세 이비인후과의원',
          category: 'internal',
          categoryText: '이비인후과',
          floor: '3F',
          floorClass: 'f3',
          location: '303호',
          hours: '09:00 - 18:00',
          tel: '032-123-3003',
          description: '비염, 축농증, 난청, 어지럼증 클리닉 및 음성 질환 전문 진료.',
          image: 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 4,
          name: '맑은눈 안과의원',
          category: 'special',
          categoryText: '안과',
          floor: '3F',
          floorClass: 'f3',
          location: '304호',
          hours: '09:00 - 18:00',
          tel: '032-123-3004',
          description: '백내장, 녹내장, 드림렌즈, 안구건조증 전문 정밀 검사 클리닉.',
          // 수정된 안과 이미지 URL
          image: 'https://images.unsplash.com/photo-1551076805-e1869033e561?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 5,
          name: '연세 스마일 치과의원',
          category: 'special',
          categoryText: '치과',
          floor: '3F',
          floorClass: 'f3',
          location: '305호',
          hours: '09:30 - 18:30 (야간 진료)',
          tel: '032-123-3005',
          description: '임플란트, 치아교정, 스케일링, 무통 충치 치료 전담 팀 운영.',
          image: 'https://images.unsplash.com/photo-1606811841689-23dfddce3e95?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 6,
          name: '마음건강 정신건강의학과',
          category: 'internal',
          categoryText: '정신건강의학과',
          floor: '3F',
          floorClass: 'f3',
          location: '306호',
          hours: '10:00 - 19:00',
          tel: '032-123-3006',
          description: '우울증, 불면증, 스트레스 및 심리 상담 케어 전문 클리닉.',
          image: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=600&q=80'
        },

        // --- 4층 진료과 (6개) ---
        {
          id: 7,
          name: '바른마디 정형외과',
          category: 'surgery',
          categoryText: '정형외과',
          floor: '4F',
          floorClass: 'f4',
          location: '401호',
          hours: '09:00 - 18:00',
          tel: '032-123-4001',
          description: '관절·척추 관절 센터, 도수치료 및 체외충격파 비수술 치료 전문.',
          image: 'https://images.unsplash.com/photo-1516549655169-df83a0774514?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 8,
          name: '오라클 피부과의원',
          category: 'special',
          categoryText: '피부과',
          floor: '4F',
          floorClass: 'f4',
          location: '402호',
          hours: '10:00 - 20:00 (야간 진료)',
          tel: '032-123-4002',
          description: '여드름, 리프팅, 레이저 토닝, 메디컬 스킨케어 및 쁘띠 시술.',
          // 수정된 피부과 이미지 URL
          image: 'https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 9,
          name: '미유 산부인과의원',
          category: 'surgery',
          categoryText: '산부인과',
          floor: '4F',
          floorClass: 'f4',
          location: '403호',
          hours: '09:00 - 18:00',
          tel: '032-123-4003',
          description: '여성 질환 검진, 자궁경부암 검진, 미드와이프 맞춤 웰빙 케어.',
          image: 'https://images.unsplash.com/photo-1532938911079-1b06ac7ceec7?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 10,
          name: '비상 비뇨의학과',
          category: 'surgery',
          categoryText: '비뇨의학과',
          floor: '4F',
          floorClass: 'f4',
          location: '404호',
          hours: '09:00 - 18:00',
          tel: '032-123-4004',
          description: '요로결석 클리닉(24시 대응), 전립선 질환 및 남성/여성 요실금 케어.',
          image: 'https://images.unsplash.com/photo-1579684385127-1ef15d508118?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 11,
          name: '연세 재활의학과',
          category: 'surgery',
          categoryText: '재활의학과',
          floor: '4F',
          floorClass: 'f4',
          location: '405호',
          hours: '09:00 - 18:30',
          tel: '032-123-4005',
          description: '수술 후 재활, 자세 교정, 운동 치료 및 맞춤 물리치료 센터.',
          image: 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?auto=format&fit=crop&w=600&q=80'
        },
        {
          id: 12,
          name: '경희한의원',
          category: 'special',
          categoryText: '한의원',
          floor: '4F',
          floorClass: 'f4',
          location: '406호',
          hours: '09:30 - 18:30',
          tel: '032-123-4006',
          description: '교통사고 후유증, 침구 치료, 체질 맞춤 한약 처방 및 추나요법.',
          image: 'https://images.unsplash.com/photo-1505751172876-fa1923c5c528?auto=format&fit=crop&w=600&q=80'
        }
      ]
    }
  },
  computed: {
    filteredClinics() {
      if (this.activeTab === 'all') {
        return this.clinics;
      }
      return this.clinics.filter(clinic => clinic.category === this.activeTab);
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
.store-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: 'Pretendard', -apple-system, BlinkMacSystemFont, system-ui, sans-serif;
  color: #333;
}

/* 헤더 */
.store-header {
  text-align: center;
  margin-bottom: 35px;
}

.badge {
  display: inline-block;
  background-color: #0066ff;
  color: #fff;
  font-weight: 700;
  font-size: 13px;
  padding: 4px 12px;
  border-radius: 12px;
  margin-bottom: 12px;
}

.store-header h1 {
  font-size: 30px;
  font-weight: 700;
  color: #111;
  margin-bottom: 8px;
  max-width: none;
}

.subtitle {
  font-size: 15px;
  color: #666;
}

/* 카테고리 탭 */
.filter-tabs {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.tab-btn {
  background-color: #f4f5f7;
  border: none;
  padding: 10px 20px;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 600;
  color: #555;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab-btn:hover {
  background-color: #e5e8eb;
}

.tab-btn.active {
  background-color: #0066ff;
  color: #fff;
  box-shadow: 0 4px 10px rgba(0, 102, 255, 0.25);
}

/* 진료과 카드 그리드 */
.store-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 25px;
}

.store-card {
  background-color: #fff;
  border-radius: 16px;
  border: 1px solid #eaeaea;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.04);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  display: flex;
  flex-direction: column;
}

.store-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
}

.card-image {
  position: relative;
  height: 200px;
  background-color: #f1f5f9;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.store-card:hover .card-image img {
  transform: scale(1.05);
}

/* 층수 태그 */
.floor-tag {
  position: absolute;
  top: 15px;
  right: 15px;
  padding: 4px 12px;
  border-radius: 20px;
  font-weight: 700;
  font-size: 13px;
  color: #fff;
}

.floor-tag.f3 {
  background-color: #0284c7;
}

.floor-tag.f4 {
  background-color: #7c3aed;
}

/* 카드 본문 */
.card-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 12px;
}

.category {
  color: #0066ff;
  font-weight: 700;
}

.location {
  color: #777;
}

.store-name {
  font-size: 18px;
  font-weight: 700;
  color: #222;
  margin-bottom: 8px;
}

.store-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 16px;
  flex-grow: 1;
}

.card-footer {
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #888;
}

@media (max-width: 600px) {
  .store-grid {
    grid-template-columns: 1fr;
  }
}
</style>