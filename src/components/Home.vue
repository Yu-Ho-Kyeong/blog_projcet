<template>
  <div class="home">
    <section class="py-1 text-center container">
      <div class="row py-lg-5">
        <div class="col-lg-6 col-md-8 mx-auto">
          <a class="profile-pic" href="">
            <img alt="profile" fetchpriority="high" width="128" height="128" 
                decoding="async" data-nimg="1" style="color:transparent" 
                src="https://images.velog.io/images/cindy-choi/profile/1dcf051d-4820-472b-a3fc-2782449f2430/9D473C99-114F-4D41-A8F3-46B421A49FD1.png">
            </a>
          
          <h1 class="fw-light">HK 개발블로그</h1>
          <p class="lead text-body-secondary">안녕나는클레오파트라세상에서제일가는포테이토칩</p>
          <div></div>
          <p> 
            <a href="#" @click="movePage('board-list')" class="btn btn-primary my-2">게시글</a>
            <a href="#" class="btn btn-secondary my-2">시리즈</a>
            <a href="#" @click="movePage('introduce')" class="btn btn-secondary my-2">소개</a>
          </p>
        </div>
      </div>
    </section>

    <div class="home-tab">
      <div class="album py-5 bg-body-tertiary" v-if="currentPage === 'board-list'">
        <BoardList />
      </div>
      <div class="album py-5 bg-body-tertiary" v-else-if="currentPage === 'introduce'">
        <Introduce />
      </div>
      <!-- <div class="album py-5 bg-body-tertiary" id="boar-list" style="display:none">
        <BoardList />
      </div> -->
    </div>
  </div>
</template>

<script>
import BoardList from '@/pages/homeTab/BoardList.vue';
import Introduce from '@/pages/homeTab/Introducing.vue';
import { ref } from 'vue';
	export default {
    components: { BoardList, Introduce },
    setup(){
      const currentPage = ref('board-list');
      const movePage = (id)=> {
        console.log('id : '+ id);
        if(id === 'board-list'){
          currentPage.value = 'board-list';
        }else if(id === 'introduce'){
          currentPage.value = 'introduce';
        }
        //console.log('currentPage : '+ currentPage.value);
      }
      return { movePage, currentPage }
   }
  }
</script>

<style scoped>
  .profile-pic img{
    border-radius: 50%;
  }
  .container{ 
    max-width: 1080px; 
    position: relative;
  } 
</style>

