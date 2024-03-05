<template>
  <div>
    <!-- 헤더 컴포넌트-->
    <PageHeader />
    <!-- 페이지 이동 컴포넌트-->
    <RouterView />     
    <!-- <PageFooter /> 푸터 컴포넌트-->
  </div>
</template>

<script>
import PageHeader from './components/PageHeader.vue'  // 헤더
import store from '@/script/store'                    
import { useRoute } from 'vue-router'; 
import { watch } from 'vue'; 

export default {
  name :'App',
  components: { PageHeader },
  mounted() {
    window.onload = () => {
      //console.log('window.onload');
      const refreshToken = localStorage.getItem('user_refresh_token');
      if (refreshToken) {
        //console.log('refreshToken있음');
        this.requestNewAccessToken(refreshToken);
      }
    };
    //this.check();
    
    this.route = useRoute();
    watch(this.route, () => {
      this.check();
    });
  },
  methods: {
    check() {
      console.log('check()');
      const accessToken = localStorage.getItem('user_access_token');
      if (accessToken) {
        console.log('accessToken : ' + accessToken);
        const userId = localStorage.getItem('user_id');
        const userRole = localStorage.getItem('user_role');
        const userAccessToken = localStorage.getItem('user_access_token');
        const userRefreshToken = localStorage.getItem('user_refresh_token');
        store.commit('setAccount', {
          isLogin: true,
          userId : userId,
          userRole : userRole,
          userAccessToken : userAccessToken,
          userRefreshToken : userRefreshToken
        });

        console.log('account : ' + JSON.stringify(this.$store.state.account));
      }
    },
    requestNewAccessToken(refreshToken) {
      const reqData = {"refreshToken" : refreshToken};  
      this.$axios.post('/api/all/refreshAccessToken', reqData, {
        headers: {
          'Content-type': 'application/json'
        }
      }).then((res) => {
        const newAccessToken = res.data.accessToken;
        localStorage.setItem('user_access_token', newAccessToken);
      }).catch((err) => {
        console.log(err);
      });
    },
  }
};
</script>
