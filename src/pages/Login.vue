<template>

  <main class="form-signin w-100 m-auto">
    <form>
      <h1 class="h3 mb-3 fw-normal">로그인</h1>

      <div class="form-floating">
        <input type="text" class="form-control" id="userId" placeholder="id를 입력해주세요" 
              @keyup.enter="submit()"
              v-model="state.form.userId">
        <label for="userId">Id</label>
      </div>
      <div class="form-floating">
        <input type="password" class="form-control" id="userPw" placeholder="password를 입력해주세요" 
              @keyup.enter="submit()"
              v-model="state.form.userPw">
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
import axios from 'axios';
import { reactive } from 'vue'; 
import store from '@/script/store';
import router from '@/script/router';
export default ({
    setup(){
        const state = reactive({
            form:{
                userId:"",
                userPw:""
            }
        })

        // const getToken = (cookieName) => {
        //   const cookies = document.cookie.split(';');
        //   console.log('cookies : ' + cookies);
        //   console.log('cookies_test : ' + document.cookie);
        //   for (let i = 0; i < cookies.length; i++) {
        //       const cookie = cookies[i].trim();
        //       // 쿠키 이름이 일치하는 경우 쿠키 값을 반환
        //       if (cookie.startsWith(cookieName + '=')) {
        //           return cookie.substring(cookieName.length + 1);
        //       }else{
        //         console.log('서버cookie : ' + cookie);
        //         console.log('cookieName : ' + cookieName + '=');
        //         console.log('쿠키이름이 일치하지 않음')
        //       }
        //   }
        //   return null; // 쿠키를 찾지 못한 경우 null 반환
        // }

        const submit = () => {
          if(!state.form.userId){
            alert('id를 입력해주세요.')
            return;
          }
          if(!state.form.userPw){
            alert('password를 입력해주세요.')
            return;
          }

          axios.post("/api/user/login", state.form)
            .then((res) => {
               store.commit('setAccount', {
                        id: res.data,
                        userId: state.form.userId,
                        //name: res.data.userName 
                });  
                //let token = getToken('token');
                
                // if (token) {
                //   console.log(token); // 쿠키 값 출력
                // } else {
                //   console.log("토큰이 존재하지 않습니다.");
                // }
               // store.commit('setToken', token); // 토큰 저장
                


                //console.log('headers : ' + res.headers['set-cookie']);
                //console.log('Content-Type : ' + JSON.stringify(res));
                //console.log('headers : ' + JSON.stringify(res.headers, null, 2));
                //console.log('세션 데이터 : ' + JSON.stringify(store.state.account, null, 2));
                router.push({path:'/'});
                sessionStorage.setItem("id", res.data);
                window.alert("로그인되었습니다.");
            }).catch(() =>{
              window.alert("로그인 정보가 존재하지 않습니다.");
            })
        }
        

        return { state, submit };
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
