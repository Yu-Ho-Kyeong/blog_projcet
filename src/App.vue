<template>
  <div>
    <!-- 헤더 컴포넌트-->
    <PageHeader />
    <!--태그 컴포넌트 -->
    <aside class="main-tag" v-if="$route.path === '/'" >     
      <div class="main-tag-name">태그 목록</div>
        <ul v-for="(titem, idx) in state.tagItems" :key="idx"> 
          <TagList :titem="titem" v-if="titem"/>
        </ul>
    </aside>
    <!-- 페이지 이동 컴포넌트-->
    <RouterView />     
    <!-- <PageFooter /> 푸터 컴포넌트-->
  </div>
</template>

<script>
import PageHeader from './components/PageHeader.vue'  // 헤더
import TagList from '@/pages/tag/TagList.vue'
//푸터 import PageFooter from './components/PageFooter.vue'
import { reactive } from 'vue';
import store from '@/script/store'                    //
import axios from 'axios'                             //
import { useRoute } from 'vue-router'                 //
import { watch } from 'vue'                           //

export default {
  name :'App',
  components: { PageHeader, TagList },
    
  setup(){
    const state = reactive({
      tagItems:[]
    });
    const check = () => {
      axios.get("/api/user/check").then(({data})=>{
        console.log("토큰 check : " + data);
        if(data){
          store.commit("setAccount", data);
        }else{
          store.commit("setAccount", 0);
        }
      })
    }

    // 태그목록 불러오기
    axios.get("/api/user/tag/getTags").then(({data}) => { 
      state.tagItems = data;
      console.log("tag_data : " + JSON.stringify(data), null, 2);
    })
    .catch((error) => {
      console.error('Tag API 호출 중 에러 발생:', error);
    });

    const route  = useRoute();
    watch(route, ()=> {
      check();
    })
    return { state }
  }
}
</script>

<style scoped>
  .main-tag{
      position: fixed; 
      top: 440px;
      left: 50%;
      transform: translateX(-540px);
      width: 200px; 
      background: #fff;
      border: 1px solid #aaa;
    }
  .main-tag-name{
    border-bottom:1px;
  }
</style>