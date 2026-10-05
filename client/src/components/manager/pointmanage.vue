<template>
  <div id="point-manager" class="point-panel">
    <div class="panel-header">
      <span class="panel-eyebrow">POINT STATUS</span>
      <h3 class="panel-title">포인트 관리</h3>
    </div>

    <p class="remain-highlight">{{ discntTime }} 시간 남았습니다</p>

    <div class="progress-track">
      <div class="progress-remain" :style="{ width: getRemainPercent() + '%' }"></div>
      <div class="progress-used" :style="{ width: getUsedPercent() + '%' }"></div>
    </div>

    <div class="progress-legend">
      <p>
        <span class="legend-dot remain"></span>잔여 {{ discntTime }} /
        {{ totalTime }}시간
      </p>
      <p><span class="legend-dot used"></span>사용 {{ getRemainTime() }}시간</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      totalTime: 200, // 총 할당 시간 (기준)
      discntTime: 0, //  일단 0으로 초기화 (sessionStorage 에서 가져옴)
    };
  },
  methods: {
    getTime(){
      const login=JSON.parse(sessionStorage.getItem('login'))
      const param={
        params:{
          id:login.id
        }
      }

      axios.get('/getmember', param)
            .then(resp=>{
              this.discntTime=resp.data.discntTime;
            })
            .catch(error=>{
              console.error(error);
            });
    },
    // [남은 시간 계산]: 총 시간(200) - 사용한 시간
    getRemainTime() {
      return this.totalTime - this.discntTime;
    },

    // [남은 시간 비율 계산]: 퍼센트로 바꾸기 (남은 시간 / 총 시간) * 100
    getRemainPercent() {
      return (this.getRemainTime() / this.totalTime) * 100;
    },

    // [사용 시간 비율 계산]: 퍼센트로 바꾸기 (사용 시간 / 총 시간) * 100
    getUsedPercent() {
      return (this.discntTime / this.totalTime) * 100;
    },
  },
  created() {
    this.getTime();
  },
};
</script>

<style src="@/components/basic/css/common.css"></style>
<style src="@/components/manager/css/data-table.css"></style>
<style src="@/components/manager/css/pointmanage.css"></style>
