<template>

  <main class="form-signin w-100 m-auto">
    <form>
      <h1 class="h3 mb-3 fw-normal">로그인</h1>

      <div class="form-floating">
        <input type="text" class="form-control" id="userId" placeholder="id를 입력해주세요" 
              @keyup.enter="submit()"
              v-model="form.userId">
        <label for="userId">Id</label>
      </div>
      <div class="form-floating">
        <input type="password" class="form-control" id="userPw" placeholder="password를 입력해주세요" 
              @keyup.enter="submit()"
              v-model="form.userPw">
        <label for="userPw">Password</label>
      </div>

        <div class="form-check text-start my-3">
        <input class="form-check-input" type="checkbox" value="remember-me" id="flexCheckDefault"
              @keyup.enter="submit()">
        <label class="form-check-label" for="flexCheckDefault">
          자동로그인
        </label>
      </div>
      <button class="btn btn-primary w-100 py-2" @click="submit()">로그인</button>
      <div class="btn-join">
         아직 회원이 아니시면 <router-link to="/join" class="join-a">회원가입</router-link>을 클릭해주세요.
      </div>
      

    </form>
  </main>
</template>

<script>
import { useStore } from 'vuex'; // vuex 추가
import { reactive, toRefs, computed } from 'vue';
import router from '@/script/router';

export default ({
    setup(){
      const store = useStore(); 
      const errorState = computed(() => store.getters.getErrorState);
      const state = reactive({
        form:{
          userId:"",
          userPw:"" 
        }
      })
      const goToPages = () => {
        router.push({
          name: 'Home'
        })
      }
      const submit = async () => {
        if(!state.form.userId){
          alert('id를 입력해주세요.')
          return;
        }
        if(!state.form.userPw){
          alert('password를 입력해주세요.')
          return;
        }

        try{
          let loginResult = await store.dispatch('login', { // login 액션 호출
            userId: state.form.userId,
            userPw: state.form.userPw
          });
          if (loginResult) goToPages();
        } catch (err) {
          if (err.message.indexOf('Network Error') > -1) {
            alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
          } else {
            alert('로그인 정보를 확인할 수 없습니다.')
          }
        }
      }

      return {...toRefs(state), submit, errorState, goToPages };
    } 
})
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
