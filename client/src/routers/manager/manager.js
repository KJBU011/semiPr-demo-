import manager from "@/components/manager/manager.vue";

export default[
    {
        path:'/manager',
        name: 'manager',
        component: manager,
        meta: { requiresAuth: true }
    }
]