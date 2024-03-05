import { createRouter, createWebHashHistory } from 'vue-router';
import store from '@/script/store';
import Login from '@/pages/Login';
import Join from '@/pages/Join';
import UserSetting from '@/pages/member/UserSetting';
import AdminSetting from '@/pages/member/AdminSetting';
import Home from '@/components/Home';
import Introducing from '@/pages/homeTab/Introducing';
import Comment from '@/pages/comment/Comment';
import Board from '@/pages/homeTab/BoardList';
import BoardWrite from '@/pages/board/BoardWrite';
import BoardDetail from '@/pages/board/BoardDetail';
//import addIntroduction from '@/pages/homeTab/add_introduction';
import Test from '@/pages/board/test';
import SignOut from '@/pages/member/SignOut'

const requireAuth = () => (from, to, next) => {
    if (store.state.account.isLogin === true) {
      return next()
    }
    next('/login') 
}
// const beforeRouteUpdate = () => (to, from, next) => {
//     // 이전 라우트와 현재 라우트의 정보를 확인할 수 있습니다.
//     console.log('이전 라우트:', from)
//     console.log('현재 라우트:', to)
    
//     // 라우트 변경에 따른 로직을 수행합니다.
//     // 예를 들어 데이터를 다시 가져오거나 특정 작업을 수행할 수 있습니다.
    
//     next() // 반드시 next()를 호출하여 내비게이션을 계속 진행해야 합니다.
//   }
const routes = [
    { path: '/', name : 'Home', component: Home },                                          // 메인
    { path: '/login', name : 'Login', component: Login },                                   // 로그인
    { path: '/join', name : 'Join', component: Join },                                      // 회원가입
    { path: '/UserSetting', name : 'UserSetting', component: UserSetting },                 // 회원설정
    { path: '/AdminSetting', name : 'AdminSetting', component: AdminSetting },              // 관리자설정
    { path: '/signOut', name : 'SignOut', component: SignOut },                             // 회원탈퇴
    { path: '/comment', name : 'Comment', component: Comment },                             // 회원댓글
    { path: '/introduce', name : 'Introducing', component: Introducing },                   // 소개페이지
    
    { path: '/board', name : 'Board', component: Board },                                   // 게시글 목록
    { path: '/board/write', name : 'BoardWrite', component: BoardWrite},                    // 새글작성
    { path: '/board/detail/', name : 'BoardDetail', component: BoardDetail},                 // 게시글 상세
    { path: '/board/test', name : 'Test', component: Test, beforeEnter: requireAuth()}      // tes페이지
];

const router = createRouter({
    mode: 'history',
    history: createWebHashHistory(),
    routes
});


// 사용자가 로그인되어 있는지 확인하는 함수
// const isLoggedIn = () => {
//     const token = store.state.account.token
//     if(token){
//         console.log("token_check : " + token);
//         return true;
//     }
//     return false; 
// }

export default router;