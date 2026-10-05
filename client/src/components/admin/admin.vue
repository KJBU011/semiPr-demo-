<template>
  <div id="admin-page">
    <h1>관리자 페이지</h1>
    <table border="1">
      <colgroup>
        <col width="400px" />
        <col width="400px" />
        <col width="400px" />
        <col width="400px" />
      </colgroup>
      <thead>
        <tr>
          <!-- 클릭했을 경우 displaying 변수를 각 메뉴에 맞는 글자로 변경 -->
          <th>
            <p
              class="adminMenu"
              :class="{ active: displaying === 'admindash' }"
              @click="displaying = 'admindash'"
            >
              대시보드
            </p>
          </th>
          <th>
            <p
              class="adminMenu"
              :class="{ active: displaying === 'adminmember' }"
              @click="displaying = 'adminmember'"
            >
              회원 관리
            </p>
          </th>
          <th>
            <p
              class="adminMenu"
              :class="{ active: displaying === 'adminmanager' }"
              @click="displaying = 'adminmanager'"
            >
              점주 관리
            </p>
          </th>
          <th>
            <p
              class="adminMenu"
              :class="{ active: displaying === 'admincar' }"
              @click="displaying = 'admincar'"
            >
              차량 관리
            </p>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td colspan="4">
            <div>
              <!-- displaying 변수의 값에 따라 보여주기 v-show 를 사용해도 무방 -->
              <admindash v-if="displaying === 'admindash'" />
              <adminmember v-else-if="displaying === 'adminmember'" />
              <adminmanager v-else-if="displaying === 'adminmanager'" />
              <admincar v-else-if="displaying === 'admincar'" />
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
// import
import admindash from "./admindash.vue";
import adminmember from "./adminmember.vue";
import adminmanager from "./adminmanager.vue";
import admincar from "./admincar.vue";

export default {
  // components 설정
  components: {
    admindash,
    adminmember,
    adminmanager,
    admincar,
  },
  // 변수 설정
  data() {
    return {
      displaying: "admindash", // 초기값은 대시보드로 설정
    };
  },
  mounted () {
    let auth = JSON.parse(sessionStorage.getItem('login')).auth;
    if(auth !== 1){
      this.$router.push('/');
    }
  },
};
</script>

<style>
/* 어드민 메뉴에 마우스를 올렸을 경우 밑줄 + 커서 모양 변경 */
p.adminMenu:hover {
  text-decoration: underline;
  cursor: pointer;
}
</style>
<style src="@/components/basic/CSS/common.css"></style>
<style src="@/components/admin/CSS/admin.css"></style>
