<template>
    <div class="btn-container">
        <button @click="btn" v-if="$store.state.account.isLogin" class="btn">login된 id : {{loginId}}</button>
        <a v-else>token이 존재하지 않습니다.</a>
    </div>
</template>


<script>
import axios from 'axios';
import store from '@/script/store';
import { ref } from 'vue';
export default ({
    setup() {
      const loginId = ref('')
      const btn = () =>{
        //alert("click btn");
        axios.post("/api/admin/test")
          .then((res) => {
            console.log("res : " + res.data)
            loginId.value = res.data
          }).catch(() =>{
            window.alert("로그인 정보가 존재하지 않습니다.");
          })
      }
     
     return { btn, store, loginId }
    },
    
})
</script>

<style scoped>

</style>
