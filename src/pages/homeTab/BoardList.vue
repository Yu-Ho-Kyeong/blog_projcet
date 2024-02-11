<template>
      <div class="container">
        <div class="row row-cols-1 g-3 con_wrap">
          <div class="col" v-for="(bitem, idx) in state.boardItems" :key="idx"><!-- items: 배열 item : 객체 -->
            <!-- {{item}} 현재는 String 형태의 데이터가 들어가 있음 데이터타입이 Object로 들어가면 객체가 들어감--> 
            <Board :bitem="bitem" v-if="bitem"/>

          </div>
        </div>
      </div>
 
</template>

<script>
import Board from '@/pages/board/BoardList.vue';
import axios from 'axios';
import { reactive } from 'vue'; 
export default {
  components: { Board },
	name :'Home',
    setup(){
      const state = reactive({
        boardItems: [],
        tagItems:[]
      });
      // 게시글 정보 불러오기
      axios.get("/api/user/board/getBoards").then(({data}) => { 
        state.boardItems = data;
        console.log("board_data : " + JSON.stringify(data), null, 2);
      })
      .catch((error) => {
        console.error('Board API 호출 중 에러 발생:', error);
      });
      return { state };
    }
}
</script>
<style scoped>

  .container{ 
    max-width: 1080px; 
    position: relative;
  }
  
  .con_wrap{
    padding-left: 220px;
    width: 100%;
  }
</style>