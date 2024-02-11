<template>
	<header data-bs-theme="dark">
  <div class="navbar navbar-dark bg-dark shadow-sm">
    <div class="container d-flex justify-content-between align-items-center position-relative">
      <a href="#" class="navbar-brand d-flex align-items-center">
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" aria-hidden="true" class="me-2" viewBox="0 0 24 24">
          <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/>
          <circle cx="12" cy="13" r="4"/>
        </svg>
        <strong>HK</strong>
      </a>
      <router-link to="/board/write" class="btn btn-light rounded-pill px-3" v-if="$store.state.account.id">새글작성</router-link>
      <!-- Toggle Button -->
        <button class="navbar-toggler" @click="toggleMenu">
          <span class="navbar-toggler-icon"></span>
        </button>
    </div>
  </div>

  <!-- Collapsible Menu -->
  <div class="collapsible-menu" :class="{ 'collapsed': isCollapsed }">
    <div class="menu-container">
      <!-- Your menu items go here -->
      <ul>
        <li><a href="/">내 블로그</a></li>
        <li><router-link to="/setting" class="text-white" v-if="$store.state.account.id">설정</router-link></li>
        <li>
          <!-- <router-link to="/login" class="text-white" v-if="!$store.state.account.id">로그인</router-link> -->
          <a class="text-white" @click="login()" v-if="!$store.state.account.id">로그인</a>
          <a class="text-white" @click="logout()" v-else>로그아웃</a>
        </li>
        
      </ul>
    </div>
  </div>
</header>
</template>

<script>
import store from '@/script/store';
import router from '@/script/router';
import { ref } from 'vue';

	export default {
		name :'PageHeader',
    setup(){
      const isCollapsed = ref(false);

      const toggleMenu = ()=>{
        isCollapsed.value = !isCollapsed.value;
        console.log('test1 : ' + store.state.account.id);
      }
      const login = () =>{
        router.push("/login");
      }
      const logout = ()=> {
        //store.commit('setAccount', 0);
        store.commit('setAccount', {
          id: 0,
          userId: '',
        });
        console.log("store.state.account : " + JSON.stringify(store.state.account, null, 2));
        //sessionStorage.removeItem("id");
        window.alert("로그아웃 되었습니다.");
        router.push({path:"/"});
      }
      
      return { login, logout, toggleMenu, isCollapsed };
    }
	}
</script>

<style scoped>
.btn-light {
  position: absolute;
  right: 100px;
}
/* Navbar styling goes here */

.collapsible-menu {
  overflow: hidden;
  height: 0;
  transition: height 0.3s ease;
  background-color: #343a40;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  position: absolute;
  top: 60px; /* 조절 가능: 상단 여백 설정 */
  right: 0; /* 우측 정렬 설정 */
  width: 200px; /* 조절 가능: 메뉴 너비 설정 */
}

.collapsible-menu.collapsed {
  height: auto;
}

.menu-container {
  padding: 20px;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

ul li {
  margin-bottom: 10px;
  cursor: pointer;
}

.menu-container a {
  text-decoration: none;
  color: #fff;
  font-weight: bold;
  font-size: 16px;
  transition: color 0.3s ease;
}

a:hover {
  color: #0fe267;
}

.text-white{
  cursor: pointer;
}
</style>

