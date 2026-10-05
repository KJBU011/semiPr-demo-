import { createApp } from 'vue';
import App from './App.vue';
import router from './routers/index.js';
import axios from 'axios';

// API base: Vercel env(VITE_API_BASE) -> Render backend, 없으면 로컬 :3000
axios.defaults.baseURL = import.meta.env.VITE_API_BASE || 'http://localhost:3000';

/* CSS */
import './css/Basic.css';
// import './css/Member.css';
// import './css/Parking.css';
// import './css/Manager.css';
import './css/boardlist.css';
import './css/qnaboard.css';
import './css/qnadetail.css';
import './css/faqlist.css';
import './css/qnawrite.css';
import './css/boardwrite.css';

const app = createApp(App);

app.use(router);
app.mount('#app');