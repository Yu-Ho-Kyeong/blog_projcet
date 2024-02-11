import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '@/pages/Login.vue';
import Join from '@/pages/Join.vue';
import Setting from '@/pages/member/Setting.vue';
import Home from '@/components/Home.vue';
import Introducing from '@/pages/homeTab/Introducing.vue';
import Comment from '@/pages/comment/Comment.vue';
import Board from '@/pages/homeTab/BoardList.vue';
import BoardWrite from '@/pages/board/BoardWrite';
import BoardDetail from '@/pages/board/BoardDetail';

const routes = [
    { path: '/', name : 'Home', component: Home },                                  // 메인
    { path: '/login', name : 'Login', component: Login },                           // 로그인
    { path: '/join', name : 'Join', component: Join },                              // 회원가입
    { path: '/setting', name : 'Setting', component: Setting },                     // 회원설정
    { path: '/comment', name : 'Comment', component: Comment },                     // 회원댓글
    { path: '/introduce', name : 'Introducing', component: Introducing },           // 소개페이지
    { path: '/board', name : 'Board', component: Board },                           // 게시글 목록
    { path: '/board/write', name : 'BoardWrite', component: BoardWrite},            // 새글작성
    { path: '/board/detail', name : 'BoardDetail', component: BoardDetail}          // 게시글 상세
];

const router = createRouter({
    mode: 'history',
    history: createWebHashHistory(),
    routes
});

export default router;