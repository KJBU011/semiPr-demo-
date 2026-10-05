import login from '../../components/member/login.vue';
import regi from '../../components/member/regi.vue';
import my from '@/components/member/my.vue';
import myupdate from '@/components/member/myupdate.vue';

export default [

    // 로그인 페이지
    {
        path:'/login',
        name:'login',
        component:login
    },

    // 회원가입 페이지
    {
        path:'/regi',
        name:'regi',
        component:regi
    },
    {
        path: '/my', 
        name: 'my', 
        component: my,
        // [추가] 로그인해야만 접근 가능한 페이지로 표시.
        // routers/index.js의 전역 가드(router.beforeEach)가 이 값을 보고 로그인 여부를 검사함.
        meta: { requiresAuth: true }
    },
    {
        path: '/myupdate', 
        name: 'myupdate', 
        component: myupdate,
        // [추가] 로그인해야만 접근 가능한 페이지로 표시.
        meta: { requiresAuth: true }
    },
];