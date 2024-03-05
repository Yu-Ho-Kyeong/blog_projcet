<template>
  <div class="signup-container">
    <h1>회원가입</h1>
    <form @submit.prevent="submitForm">
      

      <div class="form-group">
        <label for="userId">아이디</label>
        <div class="input-with-button">
          <input v-model="userId" type="text" id="userId" class="form-control" required />
          <button class="btn btn-idChk">중복검사</button>
        </div>
      </div>

      <div class="form-group">
        <label for="userPw">비밀번호</label>
        <input v-model="userPw" type="password" id="userPw" class="form-control" required />
      </div>

      <div class="form-group">
        <label for="name">이름</label>
        <input v-model="name" type="text" id="name" class="form-control" required />
      </div>

      <div class="form-group">
        <label for="name">한줄 소개</label>
        <input v-model="introduction" type="text" id="introduction" class="form-control" required />
      </div>

      <div class="button-group">
        <button type="button" class="btn btn-secondary" @click="cancle()">취소</button>
        <button type="submit" class="btn btn-primary">가입하기</button>
      </div>
    </form>
    <router-link to="/login">이미 계정이 있으신가요? 로그인</router-link>
  </div>
</template>

<script>
import axios from 'axios';
import router from '@/script/router';
import { ref } from 'vue';
export default ({
    setup() {
      const name = ref('');
      const userId = ref('');
      const userPw = ref('');
      const introduction = ref('');
      const cancle = () => {          //취소버튼
          router.push({path:'/'});    
      }
      const submitForm = async () =>{
        try{
          const response = await axios.post('/api/all/signup',{
            name: name.value,
            userId: userId.value,
            userPw: userPw.value,
            introduction: introduction.value
          });
           // Response를 로그에 출력하거나 필요에 따라 사용
          console.log(response);
          // 서버에서 올바른 응답을 받으면 로그인 페이지로 이동
          alert('회원가입되었습니다.')
          router.push('/login');
        }catch(error){
          console.log('회원가입 실패', error.response.data);
        }
      }
      return { name, userId, userPw, introduction, submitForm, cancle }
    },
    
})
</script>


<style scoped>
  .signup-container {
    max-width: 40%;
    margin: auto;
    padding: 20px;
    margin-top: 5rem;
    margin-bottom: 5rem;
  }

  .form-group {
    margin-bottom: 20px;
  }

  label {
    font-size: 1.0rem;
    margin-bottom: 5px;
    display: block;
  }

  input.form-control {
    width: 100%;
    font-size: 1.5rem;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 3px;
    transition: border-color 0.2s;
  }

  input.form-control:focus {
    border-color: #007bff;
  }

  .input-with-button {
    display: flex;
    align-items: center;
  }

  .input-with-button .btn-idChk {
    margin-left: 10px;
  }

  .button-group {
    display: flex;
    justify-content: space-between;
  }

  button.btn {
    width: 48%;
    padding: 10px;
    border: none;
    border-radius: 3px;
    cursor: pointer;
  }

  button.btn-secondary {
    background-color: #6c757d;
    color: #fff;
  }

  button.btn-primary {
    background-color: #007bff;
    color: #fff;
  }

  button.btn:hover {
    background-color: #0056b3;
  }
</style>