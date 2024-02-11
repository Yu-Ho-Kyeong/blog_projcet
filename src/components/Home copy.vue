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
            <a href="#" class="btn btn-primary my-2">게시글</a>
            <a href="#" class="btn btn-secondary my-2">시리즈</a>
            <a href="#" class="btn btn-secondary my-2">소개</a>
          </p>
        </div>
      </div>
    </section>

    <div class="album py-5 bg-body-tertiary" id="boar-list">
      <div class="container">
        <aside class="main-tag">
          <div class="main-tag-name">태그 목록</div>
            <ul v-for="(titem, idx) in state.tagItems" :key="idx"> 
              <Tag :titem="titem" v-if="titem"/>
            </ul>
        </aside>

        <div class="row row-cols-1 g-3 con_wrap">
          <div class="col" v-for="(bitem, idx) in state.boardItems" :key="idx"><!-- items: 배열 item : 객체 -->
            <!-- {{item}} 현재는 String 형태의 데이터가 들어가 있음 데이터타입이 Object로 들어가면 객체가 들어감--> 
            <Board :bitem="bitem" v-if="bitem"/>
            {{bitem.boardNo}}
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import Board from '@/pages/board/BoardList.vue';
import Tag from '@/pages/tag/TagList.vue';
import axios from 'axios';
import { reactive } from 'vue'; 
	export default {
  components: { Board, Tag },
		name :'Home',
    setup(){
      const state = reactive({
        boardItems: [],
        tagItems:[]
      });
      // 게시글 정보 불러오기
      axios.get("/api/user/Boards").then(({data}) => { 
        state.boardItems = data;
        console.log("board_date : " + data);
      })
      .catch((error) => {
        console.error('Board API 호출 중 에러 발생:', error);
      });
      // 태그목록 불러오기
      axios.get("/api/user/Tags").then(({data}) => { 
        state.tagItems = data;
        console.log("tag_data : " + data);
      })
      .catch((error) => {
        console.error('Tag API 호출 중 에러 발생:', error);
      });

      return { state };
    }
	}
</script>

<style scoped>
  .profile-pic img{
    border-radius: 50%;
  }
  .main-tag-name{
    border-bottom:1px;
  }
  .container{ 
    max-width: 1080px; 
    position: relative;
  }
  .main-tag{
    position: fixed; 
    top: 440px;
    left: 50%;
    transform: translateX(-540px);
    width: 200px; 
    background: #fff;
    border: 1px solid #aaa;
  }
  .con_wrap{
    padding-left: 220px;
    width: 100%;
  }
  
</style>

