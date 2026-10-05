import { createWebHistory, createRouter } from 'vue-router'; // npm i vue-router@next
import home from './basic/home.js';
import store from './store/store.js';
import member from './member/member.js';
import admin from './admin/admin.js';
import manager from './manager/manager.js';
import parking from './parking/parking.js';
import board from './board/board.js';

const router = createRouter({
    history:createWebHistory(),
    routes:[
        ...home,
        ...store,
        ...member,
        ...admin,
        ...manager,
        ...parking,
        ...board
    ]
})

// [추가] 전역 네비게이션 가드.
// 라우트에 meta: { requiresAuth: true }가 붙어있는 페이지는, 로그인하지 않은 상태로
// 접근하면 로그인 페이지로 돌려보냄. (login.vue, my.vue, myupdate.vue와 세트로 동작)
router.beforeEach((to, from, next) => {

    // sessionStorage에 'login' 키가 있으면 로그인 상태로 간주.
    // login.vue의 login() 메소드가 로그인 성공 시 이 키에 회원 정보를 저장해둠.
    const isLoggedIn = sessionStorage.getItem('login') !== null;

    // 이동하려는 라우트(또는 그 상위 라우트) 중에 requiresAuth: true가 하나라도 있는지 확인.
    // (라우트를 중첩해서 쓰는 경우까지 대비해 to.matched 배열 전체를 검사)
    const requiresAuth = to.matched.some((record) => record.meta && record.meta.requiresAuth);

    // 로그인이 필요한 페이지인데 로그인 상태가 아닌 경우
    if (requiresAuth && !isLoggedIn) {

        // 로그인 성공 후 원래 가려던 위치로 되돌아갈 수 있도록 경로를 저장.
        // login.vue의 login() 메소드가 로그인 성공 시 이 값을 읽어서 그 위치로 이동시킴.
        sessionStorage.setItem('location', to.fullPath);

        alert('로그인이 필요한 페이지입니다.');
        next({ name: 'login' }); // 로그인 페이지로 이동시키고 원래 요청한 이동은 취소
    }
    // 로그인이 필요 없는 페이지이거나, 이미 로그인된 상태인 경우
    else {
        next(); // 정상적으로 이동 허용
    }
});

export default router;
