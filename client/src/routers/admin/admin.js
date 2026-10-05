import admin from "@/components/admin/admin.vue";

export default [
    {
        path:'/admin',
        name:'admin',
        component:admin,
        meta: { requiresAuth: true }
    },
]