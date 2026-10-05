import board from '@/components/board/board.vue';
import boardlist from '@/components/board/boardlist/boardlist.vue';
import boarddetail from '@/components/board/boardlist/boarddetail.vue';
import boardwrite from '../../components/board/boardlist/boardwrite.vue';
import faqboard from "@/components/board/faq/faqboard.vue";
import QnaBoard from "@/components/board/qna/qnaboard.vue";
import QnaDetail from "@/components/board/qna/qnadetail.vue";
import QnaWrite from "@/components/board/qna/qnawrite.vue";
import QnaAnswer from "@/components/board/qna/qnaanswer.vue";

export default [
    {
        path:'/board',
        name:'board',
        component:board
    },
    {
        path:'/boardlist',
        name:'boardlist',
        component:boardlist
    },
    {
        path:'/boarddetail',
        name:'boarddetail',
        component:boarddetail
    },
    {
        path:'/boardwrite',
        name:'boardwrite',
        component:boardwrite
    },
    {
        path: '/faqboard',
        name: 'faqboard',
        component: faqboard
    },
    {
        path: "/qnaboard",
        name: "qna",
        component: QnaBoard,
    },
    {
        path: "/qnaboard/detail",
        name: "qnadetail",
        component: QnaDetail,
    },
    {
        path: "/qnaboard/write",
        name: "qnawrite",
        component: QnaWrite,
        meta: { requiresAuth: true, },
    },
    {
        path: "/qnaboard/answer/:seq",
        name: "qnaanswer",
        component: QnaAnswer,
        meta: { requiresAuth: true, },
    },
]