<template>
  <main class="form-signin w-100 m-auto">
    <form @submit.prevent="submit">
        <h1 class="h3 mb-3 fw-normal">패스워드를 입력해주세요</h1>
      <div class="form-floating">
        <input type="text" class="form-control" id="userId" v-model="userId">
        <label for="userId">Id</label>
      </div>
      <div class="form-floating">
        <input type="password" class="form-control" id="userPw" v-model="userPw">
        <label for="userPw">Password</label>
      </div>
      <button type="submit" class="btn btn-danger w-100 py-2">회원탈퇴</button>
    </form>
  </main>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      userId: localStorage.getItem('user_id'),
      userPw: ''
    };
  },
  mounted(){
    console.log('this.$store.state.account : ' + JSON.stringify(this.$store.state.account));
  },
  methods: {
    ...mapActions(['signout']),
    async submit() {
      if (!this.userPw) {
        alert('비밀번호를 입력해주세요.');
        return;
      }
      const userNo = parseInt(localStorage.getItem('user_no'));
      const apiUrl = '/api/user/deleteUser'
     
      // 서버에서 req.body.{} 로 확인
      this.$axios.delete(apiUrl,  {
        data: {userNo: userNo, userPw: this.userPw},
        headers: {
          'Content-Type' : 'application/json',
        },
      })
      .then(()=>{
        // store 초기화
        this.$store.commit('clearAccount', {
            isLogin: false,
            userId: '',
            userRole: '',
            userAccessToken: '',
            userRefreshToken: ''
        });
        // localStorage 초기화
        localStorage.clear();
        this.$router.push({
          name : 'Home'
        })
        //console.log('res : ' + JSON.stringify(res));
      }).catch((err)=>{
        console.log('err : ' + err);
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }else if(err.response && err.response.status === 500){
          alert('비밀번호가 일치하지 않습니다.');
        }
      })
    }
  }
}
</script>

<style scoped>
  html,body {
    height: 100%;
  }
  .fw-normal{
    margin-top: 5rem;
  }
  .form-signin {
    max-width: 530px;
    padding: 1rem;
  }

  .form-signin .form-floating:focus-within {
    z-index: 2;
  }

  .form-signin input[type="text"] {
    margin-bottom: -1px;
    border-bottom-right-radius: 0;
    border-bottom-left-radius: 0;
  }

  .form-signin input[type="password"] {
    margin-bottom: 10px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
  }
  .btn{
    background-color: rgb(0, 0, 0);
    margin-bottom: 5rem;
    cursor: pointer;
  }
  .btn-join{
    float : right;
  }
  .join-a, .form-check-label{
    text-decoration-line: none;
    cursor: pointer;
  }
  .join-a:hover{
    text-decoration-line:underline;
  }
</style>