import store from "@/components/store/store.vue";
import store1F2F from "@/components/store/store1F2F.vue";
import store3F4F from "@/components/store/store3F4F.vue";
import store5F from "@/components/store/store5F.vue";

export default [
    {
        path:'/store',
        name:'store',
        component:store
    },
    {
        path:'/store1f2f',
        name:'store1f2f',
        component:store1F2F
    },
    {
        path:'/store3f4f',
        name:'store3f4f',
        component:store3F4F
    },
    {
        path:'/store5f',
        name:'store5f',
        component:store5F
    }
    
]