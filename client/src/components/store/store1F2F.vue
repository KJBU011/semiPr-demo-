<template>
  <div class="store-container">
    <!-- 헤더 섹션 -->
    <header class="store-header">
      <span class="badge">1F - 2F</span>
      <h1>근린생활 & 편의시설</h1>
      <p class="subtitle">메디컬 타워 1~2층에 입점한 매장을 소개합니다.</p>
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
    
    <!-- 매장 리스트 그리드 -->
    <section class="store-grid">
      <article 
        v-for="store in filteredStores" 
        :key="store.id" 
        class="store-card"
      >
        <div class="card-image">
          <img :src="store.image" :alt="store.name" />
          <span class="floor-tag" :class="store.floorClass">{{ store.floor }}</span>
        </div>
        <div class="card-body">
          <div class="card-header">
            <span class="category">{{ store.categoryText }}</span>
            <span class="location"><i class="loc-icon">📍</i> {{ store.location }}</span>
          </div>
          <h2 class="store-name">{{ store.name }}</h2>
          <p class="store-desc">{{ store.description }}</p>
          <div class="card-footer">
            <span class="time">⏰ {{ store.hours }}</span>
            <span class="tel">📞 {{ store.tel }}</span>
          </div>
        </div>
      </article>
    </section>
  </div>
</template>

<script>
export default {
  name: 'store1F2F',
  data() {
    return {
      activeTab: 'all',
      tabs: [
        { label: '전체보기', value: 'all' },
        { label: '식당가', value: 'restaurant' },
        { label: '카페 & 디저트', value: 'cafe' },
        { label: '약국 & 편의점', value: 'convenience' }
      ],
      stores: [
        {
          id: 1,
          name: '타워 메디컬 약국',
          category: 'convenience',
          categoryText: '약국',
          floor: '1F',
          floorClass: 'f1',
          location: '101호 (메인 로비 옆)',
          hours: '08:30 - 20:00',
          tel: '032-123-4567',
          description: '전문의약품 처방 조제 및 친절한 복약 지도 전문 약국입니다.',
          image: 'https://images.unsplash.com/photo-1586015555751-63bb77f4322a?q=80&w=600&auto=format&fit=crop'
        },
        {
          id: 2,
          name: 'CU 메디컬타워점',
          category: 'convenience',
          categoryText: '편의점',
          floor: '1F',
          floorClass: 'f1',
          location: '102호',
          hours: '00:00 - 24:00 (24시간)',
          tel: '032-123-4568',
          description: '24시간 운영되는 편의점으로 생활 생필품 및 간식거리를 제공합니다.',
          image: 'https://images.unsplash.com/photo-1604719312566-8912e9227c6a?q=80&w=600&auto=format&fit=crop'
        },
        {
          id: 3,
          name: '스타벅스 메디컬타워점',
          category: 'cafe',
          categoryText: '카페',
          floor: '1F',
          floorClass: 'f1',
          location: '103~104호',
          hours: '07:00 - 21:00',
          tel: '1522-3232',
          description: '넓고 쾌적한 공간에서 즐기는 프리미엄 커피와 디저트 공간입니다.',
          image: 'https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?q=80&w=600&auto=format&fit=crop'
        },
        {
          id: 4,
          name: '본죽&비빔밥',
          category: 'restaurant',
          categoryText: '식당',
          floor: '2F',
          floorClass: 'f2',
          location: '201호',
          hours: '09:00 - 20:30',
          tel: '032-123-4569',
          description: '환우분들과 방문객을 위한 영양 가득한 정성 어린 죽과 비빔밥 메뉴.',
          image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=600&auto=format&fit=crop'
        },
        {
          id: 5,
          name: '스시 마스다',
          category: 'restaurant',
          categoryText: '식당',
          floor: '2F',
          floorClass: 'f2',
          location: '202호',
          hours: '11:30 - 21:30',
          tel: '032-123-4570',
          description: '신선한 제철 회와 깔끔한 초밥 정식을 즐길 수 있는 일식 전문점.',
          image: 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=600&auto=format&fit=crop'
        },
        {
          id: 6,
          name: '투썸플레이스',
          category: 'cafe',
          categoryText: '카페',
          floor: '2F',
          floorClass: 'f2',
          location: '203호',
          hours: '08:00 - 22:00',
          tel: '032-123-4571',
          description: '다양한 케이크와 페어링 음료를 함께 즐기는 프리미엄 디저트 카페.',
          image: 'https://images.unsplash.com/photo-1554118811-1e0d58224f24?q=80&w=600&auto=format&fit=crop'
        }
      ]
    }
  },
  computed: {
    filteredStores() {
      if (this.activeTab === 'all') {
        return this.stores;
      }
      return this.stores.filter(store => store.category === this.activeTab);
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

/* 매장 카드 그리드 */
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

.floor-tag.f1 {
  background-color: #10b981; /* 1층: 초록계열 */
}

.floor-tag.f2 {
  background-color: #f59e0b; /* 2층: 주황계열 */
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