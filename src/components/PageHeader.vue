<template>
  <header>
    <div class="navbar navbar-dark bg-dark shadow-sm">
      <div class="container d-flex justify-content-between align-items-center position-relative">
        <a href="#" class="navbar-brand d-flex align-items-center">
          <!-- <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" aria-hidden="true" class="me-2" viewBox="0 0 24 24">
            <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/>
            <circle cx="12" cy="13" r="4"/>
          </svg> -->
          <img src="/img/ho.svg" alt="" height="30">
          <strong v-if="isUserSettingRoute || isAdminSettingRoute">blog</strong>
          <strong v-else>{{this.input.blogName}}</strong>
        </a>
        <router-link v-if="isLogin && this.$store.state.account.userRole ==='ROLE_ADMIN'" 
                     to="/board/write" class="btn btn-light rounded-pill px-3">새글작성</router-link>

        <!-- Toggle Button -->
        <button class="navbar-toggler" @click="toggleMenu">
          <span class="navbar-toggler-icon"></span>
        </button>
      </div>
    </div>

    <!-- Collapsible Menu -->
    <div class="collapsible-menu" :class="{ 'collapsed': isCollapsed }">
      <div class="menu-container">
        <ul>
          <li><a @click="goToPages('Home')">내 블로그</a></li>
          <li>
            <a v-if="isLogin"
               @click="goToPages('Setting')" class="text-white">설정</a>
          </li>
          <li>
            <a v-if="!isLogin" @click="login" class="text-white">로그인</a>
            <a v-else @click="logout" class="text-white">로그아웃</a>
          </li>
        </ul>
      </div>
    </div>
    <!-- Collapsible Menu -->
  </header>
</template>

<script>
import store from '@/script/store';
import router from '@/script/router';

export default {
  name: 'PageHeader',
  data() {
    return {
      isCollapsed: false,
      input:{
        introduction: '',
        blogName: '',
        imgPath: '',
        name: ''
      },
      userRole : localStorage.getItem('user_role')
    };
  },
  computed: {
    isLogin() {
      return this.$store.state.account.isLogin;
    },
    isUserSettingRoute() {
      return this.$route.name === 'UserSetting';
    },
    isAdminSettingRoute() {
      return this.$route.name === 'AdminSetting';
    }
  },
  mounted(){
    this.fnGetUserInfo();
  },
  methods: {
    toggleMenu() {
      this.isCollapsed = !this.isCollapsed;
    },
    login() {
      router.push("/login");
    },
    logout() {
      localStorage.clear();
      store.commit('setAccount', {
        isLogin: false
      });
      window.alert("로그아웃 되었습니다.");
      this.goToPages('Home');
    },
    goToPages(type) {
      if (type === 'Home') {
        router.push({ name: 'Home' });
      } else {
        if(this.$store.state.account.userRole === 'ROLE_ADMIN'){
          router.push({ name: 'AdminSetting' });
        }else{
          router.push({ name: 'UserSetting' });
        }
        
      }
      this.isCollapsed = false;
    },
    // 회원정보 불러오기
    fnGetUserInfo(){
      const apiUrl = '/api/all/getAdminSetInfo/' + 24
      this.$axios.get(apiUrl)
      .then((res)=>{
        this.input.name = res.data.name;
        this.input.introduction = res.data.introduction;
        this.input.blogName = res.data.blogName;
        this.input.imgPath = res.data.imgPath; 
        //console.log('ddd : ' + JSON.stringify(res));
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      });
    },
  },
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
  top: 60px; 
  right: 0; 
  width: 200px; 
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

