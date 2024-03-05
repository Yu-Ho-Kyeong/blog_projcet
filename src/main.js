import { createApp } from 'vue';
import store from '@/script/store';
import router from '@/script/router';
import App from './App.vue';
//import axios from 'axios';
import axios from '@/script/util/axios';
import { quillEditor } from 'vue3-quill' // 글쓰기 에디터

const app = createApp(App)
app.config.globalProperties.$axios      = axios;                 // 전역변수로 설정 컴포넌트에서 this.$axios로 사용
app.config.globalProperties.$route      = router;                // 전역변수로 설정 컴포넌트에서 this.$route 사용
app.config.globalProperties.$store      = store;                 // 전역변수로 설정 컴포넌트에서 this.$store 사용
app.config.globalProperties.$serverUrl  = 'localhost:3000'       // api server url
app.config.globalProperties.$boardUrl   = '/api/board'           // api board url
app.config.globalProperties.$tagUrl     = '/api/tag'             // api tag url
app.config.globalProperties.$commentUrl = '/api/comment'         // api comment url 

app.use(store).use(router).use(quillEditor).mount('#app');
